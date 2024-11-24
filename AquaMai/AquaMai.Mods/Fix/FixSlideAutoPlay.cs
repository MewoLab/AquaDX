using System.Collections.Generic;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;
using Monitor;

namespace AquaMai.Mods.Fix;

[ConfigSection(exampleHidden: true, defaultOn: true)]
public class FixSlideAutoPlay
{
    /* 这个 Patch 用于修复以下 bug:
     *     SlideFan 在 AutoPlay 时, 只有第一个箭头会消失
     * 原 method 逻辑如下:
     *
     *     if (this.IsNoteCheckTimeStartIgnoreJudgeWait())
     *     {
     *         // do something ...
     *         if (!GameManager.IsAutoPlay())
     *         {
     *             // do something ...
     *             for (int index = 0; index < this._arrowPrefubs.Length && (double) index < (double) num2 * 11.0; ++index)
     *             {
     *                 // do something about displaying arrows ...
     *             }
     *         }
     *         else
     *         {
     *             float num4 = (currentMsec - this.StarLaunchMsec) / (this.StarArriveMsec - this.StarLaunchMsec - this.lastWaitTime);
     *             for (int index = 0; index < this._arrowPrefubs.Length && (double) index < (double) num4 * 1.0; ++index)
     *             {
     *                 // do something about displaying arrows ...
     *             }
     *             if ((double) num4 > 1.0)
     *                 num1 = 3;
     *         }
     *         // do something ...
     *     }
     *
     * 导致这个 bug 的原因是 else 分支的 for 循环终止条件写错了, 应该是 11.0 (因为有 11 个箭头), SBGA 写成了 1.0
     * 这个 method 中一共只有 5 处 ldc.r4 的 IL Code, 依次为 10.0, 11.0, 1.0, 1.0, 0.0
     * 修复 bug 需要把第三处的 1.0 更改为 11.0, 这里使用 Transpiler 解决
     */
    [HarmonyTranspiler]
    [HarmonyPatch(typeof(SlideFan), "NoteCheck")]
    private static IEnumerable<CodeInstruction> FixFanAutoPlayArrow(IEnumerable<CodeInstruction> instructions)
    {
        List<CodeInstruction> instList = new List<CodeInstruction>(instructions);
        bool found = false;
        for (int i = 0; i < instList.Count; i++)
        {
            CodeInstruction inst = instList[i];
            if (inst.LoadsConstant(11.0))
            {
                found = true;
            }

            if (found && inst.LoadsConstant(1.0))
            {
                inst.operand = 11.0f;
                break;
            }
        }
        return instList;
    }

    /* 这个 Patch 让 Slide 在 AutoPlay 的时候, 每个区仍然会分按下和松开两段进行推进 (加上 this._hitIn 的变化)
     * 原 method 逻辑如下:
     *
     *     if (!GameManager.IsAutoPlay())
     *     {
     *         // do somethings ...
     *     }
     *     else
     *     {
     *         float num1 = (currentMsec - this.StarLaunchMsec) / (this.StarArriveMsec - this.StarLaunchMsec - this.lastWaitTime);
     *         this._hitIndex = (int) ((double) this._hitAreaList.Count * (double) num1);
     *         if (this._hitIndex >= this._hitAreaList.Count)
     *             this._hitIndex = this._hitAreaList.Count - 1;
     *         if (this._hitIndex < 0)
     *             this._hitIndex = 0;
     *         int num2 = (int) ((double) this._dispLaneNum * this.GetDeleteArrowDistance());
     *         // do somethings ...
     *     }
     *
     * 现在要在 this.GetDeleteArrowDistance() 之前插入
     *     this._hitIn = ((float)this._hitAreaList.Count * num1 > (float)this._hitIndex + 0.5f);
     * 这段代码, 可以采用 Prefix, GetDeleteArrowDistance() 只在两个地方调用过, 另一处就在上面的 if 分支中 (即非 AutoPlay 情况)
     */
    [HarmonyPrefix]
    [HarmonyPatch(typeof(SlideRoot), "GetDeleteArrowDistance")]
    private static void FixSlideAutoPlayArrow(
        SlideRoot __instance, ref bool ____hitIn, int ____hitIndex, List<SlideManager.HitArea> ____hitAreaList,
        float ___StarLaunchMsec, float ___StarArriveMsec, float ___lastWaitTime
    )
    {
        if (GameManager.IsAutoPlay())
        {
            float prop = (NotesManager.GetCurrentMsec() - ___StarLaunchMsec) / (___StarArriveMsec - ___StarLaunchMsec - ___lastWaitTime);
            ____hitIn = ____hitAreaList.Count * prop > ____hitIndex + 0.5f;
        }
    }

}

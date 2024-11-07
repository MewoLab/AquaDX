using System.Collections.Generic;
using System.Reflection;
using AquaMai.Attributes;
using HarmonyLib;
using Manager;

namespace AquaMai.Mods.Fix;

[ConfigSection(defaultOn: true, hideInExample: true)]
[GameVersion(23000)]
public class FixConnSlide
{
    /* 这个 Patch 用于修复以下 bug:
     *     非 ConnSlide 被错误解析为 ConnSlide (Fes 首日刹那旅程爆机 bug)
     * 原 method 逻辑如下:
     *
     *     if (this.IsSlideAll(noteData1.type) && (index1 + 1 < this._note._noteData.Count ? 1 : 0) != 0)
     *     {
     *         int targetNote = noteData1.slideData.targetNote;
     *         if (noteData1.slideData != null)
     *             targetNote = noteData1.slideData.targetNote;
     *         for (int index3 = index1; index3 < this._note._noteData.Count; ++index3)
     *         {
     *             NoteData noteData3 = this._note._noteData[index3];
     *             if (this.IsSlideAll(noteData3.type) && noteData3.time == noteData1.end && noteData3.startButtonPos == targetNote && noteData3.parent == null)
     *             {
     *                 noteData3.parent = noteData1.parent;
     *                 noteData1.child.Add(noteData3);
     *                 noteData3.isUsed = true;
     *                 noteData3.isJudged = true;
     *                 break;
     *             }
     *         }
     *     }
     *
     * 修复 bug 需要把第二次调用 this.IsSlideAll() 更改为 this.IsConnectNote(), 这里使用 Transpiler 解决
     */
    [HarmonyTranspiler]
    [HarmonyPatch(typeof(NotesReader), "calcSlide")]
    private static IEnumerable<CodeInstruction> Fix(IEnumerable<CodeInstruction> instructions)
    {
        List<CodeInstruction> instList = new List<CodeInstruction>(instructions);
        bool found = false;
        MethodInfo methodIsSlideAll = AccessTools.Method(typeof(NotesReader), "IsSlideAll");
        MethodInfo methodIsConnectNote = AccessTools.Method(typeof(NotesReader), "IsConnectNote");

        for (int i = 0; i < instList.Count; i++)
        {
            CodeInstruction inst = instList[i];
            if (!found && inst.Calls(methodIsSlideAll))
            {
                found = true;
                continue;
            }

            if (found && inst.Calls(methodIsSlideAll))
            {
                inst.operand = methodIsConnectNote;
                // MelonLogger.Msg($"[FixConnSlide] Successfully patched NotesReader::calcSlide");
                break;
            }
        }
        return instList;
    }
}

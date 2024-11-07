using HarmonyLib;
using Monitor;

namespace AquaMai.Mods.Visual;

public class FanJudgeFlip
{
    /*
     * 这个 Patch 让 Wifi Slide 的判定显示有上下的区别 (原本所有 Wifi 的判定显示都是朝向圆心的), 就像 majdata 里那样
     * 这个 bug 产生的原因是 SBGA 忘记给 Wifi 的 EndButtonId 赋值了
     * 不过需要注意的是, 考虑到圆弧形 Slide 的判定显示就是永远朝向圆心的, 我个人会觉得这个 Patch 关掉更好看一点
     */
    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideFan), "Initialize")]
    private static void FixFanJudgeFilp(
        int[] ___GoalButtonId, SlideJudge ___JudgeObj
    )
    {
        if (null != ___JudgeObj)
        {
            if (2 <= ___GoalButtonId[1] && ___GoalButtonId[1] <= 5)
            {
                ___JudgeObj.Flip(false);
                ___JudgeObj.transform.Rotate(0.0f, 0.0f, 180f);
            }
            else
            {
                ___JudgeObj.Flip(true);
            }
        }
    }
}

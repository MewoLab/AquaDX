using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: """
        Make the judgment display of WiFi Slide different in up and down (originally all WiFi judgment displays are towards the center), just like in majdata.
        The reason for this bug is that SEGA forgot to assign EndButtonId to WiFi.
        """,
    zh: """
        这个 Patch 让 WiFi Slide 的判定显示有上下的区别 (原本所有 WiFi 的判定显示都是朝向圆心的), 就像 majdata 里那样
        这个 bug 产生的原因是 SBGA 忘记给 WiFi 的 EndButtonId 赋值了
        """)]
public class FanJudgeFlip
{
    /*
     * 这个 Patch 让 WiFi Slide 的判定显示有上下的区别 (原本所有 WiFi 的判定显示都是朝向圆心的), 就像 majdata 里那样
     * 这个 bug 产生的原因是 SBGA 忘记给 WiFi 的 EndButtonId 赋值了
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

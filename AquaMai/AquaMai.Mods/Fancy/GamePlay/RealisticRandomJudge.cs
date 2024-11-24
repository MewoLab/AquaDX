using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: """
        Make the AutoPlay random judgment mode really randomize all judgments (down to sub-judgments).
        The original random judgment will only produce all 15 judgment results from Miss(TooFast) ~ Critical ~ Miss(TooLate).
        Here, it is changed to a triangular distribution to produce all 15 judgment results from Miss(TooFast) ~ Critical ~ Miss(TooLate).
        Of course, it will not consider whether the original Note really has a corresponding judgment (such as Slide should not have non-Critical Prefect).
        """,
    zh: """
        让 AutoPlay 的随机判定模式真的会随机产生所有的判定 (精确到子判定)
        原本的随机判定只会等概率产生 Critical, LateGreat1st, LateGood, Miss(TooLate)
        这里改成三角分布产生从 Miss(TooFast) ~ Critical ~ Miss(TooLate) 的所有 15 种判定结果
        当然, 此处并不会考虑原本那个 Note 是不是真的有对应的判定 (比如 Slide 实际上不应该有小 p 之类的)
        """)]
public class RealisticRandomJudge
{
    // 让 AutoPlay 的随机判定模式真的会随机产生所有的判定 (精确到子判定)
    // 原本的随机判定只会等概率产生 Critical, LateGreat1st, LateGood, Miss(TooLate)
    // 这里改成三角分布产生从 Miss(TooFast) ~ Critical ~ Miss(TooLate) 的所有 15 种判定结果
    // 当然, 此处并不会考虑原本那个 Note 是不是真的有对应的判定 (比如 Slide 实际上不应该有小 p 之类的)

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameManager), "AutoJudge")]
    private static NoteJudge.ETiming RealAutoJudgeRandom(NoteJudge.ETiming retval)
    {
        if (GameManager.AutoPlay == GameManager.AutoPlayMode.Random)
        {
            var x = UnityEngine.Random.Range(0, 8);
            x += UnityEngine.Random.Range(0, 8);
            return (NoteJudge.ETiming) x;
        }
        return retval;
    }
}

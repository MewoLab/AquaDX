using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: """
        This Patch makes the Critical judgment of BreakSlide also flicker like BreakTap
        Recommended to use with custom skins (otherwise the visual effect may not be good)
        """,
    zh: """
        这个 Patch 让 BreakSlide 的 Critical 判定也可以像 BreakTap 一样闪烁
        推荐与自定义皮肤一起使用 (否则视觉效果可能并不好)
        """)]
public class BreakSlideJudgeBlink
{
    /*
     * 这个 Patch 让 BreakSlide 的 Critical 判定也可以像 BreakTap 一样闪烁
     * 推荐与自定义皮肤一起使用 (否则视觉效果可能并不好)
     */
    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideJudge), "UpdateBreakEffectAdd")]
    private static void FixBreakSlideJudgeBlink(
        SpriteRenderer ___SpriteRenderAdd, int ____addEffectCount
    )
    {
        if (!___SpriteRenderAdd.gameObject.activeSelf) return;
        float num = (____addEffectCount & 0b10) >> 1;
        ___SpriteRenderAdd.color = new Color(num, num, num, 1f);
    }
}

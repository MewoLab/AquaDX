using System.Collections.Generic;
using AquaMai.Config.Attributes;
using HarmonyLib;
using MelonLoader;
using TMPro;
using UnityEngine;
using UnityEngine.TextCore.LowLevel;

namespace AquaMai.Mods.Fix;

[ConfigSection(
    en: """
        Use Microsoft YaHei Bold to display characters not in the font library
        Cannot be used together with CustomFont
        """,
    zh: """
        在显示字库里没有的字时使用微软雅黑 Bold 显示
        不可以和 CustomFont 一起使用
        """)]
public class FontFix
{
    private static TMP_FontAsset fontAsset;
    private static List<TMP_FontAsset> fixedFonts = [];

    public static void OnBeforePatch(HarmonyLib.Harmony h)
    {
        var font = new Font(@"C:\Windows\Fonts\msyhbd.ttc");
        fontAsset = TMP_FontAsset.CreateFontAsset(font, 90, 9, GlyphRenderMode.SDFAA, 8192, 8192);
    }

    [HarmonyPatch(typeof(TextMeshProUGUI), "Awake")]
    [HarmonyPostfix]
    public static void PostFix(TextMeshProUGUI __instance)
    {
        if (fixedFonts.Contains(__instance.font)) return;
# if DEBUG
        MelonLogger.Msg($"[FontFix] Fixing font: {__instance.font.name}");
# endif
        __instance.font.fallbackFontAssetTable.Add(fontAsset);
        fixedFonts.Add(__instance.font);
    }
}

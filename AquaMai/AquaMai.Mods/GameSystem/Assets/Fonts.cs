using System.Collections.Generic;
using System.Linq;
using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;
using HarmonyLib;
using MelonLoader;
using TMPro;
using UnityEngine;
using UnityEngine.TextCore.LowLevel;

namespace AquaMai.Mods.GameSystem.Assets;

[ConfigSection(
    en: "Use custom font(s) as fallback or fully replace the original game font.",
    zh: "使用自定义字体作为回退（解决中文字形缺失问题），或完全替换游戏原字体")]
public class Fonts
{
    [ConfigEntry(
        en: """
            Font path(s).
            Use semicolon to separate multiple paths for a fallback chain.
            Microsoft YaHei Bold by default.
            """,
        zh: """
            字体路径
            使用分号分隔多个路径以构成 Fallback 链
            默认为微软雅黑 Bold
            """)]
    private static readonly string paths = "%SYSTEMROOT%/Fonts/msyhbd.ttc";

    [ConfigEntry(
        en: "Add custom font(s) as fallback, use original game font when possible.",
        zh: "将自定义字体作为游戏原字体的回退，尽可能使用游戏原字体")]
    private static readonly bool addAsFallback = true;

    private static List<TMP_FontAsset> fontAssets = [];
    private static readonly List<TMP_FontAsset> processedFonts = [];

    private static TMP_FontAsset replacementFontAsset;
    private static List<TMP_FontAsset> fallbackFontAssets = [];

    public static void OnBeforePatch()
    {
        var paths = Fonts.paths
            .Split(';')
            .Where(p => !string.IsNullOrWhiteSpace(p))
            .Select(FileSystem.ResolvePath);
        var fonts = paths
            .Select(p =>
            {
                var font = new Font(p);
                if (font == null)
                {
                    MelonLogger.Warning($"[Fonts] Font not found: {p}");
                }
                return font;
            })
            .Where(f => f != null);
        fontAssets = fonts
            .Select(f => TMP_FontAsset.CreateFontAsset(f, 90, 9, GlyphRenderMode.SDFAA, 8192, 8192))
            .ToList();

        if (fontAssets.Count == 0)
        {
            MelonLogger.Warning("[Fonts] No font loaded.");
        }
        else if (addAsFallback)
        {
            fallbackFontAssets = fontAssets;
        }
        else
        {
            replacementFontAsset = fontAssets[0];
            fallbackFontAssets = fontAssets.Skip(1).ToList();
        }
    }

    [HarmonyPatch(typeof(TextMeshProUGUI), "Awake")]
    [HarmonyPostfix]
    public static void PostAwake(TextMeshProUGUI __instance)
    {
        if (fontAssets.Count == 0) return;
        if (processedFonts.Contains(__instance.font)) return;

        if (replacementFontAsset != null)
        {
            ProcessReplacement(__instance);
        }
        if (fallbackFontAssets.Count > 0)
        {
            ProcessFallback(__instance);
        }

        processedFonts.Add(__instance.font);
    }

    private static void ProcessReplacement(TextMeshProUGUI __instance)
    {
# if DEBUG
        MelonLogger.Msg($"{__instance.font.name} {__instance.text}");
# endif

        var materialOrigin = __instance.fontMaterial;
        var materialSharedOrigin = __instance.fontSharedMaterial;
        __instance.font = replacementFontAsset;

# if DEBUG
        MelonLogger.Msg($"shaderKeywords {materialOrigin.shaderKeywords.Join()} {__instance.fontMaterial.shaderKeywords.Join()}");
# endif
        // __instance.fontSharedMaterial = materialSharedOrigin;

        // 这样之后该有描边的地方整个字后面都是阴影，它不知道哪里是边
        // materialOrigin.mainTexture = __instance.fontMaterial.mainTexture;
        // materialOrigin.mainTextureOffset = __instance.fontMaterial.mainTextureOffset;
        // materialOrigin.mainTextureScale = __instance.fontMaterial.mainTextureScale;
        // __instance.fontMaterial.CopyPropertiesFromMaterial(materialOrigin);

        // 这样了之后有描边了，但是描边很细
        // __instance.fontMaterial.shader = materialOrigin.shader;
        foreach (var keyword in materialOrigin.shaderKeywords)
        {
            __instance.fontMaterial.EnableKeyword(keyword);
        }
        // __instance.fontMaterial.globalIlluminationFlags = materialOrigin.globalIlluminationFlags;

        // 原来是 underlay，但是复制这三个属性之后就又变成整个字后面都是阴影了
        // __instance.fontMaterial.SetFloat(ShaderUtilities.ID_UnderlayOffsetY, materialOrigin.GetFloat(ShaderUtilities.ID_UnderlayOffsetY));
        // __instance.fontMaterial.SetFloat(ShaderUtilities.ID_UnderlayOffsetX, materialOrigin.GetFloat(ShaderUtilities.ID_UnderlayOffsetX));
        // __instance.fontMaterial.SetFloat(ShaderUtilities.ID_UnderlayDilate, materialOrigin.GetFloat(ShaderUtilities.ID_UnderlayDilate));

        // if(materialOrigin.shaderKeywords.Contains(ShaderUtilities.Keyword_Underlay))
        // {
        //     __instance.fontMaterial.EnableKeyword(ShaderUtilities.Keyword_Glow);
        //     __instance.fontMaterial.SetFloat(ShaderUtilities.ID_GlowOuter, .5f);
        //     // __instance.fontMaterial.SetFloat(ShaderUtilities.ID_UnderlayOffsetX, materialOrigin.GetFloat(ShaderUtilities.ID_UnderlayOffsetX));
        // }
    }

    private static void ProcessFallback(TextMeshProUGUI __instance)
    {
        foreach (var fontAsset in fallbackFontAssets)
        {
            __instance.font.fallbackFontAssetTable.Add(fontAsset);
        }
    }
}

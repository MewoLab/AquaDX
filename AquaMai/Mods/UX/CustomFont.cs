using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using HarmonyLib;
using MelonLoader;
using TMPro;
using UnityEngine;
using UnityEngine.TextCore.LowLevel;

namespace AquaMai.Mods.UX;

public class CustomFont
{
    private static Font font;
    private static TMP_FontAsset fontAsset;

    public static void DoCustomPatch(HarmonyLib.Harmony h)
    {
        var fontPath = Path.Combine(Environment.CurrentDirectory, "LocalAssets", "font.ttf");
        if (!File.Exists(fontPath)) return;

        font = new Font(fontPath);
        fontAsset = TMP_FontAsset.CreateFontAsset(font, 90, 9, GlyphRenderMode.SDFAA, 8192, 8192);
    }

    [HarmonyPatch(typeof(TextMeshProUGUI), "Awake")]
    [HarmonyPostfix]
    public static void PostFix(TextMeshProUGUI __instance)
    {
        if (font is null) return;
# if DEBUG
        MelonLogger.Msg($"{__instance.font.name} {__instance.text}");
# endif

        var materialOrigin = __instance.fontMaterial;
        var materialSharedOrigin = __instance.fontSharedMaterial;
        __instance.font = fontAsset;
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
}

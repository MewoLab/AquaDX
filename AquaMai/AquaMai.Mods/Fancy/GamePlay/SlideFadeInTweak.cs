using System;
using System.Collections.Generic;
using AquaMai.Config.Attributes;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Monitor;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    zh: "让星星在启动拍等待期间从 50% 透明度渐入为 100%，取代原本在击打星星头时就完成渐入",
    en: "Slides will fade in instead of instantly appearing.")]
public class SlideFadeInTweak
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(SlideRoot), "UpdateAlpha")]
    private static bool UpdateAlphaOverwrite(
        SlideRoot __instance,
        ref bool ___UpdateAlphaFlag,
        float ___StartMsec, float ___AppearMsec, float ___StarLaunchMsec, float ___DefaultMsec,
        int ____dispLaneNum, bool ___BreakFlag,
        List<SpriteRenderer> ____spriteRenders, List<BreakSlide> ____breakSpriteRenders
    )
    {
        if (!___UpdateAlphaFlag)
            return false;
        
        var currentMsec = NotesManager.GetCurrentMsec();
        var slideSpeed = (int) Singleton<GamePlayManager>.Instance.GetGameScore(__instance.MonitorId).UserOption.SlideSpeed;
        var defaultFadeInLength = (21 - slideSpeed) / 10.5f * ___DefaultMsec;
        var fadeInFirstMsec = Math.Max(___StartMsec, ___AppearMsec - defaultFadeInLength);
        var fadeInSecondMsec = Math.Max(___AppearMsec, ___StarLaunchMsec - defaultFadeInLength);
        // var fadeInSecondMsec = ___AppearMsec;
        var color = new Color(1f, 1f, 1f, 1f);
        
        if (currentMsec >= ___StarLaunchMsec)
        {
            ___UpdateAlphaFlag = false;
        }
        else if (currentMsec < fadeInFirstMsec)
        {
            color.a = 0.0f;
        }
        else if (fadeInFirstMsec <= currentMsec && currentMsec < ___AppearMsec)
        {
            var fadeInLength = Math.Min(200.0f, ___AppearMsec - fadeInFirstMsec);
            color.a = 0.5f * Math.Min(1f, (currentMsec - fadeInFirstMsec) / fadeInLength);
        }
        else if (___AppearMsec <= currentMsec && currentMsec < fadeInSecondMsec)
        {
            color.a = 0.5f;
        }
        else if (fadeInSecondMsec <= currentMsec && currentMsec < ___StarLaunchMsec)
        {
            var fadeInLength = Math.Min(200.0f, ___StarLaunchMsec - fadeInSecondMsec);
            // var fadeInLength = ___StarLaunchMsec - fadeInSecondMsec;
            color.a = 0.5f + 0.5f * Math.Min(1f, (currentMsec - fadeInSecondMsec) / fadeInLength);
        }

        if (!___BreakFlag)
        {
            for (var index = 0; index < ____dispLaneNum; ++index)
            {
                if (index >= ____spriteRenders.Count) break;
                ____spriteRenders[index].color = color;
            }
        }
        else
        {
            for (var index = 0; index < ____dispLaneNum; ++index)
            {
                if (index >= ____breakSpriteRenders.Count) break;
                ____breakSpriteRenders[index].SpriteRender.color = color;
            }
        }

        return false;
    }
    
    [HarmonyPrefix]
    [HarmonyPatch(typeof(SlideFan), "UpdateAlpha")]
    private static bool UpdateFanAlphaOverwrite(
        SlideRoot __instance,
        float ___StartMsec, float ___AppearMsec, float ___StarLaunchMsec, float ___DefaultMsec,
        Color ____defaultColor, SpriteRenderer[] ____spriteLines
    )
    {
        var currentMsec = NotesManager.GetCurrentMsec();
        
        var slideSpeed = (int) Singleton<GamePlayManager>.Instance.GetGameScore(__instance.MonitorId).UserOption.SlideSpeed;
        var defaultFadeInLength = (21 - slideSpeed) / 10.5f * ___DefaultMsec;
        var fadeInFirstMsec = Math.Max(___StartMsec, ___AppearMsec - defaultFadeInLength);
        var fadeInSecondMsec = Math.Max(___AppearMsec, ___StarLaunchMsec - defaultFadeInLength);
        // var fadeInSecondMsec = ___AppearMsec;
        var color = ____defaultColor;
        
        if (currentMsec < fadeInFirstMsec)
        {
            color.a = 0.0f;
        }
        else if (fadeInFirstMsec <= currentMsec && currentMsec < ___AppearMsec)
        {
            var fadeInLength = Math.Min(200.0f, ___AppearMsec - fadeInFirstMsec);
            color.a = 0.3f * Math.Min(1f, (currentMsec - fadeInFirstMsec) / fadeInLength);
        }
        else if (___AppearMsec <= currentMsec && currentMsec < fadeInSecondMsec)
        {
            color.a = 0.3f;
        }
        else if (fadeInSecondMsec <= currentMsec && currentMsec < ___StarLaunchMsec)
        {
            var fadeInLength = Math.Min(200.0f, ___StarLaunchMsec - fadeInSecondMsec);
            // var fadeInLength = ___StarLaunchMsec - fadeInSecondMsec;
            color.a = 0.3f + 0.3f * Math.Min(1f, (currentMsec - fadeInSecondMsec) / fadeInLength);
        }
        else
        {
            color.a = 0.6f;
        }

        foreach (SpriteRenderer spriteLine in ____spriteLines)
            spriteLine.color = color;

        return false;
    }
    
}

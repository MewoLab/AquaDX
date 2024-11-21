using System;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;
using Monitor;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: """
        Make the judgment display of circular Slides align precisely with the judgment line (originally a bit off)
        Just like in majdata
        """,
    zh: """
        让圆弧形的 Slide 的判定显示与判定线精确对齐 (原本会有一点歪)
        就像 majdata 里那样
        """)]
public class AlignCircleSlideJudgeDisplay
{
    /*
     * 这个 Patch 让圆弧形的 Slide 的判定显示与判定线精确对齐 (原本会有一点歪), 就像 majdata 里那样
     */
    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideRoot), "Initialize")]
    private static void FixJudgePosition(
        SlideRoot __instance, SlideType ___EndSlideType, SlideJudge ___JudgeObj
    )
    {
        if (null != ___JudgeObj)
        {
            float z = ___JudgeObj.transform.localPosition.z;
            if (___EndSlideType == SlideType.Slide_Circle_L)
            {
                float angle = -45.0f - 45.0f * __instance.EndButtonId;
                double angleRad = Math.PI / 180.0 * (angle + 90 + 22.5 + 2.6415);
                ___JudgeObj.transform.localPosition = new Vector3(480f * (float)Math.Cos(angleRad), 480f * (float)Math.Sin(angleRad), z);
                ___JudgeObj.transform.localRotation = Quaternion.Euler(0.0f, 0.0f, angle);
            }
            else if (___EndSlideType == SlideType.Slide_Circle_R)
            {
                float angle = -45.0f * __instance.EndButtonId;
                double angleRad = Math.PI / 180.0 * (angle + 90 - 22.5 - 2.6415);
                ___JudgeObj.transform.localPosition = new Vector3(480f * (float)Math.Cos(angleRad), 480f * (float)Math.Sin(angleRad), z);
                ___JudgeObj.transform.localRotation = Quaternion.Euler(0.0f, 0.0f, angle);
            }
        }
    }


}

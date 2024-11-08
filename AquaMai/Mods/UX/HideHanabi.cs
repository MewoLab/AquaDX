using AquaMai.Attributes;
using Fx;
using HarmonyLib;
using Monitor;
using UnityEngine;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "Cannot be used together with HanabiFix",
    zh: """
        完全隐藏烟花
        不能和 HanabiFix 一起使用
        """)]
public class HideHanabi
{
    [HarmonyPatch(typeof(TapCEffect), "SetUpParticle")]
    [HarmonyPostfix]
    public static void FixZeroSize(TapCEffect __instance, FX_Mai2_Note_Color ____particleControler)
    {
        var entities = ____particleControler.GetComponentsInChildren<ParticleSystemRenderer>(true);
        foreach (var entity in entities)
        {
            entity.maxParticleSize = 0f;
        }
    }
}

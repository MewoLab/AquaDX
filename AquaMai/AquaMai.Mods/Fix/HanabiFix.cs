using AquaMai.Config.Attributes;
using Fx;
using HarmonyLib;
using Monitor;
using UnityEngine;

namespace AquaMai.Mods.Fix;

[ConfigSection(
    en: """
        Fix the size of hanabi in 1p mode
        Cannot be used together with HideHanabi
        """,
    zh: """
        修复 1p 模式下的烟花大小
        不能和 HideHanabi 一起使用
        """)]
public class HanabiFix
{
    [HarmonyPatch(typeof(TapCEffect), "SetUpParticle")]
    [HarmonyPostfix]
    public static void FixMaxSize(TapCEffect __instance, FX_Mai2_Note_Color ____particleControler)
    {
        var entities = ____particleControler.GetComponentsInChildren<ParticleSystemRenderer>(true);
        foreach (var entity in entities)
        {
            entity.maxParticleSize = 1f;
        }
    }
}

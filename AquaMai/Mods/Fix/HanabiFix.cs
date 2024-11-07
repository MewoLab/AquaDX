using Fx;
using HarmonyLib;
using Monitor;
using UnityEngine;

namespace AquaMai.Mods.Fix;

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

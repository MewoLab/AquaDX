using HarmonyLib;

namespace AquaMai.Mods.Fix;

public class ForceFreePlay
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(Manager.Credit), "IsFreePlay")]
    private static bool PreIsFreePlay(ref bool __result)
    {
        __result = true;
        return false;
    }
}

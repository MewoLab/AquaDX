using HarmonyLib;

namespace AquaMai.Mods.Fix;

public class ForcePaidPlay
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(Manager.Credit), "IsFreePlay")]
    private static bool PreIsFreePlay(ref bool __result)
    {
        __result = false;
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(Manager.Credit), "IsGameCostEnough")]
    private static bool PreIsGameCostEnough(ref bool __result)
    {
        __result = true;
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(AMDaemon.CreditUnit), "Credit", MethodType.Getter)]
    private static bool PreCredit(ref uint __result)
    {
        __result = 24;
        return false;
    }
}

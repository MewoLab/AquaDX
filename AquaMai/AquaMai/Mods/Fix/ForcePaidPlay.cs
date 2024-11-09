using AquaMai.Config.Attributes;
using HarmonyLib;

namespace AquaMai.Mods.Fix;

[ConfigSection(
    en: "Force the game to be in PaidPlay mode with 24 coins locked, conflicts with ForceFreePlay",
    zh: "强制付费游玩并锁定 24 个币，和 ForceFreePlay 冲突")]
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

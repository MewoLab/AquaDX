using AquaMai.Config.Attributes;
using AquaMai.Core.Attributes;
using HarmonyLib;

namespace AquaMai.Mods.GameSettings;

[ConfigSection(
    en: "Set the game to Paid Play (lock credits) or Free Play.",
    zh: "设置游戏为付费游玩（锁定可用点数）或免费游玩")]
public class CreditConfig
{
    [ConfigEntry(
        en: "Set to Free Play (set to false for Paid Play).",
        zh: "是否免费游玩（设为 false 时为付费游玩）")]
    private static readonly bool isFreePlay = true;

    [HarmonyPrefix]
    [HarmonyPatch(typeof(Manager.Credit), "IsFreePlay")]
    private static bool PreIsFreePlay(ref bool __result)
    {
        __result = isFreePlay;
        return false;
    }

    [ConfigEntry(
        en: "Lock credits amount (only valid in Paid Play). Set to 0 to disable.",
        zh: "锁定可用点数数量（仅在付费游玩时有效），设为 0 以禁用")]
    private static readonly uint lockCredits = 24;

    private static bool ShouldLockCredits => !isFreePlay && lockCredits > 0;

    [EnableIf(nameof(ShouldLockCredits))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(Manager.Credit), "IsGameCostEnough")]
    private static bool PreIsGameCostEnough(ref bool __result)
    {
        __result = true;
        return false;
    }

    [EnableIf(nameof(ShouldLockCredits))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(AMDaemon.CreditUnit), "Credit", MethodType.Getter)]
    private static bool PreCredit(ref uint __result)
    {
        __result = 24;
        return false;
    }
}

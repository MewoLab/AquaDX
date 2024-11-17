using AquaMai.Core.Attributes;
using AquaMai.Config.Attributes;
using HarmonyLib;
using MAI2System;
using Manager;

namespace AquaMai.Mods.Cheat;

[ConfigSection(
    en: "Unlock Utage without the need of DXRating 10000",
    zh: "不需要万分也可以进宴会场")]
[EnableGameVersion(24000)]
public class UnlockUtage
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(GameManager), "CanUnlockUtageTotalJudgement")]
    public static bool CanUnlockUtageTotalJudgement(out ConstParameter.ResultOfUnlockUtageJudgement result1P, out ConstParameter.ResultOfUnlockUtageJudgement result2P)
    {
        result1P = ConstParameter.ResultOfUnlockUtageJudgement.Unlocked;
        result2P = ConstParameter.ResultOfUnlockUtageJudgement.Unlocked;
        return false;
    }
}

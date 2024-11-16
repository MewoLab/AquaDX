using AquaMai.Config.Attributes;
using HarmonyLib;

namespace AquaMai.Mods.Fix;

[ConfigSection(
    en: "Force the game to be in FreePlay mode",
    zh: "强制改为免费游玩（FreePlay）")]
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

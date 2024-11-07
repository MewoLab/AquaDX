using AquaMai.Attributes;
using HarmonyLib;
using Manager;
using Monitor;

namespace AquaMai.Mods.ModKeyMap;

[GameVersion(23000)]
public class EnableNativeQuickRetry
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(QuickRetry), "IsQuickRetryEnable")]
    public static bool OnQuickRetryIsQuickRetryEnable(ref bool __result)
    {
        var isUtageProperty = Traverse.Create(typeof(GameManager)).Property("IsUtage");
        __result = !isUtageProperty.PropertyExists() || !isUtageProperty.GetValue<bool>();
        return false;
    }
}

using AMDaemon;
using HarmonyLib;

namespace AquaMai.Mods.Fix;

public class ForceAsServer
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(LanInstall), "IsServer", MethodType.Getter)]
    private static bool PreIsServer(ref bool __result)
    {
        __result = true;
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(Network), "IsLanAvailable", MethodType.Getter)]
    private static bool PreIsLanAvailable(ref bool __result)
    {
        __result = false;
        return false;
    }
}

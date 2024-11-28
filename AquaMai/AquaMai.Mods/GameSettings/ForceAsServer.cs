using AMDaemon;
using AquaMai.Config.Attributes;
using HarmonyLib;

namespace AquaMai.Mods.GameSettings;

[ConfigSection(
    en: "If you want to configure in-shop party-link, you should turn this off.",
    zh: "如果要配置店内招募的话，应该要把这个关闭",
    defaultOn: true)]
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

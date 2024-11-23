using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;

namespace AquaMai.Mods.Tweaks.TimeSaving;

[ConfigSection(
    en: "Skip SDEZ's warning screen and logo shown after the POST sequence",
    zh: "跳过 SDEZ 启动时的 WARNING 界面")]
public class SkipStartupWarning
{
    /*
     * Patch PlayLogo to disable the warning screen
     */
    [HarmonyPrefix]
    [HarmonyPatch(typeof (WarningMonitor), "PlayLogo")]
    public static bool PlayLogo()
    {
        // Return false to block the original method
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof (WarningMonitor), "IsLogoAnimationEnd")]
    public static bool IsLogoAnimationEnd(ref bool __result)
    {
        // Always return true to indicate the animation has ended
        __result = true;
        return false;
    }
}

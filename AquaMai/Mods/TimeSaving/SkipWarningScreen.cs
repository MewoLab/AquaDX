using AquaMai.Attributes;
using HarmonyLib;
using Monitor;

namespace AquaMai.Mods.TimeSaving;

[ConfigSection(
    en: "Skip the warning screen and logo shown after the POST sequence",
    zh: "跳过日服启动时候的 WARNING 界面")]
public class SkipWarningScreen
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

using AquaMai.Attributes;
using AquaMai.Helpers;
using AquaMai.Resources;
using HarmonyLib;
using Process;

namespace AquaMai.Mods.UX;

[ConfigSection(defaultOn: true, hideInExample: true)]
public class CiBuildAlert
{
    [HarmonyPatch(typeof(AdvertiseProcess), "OnStart")]
    [HarmonyPostfix]
    public static void OnStart(AdvertiseProcess __instance)
    {
        MessageHelper.ShowMessage(Locale.CiBuildAlertContent, title: Locale.CiBuildAlertTitle);
    }
}

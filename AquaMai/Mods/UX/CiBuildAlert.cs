using AquaMai.Helpers;
using AquaMai.Resources;
using HarmonyLib;
using Process;

namespace AquaMai.Mods.UX;

public class CiBuildAlert
{
    [HarmonyPatch(typeof(AdvertiseProcess), "OnStart")]
    [HarmonyPostfix]
    public static void OnStart(AdvertiseProcess __instance)
    {
        MessageHelper.ShowMessage(Locale.CiBuildAlertContent, title: Locale.CiBuildAlertTitle);
    }
}

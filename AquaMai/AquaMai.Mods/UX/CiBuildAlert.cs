using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;
using AquaMai.Core.Resources;
using HarmonyLib;
using Process;

namespace AquaMai.Mods.UX;

[ConfigSection(example: ConfigSectionExample.HiddenDefaultOn)]
public class CiBuildAlert
{
    [HarmonyPatch(typeof(AdvertiseProcess), "OnStart")]
    [HarmonyPostfix]
    public static void OnStart(AdvertiseProcess __instance)
    {
# if CI
        MessageHelper.ShowMessage(Locale.CiBuildAlertContent, title: Locale.CiBuildAlertTitle);
# endif
    }
}

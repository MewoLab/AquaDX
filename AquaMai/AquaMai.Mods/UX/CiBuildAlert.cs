using AquaMai.Config.Attributes;
using AquaMai.Core.Attributes;
using AquaMai.Core.Helpers;
using AquaMai.Core.Resources;
using HarmonyLib;
using Process;

namespace AquaMai.Mods.UX;

[ConfigSection(exampleHidden: true, defaultOn: true)]
[EnableIf(nameof(isCiBuild))]
public class CiBuildAlert
{
# if CI
    private static readonly bool isCiBuild = true;
# else
    private static readonly bool isCiBuild = false;
# endif

    [HarmonyPatch(typeof(AdvertiseProcess), "OnStart")]
    [HarmonyPostfix]
    public static void OnStart(AdvertiseProcess __instance)
    {
        MessageHelper.ShowMessage(Locale.CiBuildAlertContent, title: Locale.CiBuildAlertTitle);
    }
}

using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;

namespace AquaMai.Mods.Tweaks.TimeSaving;

[ConfigSection(
    en: "Skip the \"Do not tap or slide vigorously\" screen, immediately proceed to the next screen once data is loaded",
    zh: "跳过“不要大力拍打或滑动哦”这个界面，数据一旦加载完就立马进入下一个界面")]
public class IWontTapOrSlideVigorously
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(PlInformationMonitor), "IsPlayPlInfoEnd")]
    public static bool Patch(ref bool __result)
    {
        __result = true;
        return false;
    }
}

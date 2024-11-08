using AquaMai.Attributes;
using HarmonyLib;
using Monitor;

namespace AquaMai.Mods.TimeSaving;

[ConfigSection(
    en: "Skip TrackStart screen",
    zh: "跳过乐曲开始界面")]
public class SkipTrackStart
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof (TrackStartMonitor), "IsEnd")]
    public static bool IsEnd(ref bool __result)
    {
        __result = true;
        return false;
    }
}

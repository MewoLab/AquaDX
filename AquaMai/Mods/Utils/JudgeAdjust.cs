using System.Threading;
using AquaMai.Attributes;
using HarmonyLib;
using IO;
using Manager.UserDatas;

namespace AquaMai.Mods.Utils;

[ConfigSection(
    en: "Globally adjust A/B judgment (unit same as in-game options) or increase touch delay",
    zh: "全局调整 A/B 判（单位和游戏里一样）或增加触摸延迟")]
public class JudgeAdjust
{
    [ConfigEntry(
        en: "Adjust A judgment",
        zh: "调整 A 判")]
    private static readonly float A = 0;

    [ConfigEntry(
        en: "Adjust B judgment",
        zh: "调整 B 判")]
    private static readonly float B = 0;

    [ConfigEntry(
        en: "Increase touch delay",
        zh: "增加触摸延迟")]
    private static readonly int TouchDelay = 0;

    [HarmonyPostfix]
    [HarmonyPatch(typeof(UserOption), "GetAdjustMSec")]
    public static void GetAdjustMSec(ref float __result)
    {
        __result += A * 16.666666f;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(UserOption), "GetJudgeTimingFrame")]
    public static void GetJudgeTimingFrame(ref float __result)
    {
        __result += B;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(NewTouchPanel), "Recv")]
    public static void NewTouchPanelRecv()
    {
        if (TouchDelay <= 0) return;
        Thread.Sleep(TouchDelay);
    }
}

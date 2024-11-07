using System.Threading;
using HarmonyLib;
using IO;
using Manager.UserDatas;

namespace AquaMai.Mods.Utils;

public class JudgeAdjust
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(UserOption), "GetAdjustMSec")]
    public static void GetAdjustMSec(ref float __result)
    {
        __result += AquaMai.AppConfig.Utils.JudgeAdjustA * 16.666666f;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(UserOption), "GetJudgeTimingFrame")]
    public static void GetJudgeTimingFrame(ref float __result)
    {
        __result += AquaMai.AppConfig.Utils.JudgeAdjustB;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(NewTouchPanel), "Recv")]
    public static void NewTouchPanelRecv()
    {
        if (AquaMai.AppConfig.Utils.TouchDelay <= 0) return;
        Thread.Sleep(AquaMai.AppConfig.Utils.TouchDelay);
    }
}

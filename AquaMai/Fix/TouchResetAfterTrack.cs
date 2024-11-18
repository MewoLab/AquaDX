using HarmonyLib;
using MAI2.Util;
using Manager;
using Process;

namespace AquaMai.Fix;

public class TouchResetAfterTrack
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultProcess), "OnStart")]
    public static void ResultProcessOnStart()
    {
        SingletonStateMachine<AmManager, AmManager.EState>.Instance.StartTouchPanel();
        MelonLoader.MelonLogger.Msg("[TouchResetAfterTrack] Touch panel reset");
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GamePlayManager), "SetQuickRetryFrag")]
    public static void OnStart()
    {
        SingletonStateMachine<AmManager, AmManager.EState>.Instance.StartTouchPanel();
        MelonLoader.MelonLogger.Msg("[TouchResetAfterTrack] Touch panel reset");
    }
}

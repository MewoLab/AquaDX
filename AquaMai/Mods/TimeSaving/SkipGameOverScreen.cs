using HarmonyLib;
using Monitor;
using Process;

namespace AquaMai.Mods.TimeSaving;

public class SkipGameOverScreen
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(GameOverMonitor), "IsPlayEnd")]
    public static bool GameOverMonitorPlayEnd(ref bool __result)
    {
        __result = true;
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(GameOverProcess), "OnUpdate")]
    public static void GameOverProcessOnUpdate(ref GameOverProcess.GameOverSequence ____state)
    {
        if (____state == GameOverProcess.GameOverSequence.SkyChange)
        {
            ____state = GameOverProcess.GameOverSequence.Disp;
        }
    }
}

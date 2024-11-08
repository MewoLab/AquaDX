using AquaMai.Attributes;
using HarmonyLib;
using Monitor;
using Process;

namespace AquaMai.Mods.TimeSaving;

[ConfigSection(
    en: "Skip the \"Goodbye\" screen at the end of the game",
    zh: "跳过游戏结束的「再见」界面")]
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

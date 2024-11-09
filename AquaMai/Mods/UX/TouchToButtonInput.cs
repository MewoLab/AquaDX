using AquaMai.Config.Attributes;
using HarmonyLib;
using Process;
using static Manager.InputManager;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "Map touch actions to buttons",
    zh: "映射触摸操作至实体按键")]
public class TouchToButtonInput
{
    private static bool _isPlaying = false;

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameProcess), "OnStart")]
    public static void OnGameProcessStart(GameProcess __instance)
    {
        _isPlaying = true;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameProcess), "OnRelease")]
    public static void OnGameProcessRelease(GameProcess __instance)
    {
        _isPlaying = false;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(Manager.InputManager), "GetButtonDown")]
    public static void GetButtonDown(ref bool __result, int monitorId, ButtonSetting button)
    {
        if (_isPlaying || __result) return;
        if (button.ToString().StartsWith("Button"))
        {
            __result = GetTouchPanelAreaDown(monitorId, (TouchPanelArea)button);
        }
        else if (button.ToString().Equals("Select"))
        {
            __result = GetTouchPanelAreaLongPush(monitorId, TouchPanelArea.C1, 500L) || GetTouchPanelAreaLongPush(monitorId, TouchPanelArea.C2, 500L);
        }
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(Manager.InputManager), "GetButtonPush")]
    public static void GetButtonPush(ref bool __result, int monitorId, ButtonSetting button)
    {
        if (_isPlaying || __result) return;
        if (button.ToString().StartsWith("Button")) __result = GetTouchPanelAreaPush(monitorId, (TouchPanelArea)button);
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(Manager.InputManager), "GetButtonLongPush")]
    public static void GetButtonLongPush(ref bool __result, int monitorId, ButtonSetting button, long msec)
    {
        if (_isPlaying || __result) return;
        if (button.ToString().StartsWith("Button")) __result = GetTouchPanelAreaLongPush(monitorId, (TouchPanelArea)button, msec);
    }
}

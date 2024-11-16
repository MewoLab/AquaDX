using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;
using AquaMai.Core.Resources;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Monitor;
using Process;
using UnityEngine;

namespace AquaMai.Mods.TimeSaving;

[ConfigSection(
    en: "Show a \"skip\" button like AstroDX after the notes end",
    zh: "音符结束之后显示像 AstroDX 一样的「跳过」按钮")]
public class ShowQuickEndPlay
{
    private static bool _showUi;

    [HarmonyPatch(typeof(GameProcess), "OnStart")]
    [HarmonyPostfix]
    public static void GameProcessPostStart(GameMonitor[] ____monitors)
    {
        _showUi = false;
        ____monitors[0].gameObject.AddComponent<Ui>();
    }

    [HarmonyPatch(typeof(GameProcess), "OnUpdate")]
    [HarmonyPostfix]
    public static void GameProcessPostUpdate(GameProcess __instance, Message[] ____message, ProcessDataContainer ___container, byte ____sequence)
    {
        _showUi = ____sequence switch
        {
            9 => false,
            > 4 => true,
            _ => false
        };

        if (_showUi && (InputManager.GetTouchPanelAreaDown(InputManager.TouchPanelArea.B4) || InputManager.GetTouchPanelAreaDown(InputManager.TouchPanelArea.E4)))
        {
            var traverse = Traverse.Create(__instance);
            ___container.processManager.SendMessage(____message[0]);
            Singleton<GamePlayManager>.Instance.SetSyncResult(0);
            traverse.Method("SetRelease").GetValue();
        }
    }

    private class Ui : MonoBehaviour
    {
        public void OnGUI()
        {
            if (!_showUi) return;

            // 这里重新 setup 一下 style 也可以
            var x = GuiSizes.PlayerCenter;
            var y = Screen.height - GuiSizes.PlayerWidth * .37f;
            var width = GuiSizes.PlayerWidth * .25f;
            var height = GuiSizes.PlayerWidth * .13f;

            GUI.Box(new Rect(x, y, width, height), "");
            GUI.Button(new Rect(x, y, width, height), Locale.Skip);
        }
    }
}

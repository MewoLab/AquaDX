using AquaMai.Config.Attributes;
using AquaMai.Config.Types;
using AquaMai.Core.Helpers;
using AquaMai.Core.Resources;
using HarmonyLib;
using MAI2.Util;
using Main;
using Manager;
using Process;

namespace AquaMai.Mods.Tweaks;

[ConfigSection(
    en: "Reset touch panel manually or after playing track.",
    zh: "重置触摸面板")]
public class ResetTouch
{
    [ConfigEntry(en: "Reset touch panel after playing track.", zh: "玩完一首歌自动重置")]
    private static bool afterTrack = false;

    [ConfigEntry(en: "Reset manually.", zh: "按键重置")]
    public static readonly KeyCodeOrName key = KeyCodeOrName.None;

    [ConfigEntry] private static readonly bool longPress = false;

    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultProcess), "OnStart")]
    public static void ResultProcessOnStart()
    {
        if (!afterTrack) return;
        SingletonStateMachine<AmManager, AmManager.EState>.Instance.StartTouchPanel();
        MelonLoader.MelonLogger.Msg("[TouchResetAfterTrack] Touch panel reset");
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameMainObject), "Update")]
    public static void OnGameMainObjectUpdate()
    {
        if (!KeyListener.GetKeyDownOrLongPress(key, longPress)) return;
        SingletonStateMachine<AmManager, AmManager.EState>.Instance.StartTouchPanel();
        MessageHelper.ShowMessage(Locale.TouchPanelReset);
    }
}
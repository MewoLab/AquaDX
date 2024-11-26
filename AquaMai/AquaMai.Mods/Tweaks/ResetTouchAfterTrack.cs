﻿using AquaMai.Config.Attributes;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Process;

namespace AquaMai.Mods.Tweaks;

[ConfigSection(
    en: "Reset touch panel after playing track.",
    zh: "在游玩一首曲目后重置触摸面板")]
public class ResetTouchAfterTrack
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultProcess), "OnStart")]
    public static void ResultProcessOnStart()
    {
        SingletonStateMachine<AmManager, AmManager.EState>.Instance.StartTouchPanel();
        MelonLoader.MelonLogger.Msg("[TouchResetAfterTrack] Touch panel reset");
    }
}

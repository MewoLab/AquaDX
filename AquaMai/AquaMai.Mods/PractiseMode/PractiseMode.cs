using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using AquaMai.Mods.Fix;
using AquaMai.Core.Helpers;
using AquaMai.Core.Resources;
using AquaMai.Mods.PracticeMode.Libs;
using HarmonyLib;
using Manager;
using Monitor;
using Monitor.Game;
using Process;
using UnityEngine;
using AquaMai.Config.Attributes;
using AquaMai.Config.Types;

namespace AquaMai.Mods.PracticeMode;

[ConfigCollapseNamespace]
[ConfigSection(
    en: "Practice Mode",
    zh: "练习模式")]
public class PracticeMode
{
    [ConfigEntry(
        en: "Key to show Practice Mode UI",
        zh: "显示练习模式 UI 的按键")]
    public static readonly KeyCodeOrName Key = KeyCodeOrName.Test;

    [ConfigEntry]
    public static readonly bool LongPress = false;

    public static double repeatStart = -1;
    public static double repeatEnd = -1;
    public static float speed = 1;
    private static CriAtomExPlayer player;
    private static MovieMaterialMai2 movie;
    public static GameCtrl gameCtrl;
    public static bool keepNoteSpeed = false;

    public static void SetRepeatEnd(double time)
    {
        if (repeatStart == -1)
        {
            MessageHelper.ShowMessage(Locale.RepeatStartTimeNotSet);
            return;
        }

        if (time < repeatStart)
        {
            MessageHelper.ShowMessage(Locale.RepeatEndTimeLessThenStartTime);
            return;
        }

        repeatEnd = time;
    }

    public static void ClearRepeat()
    {
        repeatStart = -1;
        repeatEnd = -1;
    }

    public static void SetSpeed()
    {
        player.SetPitch((float)(1200 * Math.Log(speed, 2)));
        // player.SetDspTimeStretchRatio(1 / speed);
        player.UpdateAll();

        movie.player.SetSpeed(speed);
        gameCtrl?.ResetOptionSpeed();
    }

    private static IEnumerator SetSpeedCoroutineInner()
    {
        yield return null;
        SetSpeed();
    }

    public static void SetSpeedCoroutine()
    {
        SharedInstances.GameMainObject.StartCoroutine(SetSpeedCoroutineInner());
    }

    public static void SpeedUp()
    {
        speed += .05f;
        if (speed > 2)
        {
            speed = 2;
        }

        SetSpeed();
    }

    public static void SpeedDown()
    {
        speed -= .05f;
        if (speed < 0.05)
        {
            speed = 0.05f;
        }

        SetSpeed();
    }

    public static void SpeedReset()
    {
        speed = 1;
        SetSpeed();
    }

    public static void Seek(int addMsec)
    {
        // Debug feature 里面那个 timer 不能感知变速
        // 为了和魔改版本统一，polyfill 里面不修这个
        // 这里重新实现一个能感知变速的 Seek
        var msec = CurrentPlayMsec + addMsec;
        if (msec < 0)
        {
            msec = 0;
        }

        CurrentPlayMsec = msec;
    }

    public static double CurrentPlayMsec
    {
        get => NotesManager.GetCurrentMsec() - 91;
        set
        {
            DebugFeature.CurrentPlayMsec = value;
            SetSpeedCoroutine();
        }
    }

    public static PracticeModeUI ui;

    [HarmonyPatch]
    public class PatchNoteSpeed
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            yield return AccessTools.Method(typeof(GameManager), "GetNoteSpeed");
            yield return AccessTools.Method(typeof(GameManager), "GetTouchSpeed");
        }

        public static void Postfix(ref float __result)
        {
            if (!keepNoteSpeed) return;
            __result /= speed;
        }
    }

    [HarmonyPatch(typeof(GameProcess), "OnStart")]
    [HarmonyPostfix]
    public static void GameProcessPostStart()
    {
        repeatStart = -1;
        repeatEnd = -1;
        speed = 1;
        ui = null;
    }

    [HarmonyPatch(typeof(GameProcess), "OnRelease")]
    [HarmonyPostfix]
    public static void GameProcessPostRelease()
    {
        repeatStart = -1;
        repeatEnd = -1;
        speed = 1;
        ui = null;
    }

    [HarmonyPatch(typeof(GameCtrl), "Initialize")]
    [HarmonyPostfix]
    public static void GameCtrlPostInitialize(GameCtrl __instance)
    {
        gameCtrl = __instance;
    }

# if DEBUG
    [HarmonyPrefix]
    [HarmonyPatch(typeof(GenericProcess), "OnUpdate")]
    public static void OnGenericProcessUpdate(GenericMonitor[] ____monitors)
    {
        if (Input.GetKeyDown(KeyCode.F11))
        {
            ____monitors[0].gameObject.AddComponent<PracticeModeUI>();
        }
    }
# endif

    [HarmonyPatch(typeof(GameProcess), "OnUpdate")]
    [HarmonyPostfix]
    public static void GameProcessPostUpdate(GameProcess __instance, GameMonitor[] ____monitors)
    {
        if (KeyListener.GetKeyDownOrLongPress(Key, LongPress) && ui is null)
        {
            ui = ____monitors[0].gameObject.AddComponent<PracticeModeUI>();
        }

        if (repeatStart >= 0 && repeatEnd >= 0)
        {
            if (CurrentPlayMsec >= repeatEnd)
            {
                CurrentPlayMsec = repeatStart;
            }
        }
    }

    private static float startGap = -1f;

    [HarmonyPatch(typeof(NotesManager), "StartPlay")]
    [HarmonyPostfix]
    public static void NotesManagerPostUpdateTimer(float msecStartGap)
    {
        startGap = msecStartGap;
    }

    [HarmonyPatch(typeof(NotesManager), "UpdateTimer")]
    [HarmonyPrefix]
    public static bool NotesManagerPostUpdateTimer(bool ____isPlaying, Stopwatch ____stopwatch, ref float ____curMSec, ref float ____curMSecPre, float ____msecStartGap)
    {
        var stackTrace = new StackTrace(); // get call stack
        var stackFrames = stackTrace.GetFrames(); // get method calls (frames)
        if(stackFrames.Select(it => it.GetMethod().DeclaringType.Name).Contains("AdvDemoProcess"))
        {
            return true;
        }

        if (startGap != -1f)
        {
            ____curMSec = startGap;
            ____curMSecPre = startGap;
            ____stopwatch?.Reset();
            startGap = -1f;
        }
        else
        {
            ____curMSecPre = ____curMSec;
            if (____isPlaying && ____stopwatch != null && !DebugFeature.Pause)
            {
                var num = (double)____stopwatch.ElapsedTicks / Stopwatch.Frequency * 1000.0 * speed;
                ____curMSec += (float)num;
                ____stopwatch.Reset();
                ____stopwatch.Start();
            }
        }

        return false;
    }

    [HarmonyPatch(typeof(SoundCtrl), "Initialize")]
    [HarmonyPostfix]
    public static void SoundCtrlPostInitialize(SoundCtrl.InitParam param, Dictionary<int, object> ____players)
    {
        var wrapper = ____players[2];
        player = (CriAtomExPlayer)wrapper.GetType().GetField("Player").GetValue(wrapper);
        // var pool = new CriAtomExStandardVoicePool(1, 8, 96000, true, 2);
        // pool.AttachDspTimeStretch();
        // player.SetVoicePoolIdentifier(pool.identifier);

        // debug
        // var wrapper1 = ____players[7];
        // var player1 = (CriAtomExPlayer)wrapper1.GetType().GetField("Player").GetValue(wrapper1);
        // var pool = new CriAtomExStandardVoicePool(1, 8, 96000, true, 2);
        // pool.AttachDspTimeStretch();
        // player1.SetVoicePoolIdentifier(pool.identifier);
        // player1.SetDspTimeStretchRatio(2);
    }

    [HarmonyPatch(typeof(MovieController), "Awake")]
    [HarmonyPostfix]
    public static void MovieControllerPostAwake(MovieMaterialMai2 ____moviePlayers)
    {
        movie = ____moviePlayers;
    }
}

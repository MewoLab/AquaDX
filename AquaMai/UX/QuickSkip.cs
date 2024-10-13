﻿using System.Collections.Generic;
using AquaMai.Helpers;
using HarmonyLib;
using Mai2.Mai2Cue;
using MAI2.Util;
using Main;
using Manager;
using MelonLoader;
using Monitor;
using Process;
using UnityEngine;

namespace AquaMai.UX
{
    public class QuickSkip
    {
        private static int _keyPressFrames;

        [HarmonyPrefix]
        [HarmonyPatch(typeof(GameMainObject), "Update")]
        public static void OnGameMainObjectUpdate()
        {
            // The button between [1p] and [2p] button on ADX
            if (Input.GetKey(KeyCode.Alpha7) || InputManager.GetSystemInputPush(InputManager.SystemButtonSetting.ButtonService)) _keyPressFrames++;

            if (_keyPressFrames > 0 && !Input.GetKey(KeyCode.Alpha7) && !InputManager.GetSystemInputPush(InputManager.SystemButtonSetting.ButtonService))
            {
                _keyPressFrames = 0;
                MelonLogger.Msg(SharedInstances.ProcessDataContainer.processManager.Dump());
                return;
            }

            if (_keyPressFrames != 60) return;
            MelonLogger.Msg("[QuickSkip] Activated");

            var traverse = Traverse.Create(SharedInstances.ProcessDataContainer.processManager);
            var processList = traverse.Field("_processList").GetValue<LinkedList<ProcessManager.ProcessControle>>();

            ProcessBase processToRelease = null;

            foreach (ProcessManager.ProcessControle process in processList)
            {
                switch (process.Process.ToString())
                {
                    // After login
                    case "Process.ModeSelect.ModeSelectProcess":
                    case "Process.LoginBonus.LoginBonusProcess":
                    case "Process.RegionalSelectProcess":
                    case "Process.CharacterSelectProcess":
                    case "Process.TicketSelect.TicketSelectProcess":
                        processToRelease = process.Process;
                        break;

                    case "Process.MusicSelectProcess":
                        // Skip to save
                        SoundManager.PreviewEnd();
                        SoundManager.PlayBGM(Cue.BGM_COLLECTION, 2);
                        SharedInstances.ProcessDataContainer.processManager.AddProcess(new FadeProcess(SharedInstances.ProcessDataContainer, process.Process, new UnlockMusicProcess(SharedInstances.ProcessDataContainer)));
                        break;
                }
            }

            if (processToRelease != null)
            {
                GameManager.SetMaxTrack();
                SharedInstances.ProcessDataContainer.processManager.AddProcess(new FadeProcess(SharedInstances.ProcessDataContainer, processToRelease, new MusicSelectProcess(SharedInstances.ProcessDataContainer)));
            }
        }

        [HarmonyPostfix]
        [HarmonyPatch(typeof(GameProcess), "OnUpdate")]
        public static void PostGameProcessUpdate(GameProcess __instance, Message[] ____message, ProcessDataContainer ___container)
        {
            if (InputManager.GetButtonDown(0, InputManager.ButtonSetting.Select))
            {
                var traverse = Traverse.Create(__instance);
                ___container.processManager.SendMessage(____message[0]);
                Singleton<GamePlayManager>.Instance.SetSyncResult(0);
                traverse.Method("SetRelease").GetValue();
            }

            if (Input.GetKey(KeyCode.Alpha7) || InputManager.GetSystemInputPush(InputManager.SystemButtonSetting.ButtonService))
            {
                // This is original typo in Assembly-CSharp
                Singleton<GamePlayManager>.Instance.SetQuickRetryFrag(flag: true);
            }
        }

        [HarmonyPrefix]
        [HarmonyPatch(typeof(QuickRetry), "IsQuickRetryEnable")]
        public static bool OnQuickRetryIsQuickRetryEnable(ref bool __result)
        {
            var isUtageProperty = Traverse.Create(typeof(GameManager)).Property("IsUtage");
            __result = !isUtageProperty.PropertyExists() || !isUtageProperty.GetValue<bool>();
            return false;
        }

        // Fix for the game not resetting Fast and Late counts when quick retrying
        // For game version < 1.35.0
        [HarmonyPostfix]
        [HarmonyPatch(typeof(GamePlayManager), "SetQuickRetryFrag")]
        public static void PostGamePlayManagerSetQuickRetryFrag(GamePlayManager __instance, bool flag)
        {
            // Since 1.35.0, `GameScoreList.Initialize()` resets the Fast and Late counts
            if (flag && !Traverse.Create(typeof(GameScoreList)).Methods().Contains("Initialize"))
            {
                for (int i = 0; i < 4; i++)
                {
                    var gameScoreList = __instance.GetGameScore(i);
                    var traverse = Traverse.Create(gameScoreList);
                    traverse.Property("Fast").SetValue((uint)0);
                    traverse.Property("Late").SetValue((uint)0);
                }
            }
        }
    }
}

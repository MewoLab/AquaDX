using System.Collections.Generic;
using AquaMai.Helpers;
using HarmonyLib;
using Mai2.Mai2Cue;
using MAI2.Util;
using Main;
using Manager;
using MelonLoader;
using Process;

namespace AquaMai.Mods.ModKeyMap;

public class QuickSkip
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(GameMainObject), "Update")]
    public static void OnGameMainObjectUpdate()
    {
        if (!ModKeyListener.GetKeyDownOrLongPress(AquaMai.AppConfig.ModKeyMap.QuickSkip, AquaMai.AppConfig.ModKeyMap.QuickSkipLongPress)) return;
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
        if (ModKeyListener.GetKeyDownOrLongPress(AquaMai.AppConfig.ModKeyMap.InGameSkip, AquaMai.AppConfig.ModKeyMap.InGameSkipLongPress))
        {
            var traverse = Traverse.Create(__instance);
            ___container.processManager.SendMessage(____message[0]);
            Singleton<GamePlayManager>.Instance.SetSyncResult(0);
            traverse.Method("SetRelease").GetValue();
        }

        if (ModKeyListener.GetKeyDownOrLongPress(AquaMai.AppConfig.ModKeyMap.InGameRetry, AquaMai.AppConfig.ModKeyMap.InGameRetryLongPress) && GameInfo.GameVersion >= 23000)
        {
            // This is original typo in Assembly-CSharp
            Singleton<GamePlayManager>.Instance.SetQuickRetryFrag(flag: true);
        }
    }
}

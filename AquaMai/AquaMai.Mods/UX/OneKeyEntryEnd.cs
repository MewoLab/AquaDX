using System.Collections.Generic;
using AquaMai.Config.Attributes;
using AquaMai.Config.Types;
using AquaMai.Core.Helpers;
using HarmonyLib;
using Mai2.Mai2Cue;
using Main;
using Manager;
using MelonLoader;
using Process;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "One key to proceed to music select (during entry) or end current PC (during music select)",
    zh: "一键跳过登录过程直接进入选歌界面，或在选歌界面直接结束本局游戏")]
public class OneKeyEntryEnd
{
    [ConfigEntry]
    public static readonly KeyCodeOrName key = KeyCodeOrName.Service;

    [ConfigEntry]
    public static readonly bool longPress = true;

    [HarmonyPrefix]
    [HarmonyPatch(typeof(GameMainObject), "Update")]
    public static void OnGameMainObjectUpdate()
    {
        if (!KeyListener.GetKeyDownOrLongPress(key, longPress)) return;
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
}

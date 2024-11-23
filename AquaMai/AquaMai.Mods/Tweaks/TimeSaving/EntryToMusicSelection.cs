using AquaMai.Config.Attributes;
using DB;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Monitor;
using Process;
using Process.Information;

namespace AquaMai.Mods.Tweaks.TimeSaving;

[ConfigSection(
    en: "Directly enter the song selection screen after login",
    zh: "登录完成后直接进入选歌界面")]
public class EntryToMusicSelection
{
    /*
     * Highly experimental, may well break some stuff
     * Works by overriding the info screen (where it shows new events and stuff)
     * to directly exit to the music selection screen, skipping character and
     * event selection, among others
     */
    [HarmonyPrefix]
    [HarmonyPatch(typeof(InformationProcess), "OnUpdate")]
    public static bool OnUpdate(InformationProcess __instance, ProcessDataContainer ___container)
    {
        GameManager.SetMaxTrack();
        // Set headphone volume
        for (var i = 0; i < 2; i++)
        {
            var userData = UserDataManager.Instance.GetUserData(i);
            if (userData.IsEntry)
            {
                OptionHeadphonevolumeID headPhoneVolume = userData.Option.HeadPhoneVolume;
                SoundManager.SetHeadPhoneVolume(i, headPhoneVolume.GetValue());
            }
        }
        ___container.processManager.AddProcess(new MusicSelectProcess(___container));
        ___container.processManager.ReleaseProcess(__instance);
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(MapResultMonitor), "Initialize")]
    public static void MapResultMonitorPreInitialize(int monIndex)
    {
        var userData = Singleton<UserDataManager>.Instance.GetUserData(monIndex);
        var index = userData.MapList.FindIndex((m) => m.ID == userData.Detail.SelectMapID);
        if (index >= 0) return;
        userData.MapList.Clear();
    }
}

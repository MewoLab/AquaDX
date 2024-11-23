using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using AquaMai.Config.Attributes;
using AquaMai.Config.Types;
using AquaMai.Core.Helpers;
using HarmonyLib;
using MAI2.Util;
using Manager;
using MelonLoader;
using Process;
using Util;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "One key to hide all self-made charts in the music select process. Or hide for some users.",
    zh: "在选曲界面一键隐藏所有自制谱，或对一部分用户进行隐藏")]
public class HideSelfMadeCharts
{
    [ConfigEntry(
        en: "Key to toggle self-made charts.",
        zh: "切换自制谱显示的按键")]
    public static readonly KeyCodeOrName key = KeyCodeOrName.Test;

    [ConfigEntry]
    public static readonly bool longPress = false;

    [ConfigEntry(
        en: "One user ID per line in the file. Hide self-made charts when these users login.",
        zh: "该文件中每行一个用户 ID，当这些用户登录时隐藏自制谱")]
    private static readonly string selfMadeChartsDenyUsersFile = "LocalAssets/SelfMadeChartsDenyUsers.txt";

    [ConfigEntry(
        en: "One user ID per line in the file. Only show self-made charts when these users login.",
        zh: "该文件中每行一个用户 ID，只有这些用户登录时才显示自制谱")]
    private static readonly string selfMadeChartsWhiteListUsersFile = "LocalAssets/SelfMadeChartsWhiteListUsers.txt";

    private static Safe.ReadonlySortedDictionary<int, Manager.MaiStudio.MusicData> _musics;
    private static Safe.ReadonlySortedDictionary<int, Manager.MaiStudio.MusicData> _musicsNoneSelfMade;

    private static bool isShowSelfMadeCharts = true;
    private static bool isForceDisable;

    [HarmonyPostfix]
    [HarmonyPatch(typeof(DataManager), "GetMusics")]
    public static void GetMusics(ref Safe.ReadonlySortedDictionary<int, Manager.MaiStudio.MusicData> __result, List<string> ____targetDirs)
    {
        if (_musics is null)
        {
            // init musics for the first time
            if (__result.Count == 0) return;
            _musics = __result;
            var nonSelfMadeList = new SortedDictionary<int, Manager.MaiStudio.MusicData>();
            var officialDirs = ____targetDirs.Where(it => File.Exists(Path.Combine(it, "DataConfig.xml")) || File.Exists(Path.Combine(it, "OfficialChartsMark.txt")));
            foreach (var music in __result)
            {
                if (officialDirs.Any(it => MusicDirHelper.LookupPath(music.Value).StartsWith(it)))
                {
                    nonSelfMadeList.Add(music.Key, music.Value);
                }
            }

            _musicsNoneSelfMade = new Safe.ReadonlySortedDictionary<int, Manager.MaiStudio.MusicData>(nonSelfMadeList);
            MelonLogger.Msg($"[HideSelfMadeCharts] All music count: {__result.Count}, Official music count: {_musicsNoneSelfMade.Count}");
        }

        var stackTrace = new StackTrace(); // get call stack
        var stackFrames = stackTrace.GetFrames(); // get method calls (frames)
        if (stackFrames.All(it => it.GetMethod().DeclaringType.Name != "MusicSelectProcess")) return;
        if (isShowSelfMadeCharts && !isForceDisable) return;
        __result = _musicsNoneSelfMade;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(MusicSelectProcess), "OnUpdate")]
    public static void MusicSelectProcessOnUpdate(ref MusicSelectProcess __instance)
    {
        if (isForceDisable) return;
        if (!KeyListener.GetKeyDownOrLongPress(key, longPress)) return;
        isShowSelfMadeCharts = !isShowSelfMadeCharts;
        MelonLogger.Msg($"[HideSelfMadeCharts] isShowSelfMadeCharts: {isShowSelfMadeCharts}");
        SharedInstances.ProcessDataContainer.processManager.AddProcess(new FadeProcess(SharedInstances.ProcessDataContainer, __instance, new MusicSelectProcess(SharedInstances.ProcessDataContainer)));
        Task.Run(async () =>
        {
            await Task.Delay(1000);
            MessageHelper.ShowMessage($"{(isShowSelfMadeCharts ? "Show" : "Hide")} Self-Made Charts");
        });
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(MusicSelectProcess), "OnStart")]
    public static void MusicSelectProcessOnStart(ref MusicSelectProcess __instance)
    {
        var denyPath = FileSystem.ResolvePath(selfMadeChartsDenyUsersFile);
        if (File.Exists(denyPath))
        {
            var userIds = File.ReadAllLines(denyPath);
            for (var i = 0; i < 2; i++)
            {
                var user = Singleton<UserDataManager>.Instance.GetUserData(i);
                if (!user.IsEntry) continue;
                if (!userIds.Contains(user.Detail.UserID.ToString())) continue;
                isForceDisable = true;
                return;
            }
        }

        var whiteListPath = FileSystem.ResolvePath(selfMadeChartsWhiteListUsersFile);
        if (File.Exists(whiteListPath))
        {
            var userIds = File.ReadAllLines(whiteListPath);
            for (var i = 0; i < 2; i++)
            {
                var user = Singleton<UserDataManager>.Instance.GetUserData(i);
                if (!user.IsEntry) continue;
                if (userIds.Contains(user.Detail.UserID.ToString())) continue;
                isForceDisable = true;
                return;
            }
        }

        isForceDisable = false;
    }


    [HarmonyPostfix]
    [HarmonyPatch(typeof(EntryProcess), "OnStart")]
    public static void EntryProcessOnStart(ref EntryProcess __instance)
    {
        // reset status on login
        isShowSelfMadeCharts = true;
    }
}

using System.Collections.Generic;
using System.Diagnostics;
using HarmonyLib;
using Manager;
using MelonLoader;

namespace AquaMai.Helpers;

public class MusicDirHelper
{
    private static Dictionary<int, string> _map = new();

    [HarmonyPostfix]
    [HarmonyPatch(typeof(Manager.MaiStudio.Serialize.MusicData), "AddPath")]
    private static void AddPath(Manager.MaiStudio.Serialize.MusicData __instance, string parentPath)
    {
        _map[__instance.GetID()] = parentPath;
    }

    public static string LookupPath(int id)
    {
        return _map.GetValueOrDefault(id);
    }

    public static string LookupPath(Manager.MaiStudio.Serialize.MusicData musicData)
    {
        return LookupPath(musicData.GetID());
    }

    public static string LookupPath(Manager.MaiStudio.MusicData musicData)
    {
        return LookupPath(musicData.GetID());
    }
}

using Manager.MaiStudio;
using HarmonyLib;
using AquaMai.Config.Attributes;

namespace AquaMai.Mods.Cheat;

[ConfigSection(
    en: "Unlock maps that are not in this version",
    zh: "解锁游戏里所有的区域，包括非当前版本的（并不会帮你跑完）")]
public class MapUnlock
{
    // For any map, return the event ID 1 to unlock it
    [HarmonyPrefix]
    [HarmonyPatch(typeof(MapData), "get_OpenEventId")]
    public static bool get_OpenEventId(ref StringID __result)
    {
        var id = new Manager.MaiStudio.Serialize.StringID
        {
            id = 1,
            str = "無期限常時解放"
        };
        
        var sid = new StringID();
        sid.Init(id);
        
        __result = sid;
        return false;
    }
}

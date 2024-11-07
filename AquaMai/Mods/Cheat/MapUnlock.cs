using Manager.MaiStudio;
using HarmonyLib;

namespace AquaMai.Mods.Cheat
{
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
}

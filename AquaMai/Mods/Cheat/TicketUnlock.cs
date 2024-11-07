using Manager.MaiStudio;
using HarmonyLib;

namespace AquaMai.Mods.Cheat
{
    /**
     * Unlock tickets that are typically locked unless a specific event is open.
     */
    public class TicketUnlock
    {
        // For any ticket, return the event ID 1 to unlock it
        [HarmonyPrefix]
        [HarmonyPatch(typeof(TicketData), "get_ticketEvent")]
        public static bool get_ticketEvent(ref StringID __result)
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
        
        // Modify the maxTicketNum to 0
        // this is because TicketManager.GetTicketData adds the ticket to the list if either
        // the player owns at least one ticket or the maxTicketNum = 0
        [HarmonyPrefix]
        [HarmonyPatch(typeof(TicketData), "get_maxCount")]
        public static bool get_maxCount(ref int __result)
        {
            __result = 0;
            return false;
        }
    }
}

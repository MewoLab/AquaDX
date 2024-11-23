using AquaMai.Config.Attributes;
using AquaMai.Core.Attributes;
using MAI2System;
using Manager;
using Manager.MaiStudio;
using HarmonyLib;

namespace AquaMai.Mods.GameSystem;

[ConfigSection(
    en: "Unlock normally locked (including normally non-unlockable) game content",
    zh: "解锁原本锁定（包括正常途径无法解锁）的游戏内容")]
public class Unlock
{
    [ConfigEntry(
        en: "Unlock maps that are not in this version",
        zh: "解锁游戏里所有的区域，包括非当前版本的（并不会帮你跑完）")]
    private static readonly bool maps = true;

    [EnableIf(nameof(maps))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(MapData), "get_OpenEventId")]
    public static bool get_OpenEventId(ref StringID __result)
    {
        // For any map, return the event ID 1 to unlock it
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

    [ConfigEntry(
        en: "Unlock normally event-only tickets",
        zh: "解锁游戏里所有可能的跑图券")]
    private static readonly bool tickets = true;

    [EnableIf(nameof(tickets))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(TicketData), "get_ticketEvent")]
    public static bool get_ticketEvent(ref StringID __result)
    {
        // For any ticket, return the event ID 1 to unlock it
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

    [EnableIf(nameof(tickets))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(TicketData), "get_maxCount")]
    public static bool get_maxCount(ref int __result)
    {
        // Modify the maxTicketNum to 0
        // this is because TicketManager.GetTicketData adds the ticket to the list if either
        // the player owns at least one ticket or the maxTicketNum = 0
        __result = 0;
        return false;
    }

    [ConfigEntry(
        en: "Unlock Utage without the need of DXRating 10000",
        zh: "不需要万分也可以进宴会场")]
    private static readonly bool utage = true;

    [EnableIf(nameof(utage))]
    [EnableGameVersion(24000)]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(GameManager), "CanUnlockUtageTotalJudgement")]
    public static bool CanUnlockUtageTotalJudgement(out ConstParameter.ResultOfUnlockUtageJudgement result1P, out ConstParameter.ResultOfUnlockUtageJudgement result2P)
    {
        result1P = ConstParameter.ResultOfUnlockUtageJudgement.Unlocked;
        result2P = ConstParameter.ResultOfUnlockUtageJudgement.Unlocked;
        return false;
    }
}

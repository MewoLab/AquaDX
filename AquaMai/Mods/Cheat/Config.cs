using AquaMai.Attributes;

namespace AquaMai.Mods.Cheat;

public class Config
{
    [ConfigComment(
        en: "Unlock normally event-only tickets",
        zh: "解锁游戏里所有可能的跑图券")]
    public bool TicketUnlock { get; set; }

    [ConfigComment(
        en: "Unlock maps that are not in this version",
        zh: "解锁游戏里所有的区域，包括非当前版本的（并不会帮你跑完）")]
    public bool MapUnlock { get; set; }

    [ConfigComment(
        en: "Unlock Utage without the need of DXRating 10000",
        zh: "不需要万分也可以进宴会场")]
    public bool UnlockUtage { get; set; }
}

using AquaMai.Config.Attributes;

namespace AquaMai.Mods;

// This class is for settings only.

[ConfigSection(
    en: "AquaMai's general settings",
    zh: "AquaMai 的通用设置")]
public class General
{
    [ConfigEntry(
        en: "Language for mod UI (en and zh supported)",
        zh: "Mod 界面的语言，支持 en 和 zh")]
    public static readonly string Locale = "";
}

// Please add/remove corresponding entries in SetionNameOrder enum when adding/removing sections.
public enum SetionNameOrder
{
    General,
    Fix,
    SkipProcesses,
    GameSystem,
    GameAssets,
    GameSettings,
    UX,
    PracticeMode,
    Utils,
    Fancy
}

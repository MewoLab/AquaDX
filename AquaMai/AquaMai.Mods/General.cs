using AquaMai.Config.Attributes;

namespace AquaMai.Mods;

// This class is for settings only. Don't patch anything here.

[ConfigSection(
    en: "AquaMai's general settings",
    zh: "AquaMai 的通用设置",
    alwaysEnabled: true)]
public class General
{
    [ConfigEntry(
        en: """
            Language for mod UI (en and zh supported)
            If empty, the system language will be used
            The config file will also be saved in this language
            """,
        zh: """
            Mod 界面的语言，支持 en 和 zh
            如果为空，将使用系统语言
            配置文件也将以此语言保存
            """,
        specialConfigEntry: SpecialConfigEntry.Locale)]
    public static readonly string Locale = "";
}

// Please add/remove corresponding entries in SetionNameOrder enum when adding/removing sections.
public enum SetionNameOrder
{
    DeprecationWarning,
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

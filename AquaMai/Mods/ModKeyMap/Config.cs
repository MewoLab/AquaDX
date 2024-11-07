using AquaMai.Attributes;

namespace AquaMai.Mods.ModKeyMap;

public class Config
{
    [ConfigComment(
        en: "Skip to next step",
        zh: """
            跳过登录过程中的界面直接进入选歌界面
            在选歌界面直接结束游戏
            """)]
    public ModKeyCode QuickSkip { get; set; } = ModKeyCode.None;

    public bool QuickSkipLongPress { get; set; }

    [ConfigComment(
        en: "Quick retry in-game",
        zh: "游戏内快速重试")]
    public ModKeyCode InGameRetry { get; set; } = ModKeyCode.None;

    public bool InGameRetryLongPress { get; set; }

    [ConfigComment(
        en: "Quick skip in-game",
        zh: "游戏内快速跳过")]
    public ModKeyCode InGameSkip { get; set; } = ModKeyCode.None;

    public bool InGameSkipLongPress { get; set; }

    [ConfigComment(
        en: "Enter game test mode",
        zh: "进入游戏测试模式")]
    public ModKeyCode TestMode { get; set; } = ModKeyCode.Test;

    public bool TestModeLongPress { get; set; }

    [ConfigComment(
        zh: "练习模式")]
    public ModKeyCode PractiseMode { get; set; } = ModKeyCode.None;

    public bool PractiseModeLongPress { get; set; }

    [ConfigComment(
        zh: "选歌界面隐藏所有自制谱")]
    public ModKeyCode HideSelfMadeCharts { get; set; } = ModKeyCode.None;

    public bool HideSelfMadeChartsLongPress { get; set; }

    [ConfigComment(
        en: "Hold the bottom four buttons (3456) for official quick retry (non-utage only)",
        zh: "按住下方四个按钮（3456）使用官方快速重开（仅对非宴谱有效）")]
    public bool EnableNativeQuickRetry { get; set; }
}

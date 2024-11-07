using AquaMai.Attributes;

namespace AquaMai.Mods.TimeSaving;

public class Config
{
    [ConfigComment(
        en: "Skip the warning screen and logo shown after the POST sequence",
        zh: "跳过日服启动时候的 WARNING 界面")]
    public bool SkipWarningScreen { get; set; }

    [ConfigComment(
        en: "Disable some useless delays to speed up the game boot process",
        zh: """
            在自检界面，每个屏幕结束的时候都会等两秒才进入下一个屏幕，很浪费时间
            开了这个选项之后就不会等了
            """)]
    public bool ImproveLoadSpeed { get; set; }

    [ConfigComment(
        en: "Directly enter the song selection screen after login",
        zh: "登录完成后直接进入选歌界面")]
    public bool SkipToMusicSelection { get; set; }

    [ConfigComment(
        en: "Skip possible prompts like \"New area discovered\", \"New songs added\", \"There are events\" during game login/registration",
        zh: "跳过登录 / 注册游戏时候可能的 “发现了新的区域哟” “乐曲增加” “有活动哟” 之类的提示")]
    public bool SkipEventInfo { get; set; }

    [ConfigComment(
        en: "Skip the \"Do not tap or slide vigorously\" screen, immediately proceed to the next screen once data is loaded",
        zh: "跳过“不要大力拍打或滑动哦”这个界面，数据一旦加载完就立马进入下一个界面")]
    public bool IWontTapOrSlideVigorously { get; set; }

    [ConfigComment(
        en: "Skip the \"Goodbye\" screen at the end of the game",
        zh: "跳过游戏结束的“再见”界面")]
    public bool SkipGameOverScreen { get; set; }

    [ConfigComment(
        en: "Skip TrackStart screen",
        zh: "跳过乐曲开始界面")]
    public bool SkipTrackStart { get; set; }

    [ConfigComment(
        en: "Show a \"skip\" button like AstroDX after the notes end",
        zh: "音符结束之后显示像 AstroDX 一样的“跳过”按钮")]
    public bool ShowQuickEndPlay { get; set; }
}

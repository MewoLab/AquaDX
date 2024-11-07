using AquaMai.Attributes;

namespace AquaMai.Mods.Utils;

public class Config
{
    [ConfigComment(
        en: "Log user ID on login",
        zh: "登录时将 UserID 输出到日志")]
    public bool LogUserId { get; set; }

    [ConfigComment(
        en: "Globally increase A judgment, unit is the same as in the game",
        zh: "全局增加 A 判，单位和游戏里一样")]
    public float JudgeAdjustA { get; set; }

    [ConfigComment(
        en: "Globally increase B judgment, unit is the same as in the game",
        zh: "全局增加 B 判，单位和游戏里一样")]
    public float JudgeAdjustB { get; set; }

    [ConfigComment(
        en: "Touch screen delay, unit is milliseconds, one second = 1000 milliseconds. Must be an integer",
        zh: "触摸屏延迟，单位为毫秒，一秒 = 1000 毫秒。必须是整数")]
    public int TouchDelay { get; set; }

    [ConfigComment(
        en: "Show detail of selected song in music selection screen",
        zh: "选歌界面显示选择的歌曲的详情")]
    public bool SelectionDetail { get; set; }

    [ConfigComment(
        en: "Show Network error detail in the game",
        zh: "出现灰网时显示原因")]
    public bool ShowNetErrorDetail { get; set; }

    [ConfigComment(
        en: "Show error log in the game",
        zh: "在游戏中显示错误日志窗口而不是关闭游戏进程")]
    public bool ShowErrorLog { get; set; }

    [ConfigComment(
        en: "Display framerate",
        zh: "显示帧率")]
    public bool FrameRateDisplay { get; set; }

    [ConfigComment(
        en: """
            Adjust the baud rate of the touch screen serial port, default value is 9600
            Requires hardware support. If you are unsure whether you can use it, you cannot use it
            Set to 0 to disable
            """,
        zh: """
            调整触摸屏串口波特率，默认值 9600
            需要硬件配合。如果你不清楚你是否可以使用，那你不能使用
            改为 0 禁用
            """)]
    public int TouchPanelBaudRate { get; set; }
}

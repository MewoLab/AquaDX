using AquaMai.Attributes;

namespace AquaMai.Mods.WindowState;

public class Config
{
    [ConfigComment(
        en: "If not enabled, no operations will be performed on the game window",
        zh: "不启用的话，不会对游戏窗口做任何操作")]
    public bool Enable { get; set; }

    [ConfigComment(
        en: "Window the game",
        zh: "窗口化游戏")]
    public bool Windowed { get; set; }

    [ConfigComment(
        en: """
            Width and height for windowed mode, rendering resolution for fullscreen mode
            If set to 0, windowed mode will remember the user-set size, fullscreen mode will use the current display resolution
            """,
        zh: """
            宽度和高度窗口化时为游戏窗口大小，全屏时为渲染分辨率
            如果设为 0，窗口化将记住用户设定的大小，全屏时将使用当前显示器分辨率
            """)]
    public int Width { get; set; }

    [ConfigComment(
        zh: "高度")]
    public int Height { get; set; }
}

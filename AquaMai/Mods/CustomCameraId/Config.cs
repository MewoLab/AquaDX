using AquaMai.Attributes;

namespace AquaMai.Mods.CustomCameraId;

public class Config
{
    [ConfigComment(
        en: """
            Enable custom CameraId
            If enabled, you can customize the game to use the specified camera
            """,
        zh: """
            是否启用自定义摄像头ID
            启用后可以指定游戏使用的摄像头
            """)]
    public bool Enable { get; set; }

    [ConfigComment(
        en: "Print the camera list to the log when starting, can be used as a basis for modification",
        zh: "启动时打印摄像头列表到日志中，可以作为修改的依据")]
    public bool PrintCameraList { get; set; } = false;

    [ConfigComment(
        en: "DX Pass 1P",
        zh: "DX Pass 1P")]
    public int LeftQrCamera { get; set; } = 0;

    [ConfigComment(
        en: "DX Pass 2P",
        zh: "DX Pass 2P")]
    public int RightQrCamera { get; set; } = 0;

    [ConfigComment(
        zh: "玩家摄像头")]
    public int PhotoCamera { get; set; } = 0;

    [ConfigComment(
        zh: "二维码扫描摄像头")]
    public int ChimeCamera { get; set; } = 0;
}

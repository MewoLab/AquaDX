using AquaMai.Attributes;

namespace AquaMai.UX;

public class Config
{
    [ConfigComment(
        en: "Language for mod UI, supports en and zh",
        zh: "Mod 界面的语言，支持 en 和 zh")]
    public string Locale { get; set; } = "";

    [ConfigComment(
        en: "Single player: Show 1P only, at the center of the screen",
        zh: "单人模式，不显示 2P")]
    public bool SinglePlayer { get; set; }

    [ConfigComment(
        en: "Remove the circle mask in the game",
        zh: "移除遮罩")]
    public bool HideMask { get; set; }

    [ConfigComment(
        en: "Load Jacket image from folder \"LocalAssets\" and filename \"{MusicID}.png\" for self-made charts",
        zh: "通过游戏目录下 `LocalAssets\\000000（歌曲 ID）.png` 加载封面，自制谱用")]
    public bool LoadAssetsPng { get; set; }

    [ConfigComment(
        en: "Add \".ab\" image resources without the need of rebuilding a manifest",
        zh: """
            优化图片资源的加载，就算没有 AssetBundleImages.manifest 也可以正常加载 ab 格式的图片资源
            导入了删除曲包之类的话，应该需要开启这个
            """)]
    public bool LoadAssetBundleWithoutManifest { get; set; }

    [ConfigComment(
        en: """
            Random BGM, put Mai2Cue.{acb,awb} of old version of the game in `LocalAssets\Mai2Cue` and rename them
            Do not enable when SinglePlayer is off
            """,
        zh: """
            在 `LocalAssets\Mai2Cue` 这个目录下放置了旧版游戏的 Mai2Cue.{acb,awb} 并重命名的话，可以在播放游戏 BGM 的时候随机播放这里面的旧版游戏 BGM
            和 2P 模式有冲突，如果你没有开启 'SinglePlayer' 的话，请关闭这个
            """)]
    public bool RandomBgm { get; set; }

    [ConfigComment(
        en: "Play \"Master\" difficulty on Demo screen",
        zh: "在闲置时的演示画面上播放紫谱而不是绿谱")]
    public bool DemoMaster { get; set; }

    [ConfigComment(
        en: """
            Disable timers
            Not recommand to enable when SinglePlayer is off
            """,
        zh: """
            关掉那些游戏中的倒计时
            如果你没有开启 'SinglePlayer' 的话，不建议开这个，不过要开的话也不是不可以
            """)]
    public bool ExtendTimer { get; set; }

    [ConfigComment(
        en: "Save immediate after playing a song",
        zh: "打完一首歌的时候立即向服务器保存成绩")]
    public bool ImmediateSave { get; set; }

    [ConfigComment(
        en: """
            Use the png jacket above as BGA if BGA is not found for self-made charts
            Use together with `LoadJacketPng`
            """,
        zh: """
            如果没有 dat 格式的 BGA 的话，就用歌曲的封面做背景，而不是显示迪拉熊的笑脸
            请和 `LoadJacketPng` 一起用
            """)]
    public bool LoadLocalBga { get; set; }

    [ConfigComment(
        en: """
            Place font.ttf in the LocalAssets directory to replace the game's global font
            Cannot be used together with FontFix
            """,
        zh: """
            在 LocalAssets 目录下放置 font.ttf 可以替换游戏的全局字体
            不可以和 FontFix 一起使用
            """)]
    public bool CustomFont { get; set; }

    [ConfigComment(
        en: "Map touch actions to buttons",
        zh: "映射触摸操作至实体按键")]
    public bool TouchToButtonInput { get; set; }

    [ConfigComment(
        en: "Cannot be used together with HanabiFix",
        zh: """
            完全隐藏烟花
            不能和 HanabiFix 一起使用
            """)]
    public bool HideHanabi { get; set; }

    [ConfigComment(
        zh: "取消星星从 50% 透明度直接闪为 100% 的特性，星星会慢慢出现",
        en: "Slides will fade in instead of instantly appearing")]
    public bool SlideFadeInTweak { get; set; }

    [ConfigComment(
        zh: "在游戏总结的计分板中显示判定的详细信息（毫秒数）",
        en: "Show detailed judgment information (in milliseconds) in the score board")]
    public bool JudgeAccuracyInfo { get; set; }

    [ConfigComment(
        en: "Set the version string displayed at the top-right corner of the screen",
        zh: "把右上角的版本更改为自定义文本")]
    public string CustomVersionString { get; set; } = "";

    [ConfigComment(
        en: """
            Custom shop name in photo
            Also enable shop name display in SDGA
            """,
        zh: """
            自定义拍照的店铺名称
            同时在 SDGA 中会启用店铺名称的显示（但是不会在游戏里有设置）
            """)]
    public string CustomPlaceName { get; set; } = "";

    [ConfigComment(
        en: "Execute some command on game idle",
        zh: """
            在游戏闲置的时候执行指定的命令脚本
            比如说可以在游戏闲置是降低显示器的亮度
            """)]
    public string ExecOnIdle { get; set; } = "";

    [ConfigComment(
        en: "Execute some command on game start",
        zh: "在玩家登录的时候执行指定的命令脚本")]
    public string ExecOnEntry { get; set; } = "";
}
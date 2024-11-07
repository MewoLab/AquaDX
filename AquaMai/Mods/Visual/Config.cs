using AquaMai.Attributes;

namespace AquaMai.Mods.Visual;

public class Config
{
    [ConfigComment(
        en: """
            Provide the ability to use custom skins (advanced feature)
            Load skin textures from LocalAssets\Skins
            """,
        zh: """
            提供自定义皮肤的能力（高级功能）
            从 LocalAssets\Skins 中加载皮肤贴图
            """)]
    public bool CustomSkins { get; set; }

    [ConfigComment(
        en: """
            More detailed judgment display
            Requires CustomSkins to be enabled and the resource file to be downloaded
            https://github.com/hykilpikonna/AquaDX/releases/download/nightly/JudgeDisplay4B.7z
            """,
        zh: """
            更精细的判定表示
            需开启 CustomSkins 并下载资源文件
            https://github.com/hykilpikonna/AquaDX/releases/download/nightly/JudgeDisplay4B.7z
            """)]
    public bool JudgeDisplay4B { get; set; }

    [ConfigComment(
        en: """
            Custom track start difficulty image (not really custom difficulty)
            Requires CustomSkins to be enabled
            Will load four image resources through custom skins: musicBase, musicTab, musicLvBase, musicLvText
            """,
        zh: """
            自定义在歌曲开始界面上显示的难度贴图 (并不是真的自定义难度)
            需要启用自定义皮肤功能
            会通过自定义皮肤加载四个图片资源: musicBase, musicTab, musicLvBase, musicLvText
            """)]
    public bool CustomTrackStartDiff { get; set; }

    [ConfigComment(
        en: """
            Delayed the animation of the song start screen
            For recording chart confirmation
            """,
        zh: """
            推迟了歌曲开始界面的动画
            录制谱面确认用
            """)]
    public bool TrackStartProcessTweak { get; set; }

    [ConfigComment(
        en: """
            Disable the TRACK X text, DX/Standard display box, and the derakkuma at the bottom of the screen in the song start screen
            For recording chart confirmation
            """,
        zh: """
            在歌曲开始界面, 把 TRACK X 字样, DX/标准谱面的显示框, 以及画面下方的滴蜡熊隐藏掉
            录制谱面确认用
            """)]
    public bool DisableTrackStartTabs { get; set; }

    [ConfigComment(
        en: """
            Make the judgment display of Wifi Slide different in up and down (originally all Wifi judgment displays are towards the center), just like in majdata
            The reason for this bug is that SEGA forgot to assign EndButtonId to Wifi
            """,
        zh: """
            这个 Patch 让 Wifi Slide 的判定显示有上下的区别 (原本所有 Wifi 的判定显示都是朝向圆心的), 就像 majdata 里那样
            这个 bug 产生的原因是 SBGA 忘记给 Wifi 的 EndButtonId 赋值了
            """)]
    public bool FanJudgeFlip { get; set; }

    [ConfigComment(
        en: """
            This Patch makes the Critical judgment of BreakSlide also flicker like BreakTap
            Recommended to use with custom skins (otherwise the visual effect may not be good)
            """,
        zh: """
            这个 Patch 让 BreakSlide 的 Critical 判定也可以像 BreakTap 一样闪烁
            推荐与自定义皮肤一起使用 (否则视觉效果可能并不好)
            """)]
    public bool BreakSlideJudgeBlink { get; set; }
    
    [ConfigComment(
        en: """
            Make the Slide Track disappear with an inward-shrinking animation, similar to AstroDX
            """,
        zh: """
            使 Slide Track 消失时有类似 AstroDX 一样的向内缩入的动画
            """)]
    public bool SlideArrowAnimation { get; set; }
    
    [ConfigComment(
        en: """
            Invert the Slide hierarchy, so that the new Slide appears on top like Maimai classic
            Enable to support color changing effects achieved by overlaying multiple stars
            """,
        zh: """
            反转 Slide 层级, 使新出现的 Slide 像旧框一样显示在上层
            启用以支持通过叠加多个星星达成的变色效果
            """)]
    public bool SlideLayerReverse { get; set; }
}

using System.Diagnostics.CodeAnalysis;
using AquaMai.Attributes;

namespace AquaMai;

[SuppressMessage("ReSharper", "ClassNeverInstantiated.Global")]
public class Config
{
    // [ConfigComment(
    //     en: "UX: User Experience Improvements",
    //     zh: """

    //         试试使用 MaiChartManager 图形化配置 AquaMai 吧！
    //         https://github.com/clansty/MaiChartManager

    //         用户体验改进
    //         """)]
    // public Mods.UX.Config UX { get; set; } = new();

    [ConfigComment(
        en: "Visual effects of notes and judgment display and some other textures",
        zh: "音符和判定表示以及一些其他贴图的视觉效果调整")]
    public Mods.Visual.Config Visual { get; set; } = new();
}

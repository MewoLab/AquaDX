using System.Diagnostics.CodeAnalysis;
using AquaMai.Attributes;

namespace AquaMai;

[SuppressMessage("ReSharper", "ClassNeverInstantiated.Global")]
public class Config
{
    [ConfigComment(
        en: "UX: User Experience Improvements",
        zh: """

            试试使用 MaiChartManager 图形化配置 AquaMai 吧！
            https://github.com/clansty/MaiChartManager

            用户体验改进
            """)]
    public Mods.UX.Config UX { get; set; } = new();

    [ConfigComment(
        en: "Cheat: You control the buttons you press",
        zh: "“作弊”功能")]
    public Mods.Cheat.Config Cheat { get; set; } = new();

    [ConfigComment(
        en: "Fix: Fix some potential issues",
        zh: "修复一些潜在的问题")]
    public Mods.Fix.Config Fix { get; set; } = new();

    [ConfigComment(
        zh: "实用工具")]
    public Mods.Utils.Config Utils { get; set; } = new();

    [ConfigComment(
        en: "Time Saving: Skip some unnecessary screens",
        zh: "节省一些不知道有用没用的时间，跳过一些不必要的界面")]
    public Mods.TimeSaving.Config TimeSaving { get; set; } = new();

    [ConfigComment(
        en: "Visual effects of notes and judgment display and some other textures",
        zh: "音符和判定表示以及一些其他贴图的视觉效果调整")]
    public Mods.Visual.Config Visual { get; set; } = new();

    [ConfigComment(
        zh: "Mod 内功能的按键设置")]
    public Mods.ModKeyMap.Config ModKeyMap { get; set; } = new();

    [ConfigComment(
        zh: "窗口相关设置")]
    public Mods.WindowState.Config WindowState { get; set; } = new();

    [ConfigComment(
        en: "Custom camera ID settings",
        zh: "自定义摄像头 ID")]
    public Mods.CustomCameraId.Config CustomCameraId { get; set; } = new();

    [ConfigComment(
        zh: "触摸灵敏度设置")]
    public Mods.TouchSensitivity.Config TouchSensitivity { get; set; } = new();

    [ConfigComment(
        zh: "自定义按键映射")]
    public Mods.CustomKeyMap.Config CustomKeyMap { get; set; } = new();
}

using AquaMai.Attributes;

namespace AquaMai.Mods.TouchSensitivity;

public class Config
{
    [ConfigComment(
        en: """
            Enable custom sensitivity
            When enabled, the settings in Test mode will not take effect
            When disabled, the settings in Test mode can still be used
            """,
        zh: """
            是否启用自定义灵敏度
            这里启用之后 Test 里的就不再起作用了
            这里禁用之后就还是可以用 Test 里的调
            """)]
    public bool Enable { get; set; }

    [ConfigComment(
        en: """
            Sensitivity adjustments in Test mode are not linear
            Default sensitivity in area A: 90, 80, 70, 60, 50, 40, 30, 26, 23, 20, 10
            Default sensitivity in other areas: 70, 60, 50, 40, 30, 20, 15, 10, 5, 1, 1
            A setting of 0 in Test mode corresponds to 40, 20 here, -5 corresponds to 90, 70, +5 corresponds to 10, 1
            The higher the number in Test mode, the lower the number here, resulting in higher sensitivity for official machines
            For ADX, the sensitivity is reversed, so the higher the number here, the higher the sensitivity
            """,
        zh: """
            在 Test 模式下调整的灵敏度不是线性的
            A 区默认灵敏度 90, 80, 70, 60, 50, 40, 30, 26, 23, 20, 10
            其他区域默认灵敏度 70, 60, 50, 40, 30, 20, 15, 10, 5, 1, 1
            Test 里设置的 0 对应的是 40, 20 这一档，-5 是 90, 70，+5 是 10, 1
            Test 里的数字越大，这里的数字越小，对于官机来说，灵敏度更大
            而 ADX 的灵敏度是反的，所以对于 ADX，这里的数字越大，灵敏度越大
            """)]
    public byte A1 { get; set; } = 40;

    public byte A2 { get; set; } = 40;
    public byte A3 { get; set; } = 40;
    public byte A4 { get; set; } = 40;
    public byte A5 { get; set; } = 40;
    public byte A6 { get; set; } = 40;
    public byte A7 { get; set; } = 40;
    public byte A8 { get; set; } = 40;
    public byte B1 { get; set; } = 20;
    public byte B2 { get; set; } = 20;
    public byte B3 { get; set; } = 20;
    public byte B4 { get; set; } = 20;
    public byte B5 { get; set; } = 20;
    public byte B6 { get; set; } = 20;
    public byte B7 { get; set; } = 20;
    public byte B8 { get; set; } = 20;
    public byte C1 { get; set; } = 20;
    public byte C2 { get; set; } = 20;
    public byte D1 { get; set; } = 20;
    public byte D2 { get; set; } = 20;
    public byte D3 { get; set; } = 20;
    public byte D4 { get; set; } = 20;
    public byte D5 { get; set; } = 20;
    public byte D6 { get; set; } = 20;
    public byte D7 { get; set; } = 20;
    public byte D8 { get; set; } = 20;
    public byte E1 { get; set; } = 20;
    public byte E2 { get; set; } = 20;
    public byte E3 { get; set; } = 20;
    public byte E4 { get; set; } = 20;
    public byte E5 { get; set; } = 20;
    public byte E6 { get; set; } = 20;
    public byte E7 { get; set; } = 20;
    public byte E8 { get; set; } = 20;
}

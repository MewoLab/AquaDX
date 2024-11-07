using AquaMai.Attributes;

namespace AquaMai.Mods.CustomKeyMap;

public class Config
{
    [ConfigComment(
        en: "These settings will work regardless of whether you have enabled segatools' io4 emulation",
        zh: "这里的设置无论你是否启用了 segatools 的 io4 模拟都会工作")]
    public bool Enable { get; set; }

    public KeyCodeID Test { get; set; } = (KeyCodeID)115;
    public KeyCodeID Service { get; set; } = (KeyCodeID)5;
    public KeyCodeID Button1_1P { get; set; } = (KeyCodeID)67;
    public KeyCodeID Button2_1P { get; set; } = (KeyCodeID)49;
    public KeyCodeID Button3_1P { get; set; } = (KeyCodeID)48;
    public KeyCodeID Button4_1P { get; set; } = (KeyCodeID)47;
    public KeyCodeID Button5_1P { get; set; } = (KeyCodeID)68;
    public KeyCodeID Button6_1P { get; set; } = (KeyCodeID)70;
    public KeyCodeID Button7_1P { get; set; } = (KeyCodeID)45;
    public KeyCodeID Button8_1P { get; set; } = (KeyCodeID)61;
    public KeyCodeID Select_1P { get; set; } = (KeyCodeID)25;
    public KeyCodeID Button1_2P { get; set; } = (KeyCodeID)80;
    public KeyCodeID Button2_2P { get; set; } = (KeyCodeID)81;
    public KeyCodeID Button3_2P { get; set; } = (KeyCodeID)78;
    public KeyCodeID Button4_2P { get; set; } = (KeyCodeID)75;
    public KeyCodeID Button5_2P { get; set; } = (KeyCodeID)74;
    public KeyCodeID Button6_2P { get; set; } = (KeyCodeID)73;
    public KeyCodeID Button7_2P { get; set; } = (KeyCodeID)76;
    public KeyCodeID Button8_2P { get; set; } = (KeyCodeID)79;
    public KeyCodeID Select_2P { get; set; } = (KeyCodeID)84;
}

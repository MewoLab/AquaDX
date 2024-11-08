using System.Diagnostics;
using System.Linq;
using AquaMai.Attributes;
using AquaMai.Helpers;
using DB;
using HarmonyLib;
using Manager;

namespace AquaMai.Mods;

[ConfigSection(
    en: "These settings will work regardless of whether you have enabled segatools' io4 emulation",
    zh: "这里的设置无论你是否启用了 segatools 的 io4 模拟都会工作")]
public class GameKeyMap
{
    [ConfigEntry]
    public static readonly KeyCodeID Test = (KeyCodeID)115;

    [ConfigEntry]
    public static readonly KeyCodeID Service = (KeyCodeID)5;

    [ConfigEntry]
    public static readonly KeyCodeID Button1_1P = (KeyCodeID)67;

    [ConfigEntry]
    public static readonly KeyCodeID Button2_1P = (KeyCodeID)49;

    [ConfigEntry]
    public static readonly KeyCodeID Button3_1P = (KeyCodeID)48;

    [ConfigEntry]
    public static readonly KeyCodeID Button4_1P = (KeyCodeID)47;

    [ConfigEntry]
    public static readonly KeyCodeID Button5_1P = (KeyCodeID)68;

    [ConfigEntry]
    public static readonly KeyCodeID Button6_1P = (KeyCodeID)70;

    [ConfigEntry]
    public static readonly KeyCodeID Button7_1P = (KeyCodeID)45;

    [ConfigEntry]
    public static readonly KeyCodeID Button8_1P = (KeyCodeID)61;

    [ConfigEntry]
    public static readonly KeyCodeID Select_1P = (KeyCodeID)25;

    [ConfigEntry]
    public static readonly KeyCodeID Button1_2P = (KeyCodeID)80;

    [ConfigEntry]
    public static readonly KeyCodeID Button2_2P = (KeyCodeID)81;

    [ConfigEntry]
    public static readonly KeyCodeID Button3_2P = (KeyCodeID)78;

    [ConfigEntry]
    public static readonly KeyCodeID Button4_2P = (KeyCodeID)75;

    [ConfigEntry]
    public static readonly KeyCodeID Button5_2P = (KeyCodeID)74;

    [ConfigEntry]
    public static readonly KeyCodeID Button6_2P = (KeyCodeID)73;

    [ConfigEntry]
    public static readonly KeyCodeID Button7_2P = (KeyCodeID)76;

    [ConfigEntry]
    public static readonly KeyCodeID Button8_2P = (KeyCodeID)79;

    [ConfigEntry]
    public static readonly KeyCodeID Select_2P = (KeyCodeID)84;

    [ConfigEntry(
        en: """
            When enabled, test button must be long pressed to enter game test mode
            When test button is bound to other features, this option is enabled automatically
            """,
        zh: """
            启用后，测试键必须长按才能进入游戏测试模式
            当测试键被绑定到其它功能时，此选项自动开启
            """)]
    public static readonly bool TestProof = false; // TODO: auto enable when Test is bound to other features

    [HarmonyPatch(typeof(JvsButtonTableRecord), MethodType.Constructor, typeof(int), typeof(string), typeof(string), typeof(int), typeof(string), typeof(int), typeof(int), typeof(int))]
    [HarmonyPostfix]
    public static void JvsButtonTableRecordConstructor(JvsButtonTableRecord __instance, string Name)
    {
        var prop = (DB.KeyCodeID)typeof(GameKeyMap).GetField(Name).GetValue(null);
        __instance.SubstituteKey = prop;
    }

    [EnableIf(nameof(TestProof))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(InputManager), "GetSystemInputDown")]
    public static bool GetSystemInputDown(ref bool __result, InputManager.SystemButtonSetting button, bool[] ___SystemButtonDown)
    {
        __result = ___SystemButtonDown[(int)button];
        if (button != InputManager.SystemButtonSetting.ButtonTest)
            return false;

        var stackTrace = new StackTrace(); // get call stack
        var stackFrames = stackTrace.GetFrames(); // get method calls (frames)

        if (stackFrames.Any(it => it.GetMethod().Name == "DMD<Main.GameMainObject::Update>"))
        {
            // TODO: oh really?
            __result = KeyListener.GetKeyDownOrLongPress(KeyCodeOrName.Test, true);
        }

        return false;
    }
}

public enum KeyCodeID
{
    None,
    Backspace,
    Tab,
    Clear,
    Return,
    Pause,
    Escape,
    Space,
    Exclaim,
    DoubleQuote,
    Hash,
    Dollar,
    Ampersand,
    Quote,
    LeftParen,
    RightParen,
    Asterisk,
    Plus,
    Comma,
    Minus,
    Period,
    Slash,
    Alpha0,
    Alpha1,
    Alpha2,
    Alpha3,
    Alpha4,
    Alpha5,
    Alpha6,
    Alpha7,
    Alpha8,
    Alpha9,
    Colon,
    Semicolon,
    Less,
    Equals,
    Greater,
    Question,
    At,
    LeftBracket,
    Backslash,
    RightBracket,
    Caret,
    Underscore,
    BackQuote,
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H,
    I,
    J,
    K,
    L,
    M,
    N,
    O,
    P,
    Q,
    R,
    S,
    T,
    U,
    V,
    W,
    X,
    Y,
    Z,
    Delete,
    Keypad0,
    Keypad1,
    Keypad2,
    Keypad3,
    Keypad4,
    Keypad5,
    Keypad6,
    Keypad7,
    Keypad8,
    Keypad9,
    KeypadPeriod,
    KeypadDivide,
    KeypadMultiply,
    KeypadMinus,
    KeypadPlus,
    KeypadEnter,
    KeypadEquals,
    UpArrow,
    DownArrow,
    RightArrow,
    LeftArrow,
    Insert,
    Home,
    End,
    PageUp,
    PageDown,
    F1,
    F2,
    F3,
    F4,
    F5,
    F6,
    F7,
    F8,
    F9,
    F10,
    F11,
    F12,
    F13,
    F14,
    F15,
    Numlock,
    CapsLock,
    ScrollLock,
    RightShift,
    LeftShift,
    RightControl,
    LeftControl,
    RightAlt,
    LeftAlt,
    RightCommand,
    RightApple,
    LeftCommand,
    LeftApple,
    LeftWindows,
    RightWindows,
    AltGr,
    Help,
    Print,
    SysReq,
    Break,
    Menu,
    Mouse0,
    Mouse1,
    Mouse2,
    Mouse3,
    Mouse4,
    Mouse5,
    Mouse6,
}

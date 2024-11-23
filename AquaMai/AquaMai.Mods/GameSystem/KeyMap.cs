using System.Diagnostics;
using System.Linq;
using AquaMai.Config.Attributes;
using AquaMai.Config.Types;
using AquaMai.Core.Attributes;
using AquaMai.Core.Helpers;
using AquaMai.Mods.UX;
using HarmonyLib;
using Manager;
using MelonLoader;

namespace AquaMai.Mods.GameSystem;

[ConfigSection(
    en: "These settings will work regardless of whether you have enabled segatools' io4 emulation",
    zh: "这里的设置无论你是否启用了 segatools 的 io4 模拟都会工作")]
public class KeyMap
{
    [ConfigEntry]
    private static readonly KeyCodeID Test = (KeyCodeID)115;

    [ConfigEntry]
    private static readonly KeyCodeID Service = (KeyCodeID)5;

    [ConfigEntry]
    private static readonly KeyCodeID Button1_1P = (KeyCodeID)67;

    [ConfigEntry]
    private static readonly KeyCodeID Button2_1P = (KeyCodeID)49;

    [ConfigEntry]
    private static readonly KeyCodeID Button3_1P = (KeyCodeID)48;

    [ConfigEntry]
    private static readonly KeyCodeID Button4_1P = (KeyCodeID)47;

    [ConfigEntry]
    private static readonly KeyCodeID Button5_1P = (KeyCodeID)68;

    [ConfigEntry]
    private static readonly KeyCodeID Button6_1P = (KeyCodeID)70;

    [ConfigEntry]
    private static readonly KeyCodeID Button7_1P = (KeyCodeID)45;

    [ConfigEntry]
    private static readonly KeyCodeID Button8_1P = (KeyCodeID)61;

    [ConfigEntry]
    private static readonly KeyCodeID Select_1P = (KeyCodeID)25;

    [ConfigEntry]
    private static readonly KeyCodeID Button1_2P = (KeyCodeID)80;

    [ConfigEntry]
    private static readonly KeyCodeID Button2_2P = (KeyCodeID)81;

    [ConfigEntry]
    private static readonly KeyCodeID Button3_2P = (KeyCodeID)78;

    [ConfigEntry]
    private static readonly KeyCodeID Button4_2P = (KeyCodeID)75;

    [ConfigEntry]
    private static readonly KeyCodeID Button5_2P = (KeyCodeID)74;

    [ConfigEntry]
    private static readonly KeyCodeID Button6_2P = (KeyCodeID)73;

    [ConfigEntry]
    private static readonly KeyCodeID Button7_2P = (KeyCodeID)76;

    [ConfigEntry]
    private static readonly KeyCodeID Button8_2P = (KeyCodeID)79;

    [ConfigEntry]
    private static readonly KeyCodeID Select_2P = (KeyCodeID)84;

    [ConfigEntry(
        en: """
            When enabled, test button must be long pressed to enter game test mode
            When test button is bound to other features, this option is enabled automatically
            """,
        zh: """
            启用后，测试键必须长按才能进入游戏测试模式
            当测试键被绑定到其它功能时，此选项自动开启
            """)]
    public static readonly bool testProof = false;

    public static bool testProofImplied = false;
    public static bool TestProofEnabled => testProof || testProofImplied;

    public static void OnBeforePatch()
    {
        KeyCodeOrName[] featureKeys = [
            OneKeyEntryEnd.key,
            OneKeyRetrySkip.retryKey,
            OneKeyRetrySkip.skipKey,
            HideSelfMadeCharts.key,
            PracticeMode.PracticeMode.key
        ];
        testProofImplied = featureKeys.Any(it => it == KeyCodeOrName.Test || it.ToString() == Test.ToString());
        if (testProofImplied && !testProof)
        {
            MelonLogger.Warning("Test button bound to other feature, enabling test proof");
        }
    }

    [HarmonyPatch(typeof(DB.JvsButtonTableRecord), MethodType.Constructor, typeof(int), typeof(string), typeof(string), typeof(int), typeof(string), typeof(int), typeof(int), typeof(int))]
    [HarmonyPostfix]
    public static void JvsButtonTableRecordConstructor(DB.JvsButtonTableRecord __instance, string Name)
    {
        var prop = (DB.KeyCodeID)typeof(KeyMap).GetField(Name).GetValue(null);
        __instance.SubstituteKey = prop;
    }

    [EnableIf(nameof(TestProofEnabled))]
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

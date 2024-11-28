using System.Diagnostics;
using System.Linq;
using AquaMai.Config.Attributes;
using AquaMai.Config.Types;
using AquaMai.Core;
using AquaMai.Core.Attributes;
using AquaMai.Core.Helpers;
using AquaMai.Mods.Tweaks;
using AquaMai.Mods.UX;
using AquaMai.Mods.UX.PracticeMode;
using HarmonyLib;
using Manager;

namespace AquaMai.Mods.GameSystem;

[ConfigSection(
    en: """
        When enabled, test button must be long pressed to enter game test mode.
        When test button is bound to other features, this option is enabled automatically.
        """,
    zh: """
        启用后，测试键必须长按才能进入游戏测试模式
        当测试键被绑定到其它功能时，此选项自动开启
        """)]
[EnableImplicitlyIf(nameof(ShouldEnableImplicitly))]
public class TestProof
{
    public static bool ShouldEnableImplicitly
    {
        get
        {
            (System.Type section, KeyCodeOrName key)[] featureKeys =
            [
                (typeof(OneKeyEntryEnd), OneKeyEntryEnd.key),
                (typeof(OneKeyRetrySkip), OneKeyRetrySkip.retryKey),
                (typeof(OneKeyRetrySkip), OneKeyRetrySkip.skipKey),
                (typeof(HideSelfMadeCharts), HideSelfMadeCharts.key),
                (typeof(PracticeMode), PracticeMode.key),
                (typeof(ResetTouch), ResetTouch.key),
            ];
            var keyMapEnabled = ConfigLoader.Config.GetSectionState(typeof(KeyMap)).Enabled;
            return featureKeys.Any(it =>
                // The feature is enabled and...
                ConfigLoader.Config.GetSectionState(it.section).Enabled &&
                (
                    // and the key is test, or...
                    it.key == KeyCodeOrName.Test ||
                    // or the key have been mapped to the same key as test.
                    (keyMapEnabled && it.key.ToString() == KeyMap.Test.ToString())));
        }
    }

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
            __result = KeyListener.GetKeyDownOrLongPress(KeyCodeOrName.Test, true);
        }

        return false;
    }
}

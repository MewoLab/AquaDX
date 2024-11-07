using System.Diagnostics;
using System.Linq;
using HarmonyLib;
using Manager;

namespace AquaMai.Mods.ModKeyMap;

public class TestProof
{
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
            __result = ModKeyListener.GetKeyDownOrLongPress(AquaMai.AppConfig.ModKeyMap.TestMode, AquaMai.AppConfig.ModKeyMap.TestModeLongPress);
        }

        return false;
    }
}

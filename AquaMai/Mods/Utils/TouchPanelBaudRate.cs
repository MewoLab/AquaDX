using HarmonyLib;
using IO;

namespace AquaMai.Mods.Utils;

public class TouchPanelBaudRate
{
    [HarmonyPatch(typeof(NewTouchPanel), "Open")]
    [HarmonyPrefix]
    private static void OpenPrefix(ref int ___BaudRate)
    {
        if (AquaMai.AppConfig.Utils.TouchPanelBaudRate <= 0) return;
        ___BaudRate = AquaMai.AppConfig.Utils.TouchPanelBaudRate;
    }
}

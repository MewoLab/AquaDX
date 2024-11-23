using AquaMai.Config.Attributes;
using HarmonyLib;
using IO;

namespace AquaMai.Mods.GameSystem;

[ConfigSection(
    en: """
        Adjust the baud rate of the touch screen serial port, default value is 9600.
        Requires hardware support. If you are unsure, don't use it.
        """,
    zh: """
        调整触摸屏串口波特率，默认值 9600
        需要硬件配合，如果你不清楚你是否可以使用，请不要使用
        """)]
public class TouchPanelBaudRate
{
    [ConfigEntry(
        en: "Baud rate",
        zh: "波特率")]
    private static readonly int baudRate = 9600;

    [HarmonyPatch(typeof(NewTouchPanel), "Open")]
    [HarmonyPrefix]
    private static void OpenPrefix(ref int ___BaudRate)
    {
        if (baudRate <= 0) return;
        ___BaudRate = baudRate;
    }
}

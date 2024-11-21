using System.Collections.Generic;
using AquaMai.Config.Attributes;
using HarmonyLib;
using IO;
using Manager;
using MelonLoader;

namespace AquaMai.Mods.GameSettings;

[ConfigSection(
    en: """
        Use custom touch sensitivity
        When enabled, the settings in Test mode will not take effect
        When disabled, the settings in Test mode is used

        Sensitivity adjustments in Test mode are not linear
        Default sensitivity in area A: 90, 80, 70, 60, 50, 40, 30, 26, 23, 20, 10
        Default sensitivity in other areas: 70, 60, 50, 40, 30, 20, 15, 10, 5, 1, 1
        A setting of 0 in Test mode corresponds to 40, 20 here, -5 corresponds to 90, 70, +5 corresponds to 10, 1
        The higher the number in Test mode, the lower the number here, resulting in higher sensitivity for official machines
        For ADX, the sensitivity is reversed, so the higher the number here, the higher the sensitivity
        """,
    zh: """
        使用自定义触摸灵敏度
        这里启用之后 Test 里的就不再起作用了
        这里禁用之后就还是用 Test 里的调

        在 Test 模式下调整的灵敏度不是线性的
        A 区默认灵敏度 90, 80, 70, 60, 50, 40, 30, 26, 23, 20, 10
        其他区域默认灵敏度 70, 60, 50, 40, 30, 20, 15, 10, 5, 1, 1
        Test 里设置的 0 对应的是 40, 20 这一档，-5 是 90, 70，+5 是 10, 1
        Test 里的数字越大，这里的数字越小，对于官机来说，灵敏度更大
        而 ADX 的灵敏度是反的，所以对于 ADX，这里的数字越大，灵敏度越大
        """)]
public class TouchSensitivity
{
    [ConfigEntry]
    private static readonly byte A1 = 40;

    [ConfigEntry]
    private static readonly byte A2 = 40;

    [ConfigEntry]
    private static readonly byte A3 = 40;

    [ConfigEntry]
    private static readonly byte A4 = 40;

    [ConfigEntry]
    private static readonly byte A5 = 40;

    [ConfigEntry]
    private static readonly byte A6 = 40;

    [ConfigEntry]
    private static readonly byte A7 = 40;

    [ConfigEntry]
    private static readonly byte A8 = 40;

    [ConfigEntry]
    private static readonly byte B1 = 20;

    [ConfigEntry]
    private static readonly byte B2 = 20;

    [ConfigEntry]
    private static readonly byte B3 = 20;

    [ConfigEntry]
    private static readonly byte B4 = 20;

    [ConfigEntry]
    private static readonly byte B5 = 20;

    [ConfigEntry]
    private static readonly byte B6 = 20;

    [ConfigEntry]
    private static readonly byte B7 = 20;

    [ConfigEntry]
    private static readonly byte B8 = 20;

    [ConfigEntry]
    private static readonly byte C1 = 20;

    [ConfigEntry]
    private static readonly byte C2 = 20;

    [ConfigEntry]
    private static readonly byte D1 = 20;

    [ConfigEntry]
    private static readonly byte D2 = 20;

    [ConfigEntry]
    private static readonly byte D3 = 20;

    [ConfigEntry]
    private static readonly byte D4 = 20;

    [ConfigEntry]
    private static readonly byte D5 = 20;

    [ConfigEntry]
    private static readonly byte D6 = 20;

    [ConfigEntry]
    private static readonly byte D7 = 20;

    [ConfigEntry]
    private static readonly byte D8 = 20;

    [ConfigEntry]
    private static readonly byte E1 = 20;

    [ConfigEntry]
    private static readonly byte E2 = 20;

    [ConfigEntry]
    private static readonly byte E3 = 20;

    [ConfigEntry]
    private static readonly byte E4 = 20;

    [ConfigEntry]
    private static readonly byte E5 = 20;

    [ConfigEntry]
    private static readonly byte E6 = 20;

    [ConfigEntry]
    private static readonly byte E7 = 20;

    [ConfigEntry]
    private static readonly byte E8 = 20;

    [HarmonyPrefix]
    [HarmonyPatch(typeof(NewTouchPanel), "SetTouchPanelSensitivity")]
    public static void SetTouchPanelSensitivityPrefix(List<byte> sensitivity)
    {
        var configType = typeof(TouchSensitivity);
        for (var i = 0; i < 34; i++)
        {
            var area = (InputManager.TouchPanelArea)i;
            var field = configType.GetField(area.ToString());
            var value = (byte)field.GetValue(null);
            sensitivity[i] = value;
        }
        MelonLogger.Msg("[TouchSensitivity] Applied");
    }
}

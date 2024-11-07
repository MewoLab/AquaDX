using System.Collections.Generic;
using HarmonyLib;
using IO;
using Manager;
using MelonLoader;

namespace AquaMai.Mods.TouchSensitivity;

public class Enable
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(NewTouchPanel), "SetTouchPanelSensitivity")]
    public static void SetTouchPanelSensitivityPrefix(List<byte> sensitivity)
    {
        var configType = AquaMai.AppConfig.TouchSensitivity.GetType();
        for (var i = 0; i < 34; i++)
        {
            var area = (InputManager.TouchPanelArea)i;
            var field = configType.GetProperty(area.ToString());
            var value = (byte)field.GetValue(AquaMai.AppConfig.TouchSensitivity);
            sensitivity[i] = value;
        }
        MelonLogger.Msg("[TouchSensitivity] Applied");
    }
}

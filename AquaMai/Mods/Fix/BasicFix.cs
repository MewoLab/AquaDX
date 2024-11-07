using System.Net;
using HarmonyLib;
using Manager;
using Net;
using UnityEngine;
using AquaMai.Attributes;

namespace AquaMai.Mods.Fix;

[ConfigSection(defaultOn: true, hideInExample: true)]
public class BasicFix
{
    [ConfigEntry]
    private readonly static bool PreventIniFileClear = true;

    [EnableIf(nameof(PreventIniFileClear))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(MAI2System.IniFile), "clear")]
    private static bool PreIniFileClear()
    {
        return false;
    }

    [ConfigEntry]
    private readonly static bool FixDebugInput = true;

    [EnableIf(nameof(FixDebugInput))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetKey")]
    private static bool GetKey(ref bool __result, KeyCode name)
    {
        __result = UnityEngine.Input.GetKey(name);
        return false;
    }

    [EnableIf(nameof(FixDebugInput))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetKeyDown")]
    private static bool GetKeyDown(ref bool __result, KeyCode name)
    {
        __result = UnityEngine.Input.GetKeyDown(name);
        return false;
    }

    [EnableIf(nameof(FixDebugInput))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetMouseButton")]
    private static bool GetMouseButton(ref bool __result, int button)
    {
        __result = UnityEngine.Input.GetMouseButton(button);
        return false;
    }

    [EnableIf(nameof(FixDebugInput))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetMouseButtonDown")]
    private static bool GetMouseButtonDown(ref bool __result, int button)
    {
        __result = UnityEngine.Input.GetMouseButtonDown(button);
        return false;
    }

    [ConfigEntry]
    private readonly static bool BypassCakeHashCheck = true;

    [EnableIf(nameof(BypassCakeHashCheck))]
    [HarmonyPostfix]
    [HarmonyPatch(typeof(NetHttpClient), MethodType.Constructor)]
    private static void OnNetHttpClientConstructor(NetHttpClient __instance)
    {
        // Bypass Cake.dll hash check
        var tInstance = Traverse.Create(__instance).Field("isTrueDll");
        if (tInstance.FieldExists())
        {
            tInstance.SetValue(true);
        }
    }

    [ConfigEntry]
    private readonly static bool RestoreCertificateValidation = true;

    [EnableIf(nameof(RestoreCertificateValidation))]
    [HarmonyPostfix]
    [HarmonyPatch(typeof(NetHttpClient), "Create")]
    private static void OnNetHttpClientCreate()
    {
        // Unset the certificate validation callback (SSL pinning) to restore the default behavior
        ServicePointManager.ServerCertificateValidationCallback = null;
    }

    [ConfigEntry]
    private readonly static bool BypassSpecialNumCheck = true;

    public static void DoCustomPatch(HarmonyLib.Harmony h)
    {
        if (BypassSpecialNumCheck)
        {
            if (typeof(GameManager).GetMethod("CalcSpecialNum") is null) return;
            h.PatchAll(typeof(CalcSpecialNumPatch));
        }
    }

    private class CalcSpecialNumPatch
    {
        [HarmonyPrefix]
        [HarmonyPatch(typeof(GameManager), "CalcSpecialNum")]
        private static bool CalcSpecialNum(ref int __result)
        {
            __result = 1024;
            return false;
        }
    }
}

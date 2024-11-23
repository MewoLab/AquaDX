using System.Net;
using HarmonyLib;
using Manager;
using Net;
using UnityEngine;
using AquaMai.Config.Attributes;
using AquaMai.Core.Attributes;

namespace AquaMai.Mods.Fix;

[ConfigSection(exampleHidden: true, defaultOn: true)]
public class Common
{
    [ConfigEntry]
    private readonly static bool preventIniFileClear = true;

    [EnableIf(nameof(preventIniFileClear))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(MAI2System.IniFile), "clear")]
    private static bool PreIniFileClear()
    {
        return false;
    }

    [ConfigEntry]
    private readonly static bool fixDebugInput = true;

    [EnableIf(nameof(fixDebugInput))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetKey")]
    private static bool GetKey(ref bool __result, KeyCode name)
    {
        __result = UnityEngine.Input.GetKey(name);
        return false;
    }

    [EnableIf(nameof(fixDebugInput))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetKeyDown")]
    private static bool GetKeyDown(ref bool __result, KeyCode name)
    {
        __result = UnityEngine.Input.GetKeyDown(name);
        return false;
    }

    [EnableIf(nameof(fixDebugInput))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetMouseButton")]
    private static bool GetMouseButton(ref bool __result, int button)
    {
        __result = UnityEngine.Input.GetMouseButton(button);
        return false;
    }

    [EnableIf(nameof(fixDebugInput))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetMouseButtonDown")]
    private static bool GetMouseButtonDown(ref bool __result, int button)
    {
        __result = UnityEngine.Input.GetMouseButtonDown(button);
        return false;
    }

    [ConfigEntry]
    private readonly static bool bypassCakeHashCheck = true;

    [EnableIf(nameof(bypassCakeHashCheck))]
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
    private readonly static bool restoreCertificateValidation = true;

    [EnableIf(nameof(restoreCertificateValidation))]
    [HarmonyPostfix]
    [HarmonyPatch(typeof(NetHttpClient), "Create")]
    private static void OnNetHttpClientCreate()
    {
        // Unset the certificate validation callback (SSL pinning) to restore the default behavior
        ServicePointManager.ServerCertificateValidationCallback = null;
    }

    [ConfigEntry]
    private readonly static bool forceNonTarget = true;

    [EnableIf(nameof(forceNonTarget))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(MAI2System.Config), "IsTarget", MethodType.Getter)]
    private static bool PreIsTarget(ref bool __result)
    {
        // Who is teaching others to set `Target = 1`?!
        __result = false;
        return false;
    }

    [ConfigEntry]
    private readonly static bool forceIgnoreError = true;

    [EnableIf(nameof(forceIgnoreError))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(MAI2System.Config), "IsIgnoreError", MethodType.Getter)]
    private static bool PreIsIgnoreError(ref bool __result)
    {
        __result = true;
        return false;
    }

    [ConfigEntry]
    private readonly static bool bypassSpecialNumCheck = true;

    public static void OnAfterPatch(HarmonyLib.Harmony h)
    {
        if (bypassSpecialNumCheck)
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

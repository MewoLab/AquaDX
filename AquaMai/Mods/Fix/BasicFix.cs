using System.Net;
using HarmonyLib;
using Manager;
using Monitor.MusicSelect.ChainList;
using Net;
using UnityEngine;

namespace AquaMai.Mods.Fix;

public class BasicFix
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(MAI2System.IniFile), "clear")]
    private static bool PreIniFileClear()
    {
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetKey")]
    private static bool GetKey(ref bool __result, KeyCode name)
    {
        __result = UnityEngine.Input.GetKey(name);
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetKeyDown")]
    private static bool GetKeyDown(ref bool __result, KeyCode name)
    {
        __result = UnityEngine.Input.GetKeyDown(name);
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetMouseButton")]
    private static bool GetMouseButton(ref bool __result, int button)
    {
        __result = UnityEngine.Input.GetMouseButton(button);
        return false;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(DebugInput), "GetMouseButtonDown")]
    private static bool GetMouseButtonDown(ref bool __result, int button)
    {
        __result = UnityEngine.Input.GetMouseButtonDown(button);
        return false;
    }

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

    [HarmonyPostfix]
    [HarmonyPatch(typeof(NetHttpClient), "Create")]
    private static void OnNetHttpClientCreate()
    {
        // Unset the certificate validation callback (SSL pinning) to restore the default behavior
        ServicePointManager.ServerCertificateValidationCallback = null;
    }

    public static void DoCustomPatch(HarmonyLib.Harmony h)
    {
        if (typeof(GameManager).GetMethod("CalcSpecialNum") is null) return;
        h.PatchAll(typeof(CalcSpecialNumPatch));
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

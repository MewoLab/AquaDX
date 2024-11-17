using System;
using System.Collections;
using System.Reflection;
using AquaMai.Core.Attributes;
using HarmonyLib;
using MelonLoader;

namespace AquaMai.Core.Helpers;

public class EnableIfHelper
{
    [HarmonyPrefix]
    [HarmonyPatch("HarmonyLib.PatchTools", "GetPatchMethod")]
    public static bool PostGetPatchMethod(ref MethodInfo __result)
    {
        if (__result != null)
        {
            var enableIf = __result.GetCustomAttribute<EnableIfAttribute>();
            if (!enableIf.ShouldEnable(__result.ReflectedType))
            {
                PrintMethodSkipMessage(__result);
                __result = null;
                return false;
            }
        }
        return true;
    }

    [HarmonyPostfix]
    [HarmonyPatch("HarmonyLib.PatchTools", "GetPatchMethods")]
    public static void PostGetPatchMethods(ref IList __result)
    {
        for (int i = 0; i < __result.Count; i++)
        {
            var harmonyMethod = Traverse.Create(__result[i]).Field("info").GetValue() as HarmonyMethod;
            var method = harmonyMethod.method;
            var enableIf = method.GetCustomAttribute<EnableIfAttribute>();
            if (enableIf != null && !enableIf.ShouldEnable(method.ReflectedType))
            {
                PrintMethodSkipMessage(method);
                __result.RemoveAt(i);
                i--;
            }
        }
    }

    public static bool ShouldSkipClass(Type type)
    {
        var enableIf = type.GetCustomAttribute<EnableIfAttribute>();
        if (enableIf != null && !enableIf.ShouldEnable(type))
        {
# if DEBUG
            MelonLogger.Warning($"Skipping class {type.FullName}");
# endif
            return true;
        }
        return false;
    }

    private static void PrintMethodSkipMessage(MethodInfo method)
    {
# if DEBUG
        MelonLogger.Warning($"Skipping method {method.ReflectedType.FullName}.{method.Name}");
# endif
    }
}

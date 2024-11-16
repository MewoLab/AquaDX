using System.Collections.Generic;
using System.Reflection;
using AquaMai.Core.Attributes;
using HarmonyLib;
using MelonLoader;

namespace AquaMai.Core.Helpers;

public class EnableIfHelper
{
    [HarmonyPostfix]
    [HarmonyPatch("HarmonyLib.PatchTools", "GetPatchMethod")]
    public static bool PostGetPatchMethod(ref MethodInfo __result)
    {
        if (__result != null)
        {
            var enableIf = __result.GetCustomAttribute<EnableIfAttribute>();
            if (!enableIf.ShouldEnable(__result.ReflectedType))
            {
                PrintSkipMessage(__result);
                __result = null;
                return false;
            }
        }
        return true;
    }

    [HarmonyPostfix]
    [HarmonyPatch("HarmonyLib.PatchTools", "GetPatchMethods")]
    public static bool PostGetPatchMethods(ref List<object> __result)
    {
        for (int i = 0; i < __result.Count; i++)
        {
            var harmonyMethod = Traverse.Create(__result[i]).Field("info").GetValue() as HarmonyMethod;
            var method = harmonyMethod.method;
            var enableIf = method.GetCustomAttribute<EnableIfAttribute>();
            if (enableIf != null && !enableIf.ShouldEnable(method.ReflectedType))
            {
                PrintSkipMessage(method);
                __result.RemoveAt(i);
                i--;
            }
        }
        return false;
    }

    private static void PrintSkipMessage(MethodInfo method)
    {
# if DEBUG
        MelonLogger.Warning($"Skipping {method.ReflectedType.FullName}.{method.Name}");
# endif
    }
}

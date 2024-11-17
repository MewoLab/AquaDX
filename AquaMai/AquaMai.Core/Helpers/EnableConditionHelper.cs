using System;
using System.Collections;
using System.Reflection;
using AquaMai.Core.Attributes;
using AquaMai.Core.Resources;
using HarmonyLib;
using MelonLoader;

namespace AquaMai.Core.Helpers;

public class EnableConditionHelper
{
    [HarmonyPrefix]
    [HarmonyPatch("HarmonyLib.PatchTools", "GetPatchMethod")]
    public static bool PostGetPatchMethod(ref MethodInfo __result)
    {
        if (__result != null)
        {
            if (ShouldSkipMethodOrClass(__result.GetCustomAttribute, __result.ReflectedType, __result.Name))
            {
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
            if (ShouldSkipMethodOrClass(method.GetCustomAttribute, method.ReflectedType, method.Name))
            {
                __result.RemoveAt(i);
                i--;
            }
        }
    }

    public static bool ShouldSkipClass(Type type)
    {
        return ShouldSkipMethodOrClass(type.GetCustomAttribute, type);
    }

    private static bool ShouldSkipMethodOrClass(Func<Type, object> getCustomAttribute, Type type, string methodName = "")
    {
        var displayName = type.FullName + (string.IsNullOrEmpty(methodName) ? "" : $".{methodName}");
        var enableIf = (EnableIfAttribute)getCustomAttribute(typeof(EnableIfAttribute));
        if (enableIf != null && !enableIf.ShouldEnable(type))
        {
# if DEBUG
            MelonLogger.Msg($"Skipping {displayName} due to EnableIf condition");
# endif
            return true;
        }
        var enableGameVersion = (EnableGameVersionAttribute)getCustomAttribute(typeof(EnableGameVersionAttribute));
        if (enableGameVersion != null && !enableGameVersion.ShouldEnable(GameInfo.GameVersion))
        {
# if DEBUG
            MelonLogger.Msg($"Skipping {displayName} due to EnableGameVersion condition");
# endif
            if (!enableGameVersion.NoWarn)
            {
                MelonLogger.Warning(string.Format(Locale.SkipIncompatiblePatch, type));
            }
            return true;
        }
        return false;
    }
}

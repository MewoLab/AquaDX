using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Reflection;
using AquaMai.Core.Attributes;
using AquaMai.Core.Helpers;
using AquaMai.Core.Resources;
using MelonLoader;
using UnityEngine;

namespace AquaMai.Core;

public class Startup
{
    private static HarmonyLib.Harmony _harmony;

    private static bool _hasErrors;

    private enum ModLifecycleMethod
    {
        // Invoked before all patches are applied, including core patches
        OnBeforeAllPatch,
        // Invoked after all patches are applied
        OnAfterAllPatch,
        // Invoked before the current patch is applied
        OnBeforePatch,
        // Invoked after the current patch is applied
        // Subclasses are treated as separate patches
        OnAfterPatch,
        // Invoked when an error occurs applying the current patch
        // Lifecycle methods' excpetions not included
        // Subclasses' error not included
        OnPatchError
    }

    private static bool ShouldEnableImplicitly(Type type)
    {
        var implicitEnableAttribute = type.GetCustomAttribute<EnableImplicitlyIf>();
        if (implicitEnableAttribute == null) return false;
        var referenceField = type.GetField(implicitEnableAttribute.MemberName, BindingFlags.Static | BindingFlags.Public | BindingFlags.NonPublic);
        var referenceProperty = type.GetProperty(implicitEnableAttribute.MemberName, BindingFlags.Static | BindingFlags.Public | BindingFlags.NonPublic);
        if (referenceField == null && referenceProperty == null)
        {
            throw new ArgumentException($"Field or property {implicitEnableAttribute.MemberName} not found in {type.FullName}");
        }
        var referenceMemberValue = referenceField != null ? referenceField.GetValue(null) : referenceProperty.GetValue(null);
        if ((bool)referenceMemberValue)
        {
            MelonLogger.Msg($"Enabled {type.FullName} implicitly");
            return true;
        }
        return false;
    }

    private static void InvokeLifecycleMethod(Type type, ModLifecycleMethod methodName)
    {
        var method = type.GetMethod(methodName.ToString(), BindingFlags.Public | BindingFlags.NonPublic | BindingFlags.Static);
        if (method == null)
        {
            return;
        }
        var parameters = method.GetParameters();
        var arguments = parameters.Select(p =>
        {
            if (p.ParameterType == typeof(HarmonyLib.Harmony)) return _harmony;
            throw new InvalidOperationException($"Unsupported parameter type {p.ParameterType} in lifecycle method {type.FullName}.{methodName}");
        }).ToArray();
        try
        {
            method.Invoke(null, arguments);
        }
        catch (TargetInvocationException e)
        {
            MelonLogger.Error($"Failed to invoke lifecycle method {type.FullName}.{methodName}: {e.InnerException}");
            _hasErrors = true;
        }
    }

    private static void CollectWantedPatches(List<Type> wantedPatches, Type type)
    {
        if (EnableConditionHelper.ShouldSkipClass(type))
        {
            return;
        }

        wantedPatches.Add(type);
        foreach (var nested in type.GetNestedTypes())
        {
            CollectWantedPatches(wantedPatches, nested);
        }
    }

    private static void ApplyPatch(Type type)
    {
        MelonLogger.Msg($"> Applying {type}");
        try
        {
            InvokeLifecycleMethod(type, ModLifecycleMethod.OnBeforePatch);
            _harmony.PatchAll(type);
            InvokeLifecycleMethod(type, ModLifecycleMethod.OnAfterPatch);
        }
        catch (Exception e)
        {
            MelonLogger.Error($"Failed to patch {type}: {e}");
            InvokeLifecycleMethod(type, ModLifecycleMethod.OnPatchError);
            _hasErrors = true;
        }
    }

    private static string ResolveLocale()
    {
        var localeConfigEntry = ConfigLoader.Config.ReflectionManager.GetEntry("General.Locale");
        var localeValue = (string)ConfigLoader.Config.GetEntryState(localeConfigEntry).Value;
        return localeValue switch
        {
            "en" => localeValue,
            "zh" => localeValue,
            _ => Application.systemLanguage switch
            {
                SystemLanguage.Chinese or SystemLanguage.ChineseSimplified or SystemLanguage.ChineseTraditional => "zh",
                SystemLanguage.English => "en",
                _ => "en"
            }
        };
    }

    public static void Initialize(Assembly modsAssembly, HarmonyLib.Harmony harmony)
    {
        MelonLogger.Msg("Loading mod settings...");

        ConfigLoader.LoadConfig(modsAssembly);
        var lang = ResolveLocale();
        ConfigLoader.SaveConfig(lang); // Re-save the config as soon as possible

        _harmony = harmony;

        // Init locale with patching C# runtime
        // https://stackoverflow.com/questions/1952638/single-assembly-multi-language-windows-forms-deployment-ilmerge-and-satellite-a
        ApplyPatch(typeof(I18nSingleAssemblyHook));
        Locale.Culture = CultureInfo.GetCultureInfo(lang); // Must be called after I18nSingleAssemblyHook patched

        // The patch list is ordered
        List<Type> wantedPatches = [];

        // Must be patched first to support [EnableIf(...)] and [EnableGameVersion(...)]
        CollectWantedPatches(wantedPatches, typeof(EnableConditionHelper));
        // Core helpers patched first
        CollectWantedPatches(wantedPatches, typeof(MessageHelper));
        CollectWantedPatches(wantedPatches, typeof(MusicDirHelper));
        CollectWantedPatches(wantedPatches, typeof(SharedInstances));
        CollectWantedPatches(wantedPatches, typeof(GuiSizes));
        CollectWantedPatches(wantedPatches, typeof(KeyListener));

        // Collect patches based on the config
        var config = ConfigLoader.Config;
        foreach (var section in config.ReflectionManager.Sections)
        {
            var reflectionType = (Config.Reflection.SystemReflectionProvider.ReflectionType)section.Type;
            var type = reflectionType.UnderlyingType;
            if (!config.GetSectionState(section).Enabled && !ShouldEnableImplicitly(type)) continue;
            CollectWantedPatches(wantedPatches, type);
        }

        foreach (var type in wantedPatches)
        {
            InvokeLifecycleMethod(type, ModLifecycleMethod.OnBeforeAllPatch);
        }
        foreach (var type in wantedPatches)
        {
            ApplyPatch(type);
        }
        foreach (var type in wantedPatches)
        {
            InvokeLifecycleMethod(type, ModLifecycleMethod.OnAfterAllPatch);
        }

        if (_hasErrors)
        {
            MelonLogger.Warning("========================================================================!!!\n" + Locale.LoadError);
            MelonLogger.Warning("===========================================================================");
        }

# if CI
        MelonLogger.Warning(Locale.CiBuildAlertTitle);
        MelonLogger.Warning(Locale.CiBuildAlertContent);
# endif

        MelonLogger.Msg(Locale.Loaded);
    }

    public static void OnGUI()
    {
        GuiSizes.SetupStyles();
    }
}

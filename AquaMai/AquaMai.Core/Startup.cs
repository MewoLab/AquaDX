using System;
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

    private static void InvokeLifecycleMethod(Type type, string methodName)
    {
        var method = type.GetMethod(methodName, BindingFlags.Public | BindingFlags.NonPublic | BindingFlags.Static);
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
        method.Invoke(null, arguments);
    }

    private static void Patch(Type type, bool isNested = false)
    {
        if (EnableIfHelper.ShouldSkipClass(type))
        {
            return;
        }
        var versionAttr = type.GetCustomAttribute<GameVersionAttribute>();
        var compatible = true;
        if (versionAttr != null)
        {
            if (versionAttr.MinVersion > 0 && versionAttr.MinVersion > GameInfo.GameVersion) compatible = false;
            if (versionAttr.MaxVersion > 0 && versionAttr.MaxVersion < GameInfo.GameVersion) compatible = false;
        }

        if (!compatible)
        {
            if (!isNested)
            {
                MelonLogger.Warning(string.Format(Locale.SkipIncompatiblePatch, type));
            }

            return;
        }

        MelonLogger.Msg($"> Patching {type}");
        try
        {
            InvokeLifecycleMethod(type, "OnBeforePatch");
            _harmony.PatchAll(type);
            foreach (var nested in type.GetNestedTypes())
            {
                Patch(nested, true);
            }
            InvokeLifecycleMethod(type, "OnAfterPatch");
        }
        catch (Exception e)
        {
            MelonLogger.Error($"Failed to patch {type}: {e}");
            InvokeLifecycleMethod(type, "OnPatchError");
            _hasErrors = true;
        }
    }

    private static void InitLocale()
    {
        var localeConfigEntry = ConfigLoader.Config.ReflectionManager.GetEntry("General.Locale");
        var localeValue = (string)ConfigLoader.Config.GetEntryState(localeConfigEntry).Value;
        if (!string.IsNullOrEmpty(localeValue))
        {
            Locale.Culture = CultureInfo.GetCultureInfo(localeValue);
            return;
        }

        Locale.Culture = Application.systemLanguage switch
        {
            SystemLanguage.Chinese or SystemLanguage.ChineseSimplified or SystemLanguage.ChineseTraditional => CultureInfo.GetCultureInfo("zh"),
            SystemLanguage.English => CultureInfo.GetCultureInfo("en"),
            _ => CultureInfo.InvariantCulture
        };
    }

    public static void Initialize(Assembly modsAssembly, HarmonyLib.Harmony harmony)
    {
        MelonLogger.Msg("Loading mod settings...");

        ConfigLoader.LoadConfig(modsAssembly);

        _harmony = harmony;

        // Init locale with patching C# runtime
        // https://stackoverflow.com/questions/1952638/single-assembly-multi-language-windows-forms-deployment-ilmerge-and-satellite-a
        Patch(typeof(I18nSingleAssemblyHook));
        InitLocale(); // Must be called after I18nSingleAssemblyHook patched and config loaded

        // Helpers that does not have side effects
        // These don't need to be configurable
        Patch(typeof(MessageHelper));
        Patch(typeof(MusicDirHelper));
        Patch(typeof(SharedInstances));
        Patch(typeof(GuiSizes));

        Patch(typeof(EnableIfHelper));

        // Apply patches based on the settings
        var config = ConfigLoader.Config;
        foreach (var section in config.ReflectionManager.Sections)
        {
            if (!config.GetSectionState(section).Enabled) continue;
            var reflectionType = (Config.Reflection.SystemReflectionProvider.ReflectionType)section.Type;
            Patch(reflectionType.UnderlyingType);
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

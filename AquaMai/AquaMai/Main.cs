using System;
using System.Globalization;
using System.Reflection;
using System.Runtime.InteropServices;
using AquaMai.Core.Attributes;
using AquaMai.Core.Helpers;
using AquaMai.Core.Resources;
using MelonLoader;
using UnityEngine;

namespace AquaMai
{
    public static class BuildInfo
    {
        public const string Name = "AquaMai";
        public const string Description = "Mod for Sinmai";
        public const string Author = "Aza";
        public const string Company = null;
        public const string Version = "1.2.2";
        public const string DownloadLink = null;
    }

    public class AquaMai : MelonMod
    {
        public const string AQUAMAI_SAY = """
                                          如果你在 dnSpy / ILSpy 里看到了这行字，请从 resources 中解包 DLLs。
                                          If you see this line in dnSpy / ILSpy, please unpack the DLLs from resources.
                                          """;

        private static bool _hasErrors;

        private void Patch(Type type, bool isNested = false)
        {
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
                HarmonyInstance.PatchAll(type);
                foreach (var nested in type.GetNestedTypes())
                {
                    Patch(nested, true);
                }

                // TODO: DoCustomPatch => IOnPatch
                var customMethod = type.GetMethod("DoCustomPatch", BindingFlags.Public | BindingFlags.Static);
                customMethod?.Invoke(null, [HarmonyInstance]);
            }
            catch (Exception e)
            {
                MelonLogger.Error($"Failed to patch {type}: {e}");
                _hasErrors = true;
            }
        }

        /**
         * Apply patches using reflection, based on the settings
         */
        private void ApplyPatches()
        {
            var config = ConfigLoader.Config;
            foreach (var section in config.reflectionManager.Sections)
            {
                if (!config.GetSectionState(section).Enabled) continue;
                var reflectionType = (Config.Reflection.SystemReflectionProvider.ReflectionType)section.Type;
                Patch(reflectionType.UnderlyingType);
            }
        }

        [DllImport("kernel32.dll", SetLastError = true)]
        private static extern bool SetConsoleOutputCP(uint wCodePageID);

        private static void InitLocale()
        {
            if (!string.IsNullOrEmpty(Mods.General.Locale))
            {
                Locale.Culture = CultureInfo.GetCultureInfo(Mods.General.Locale);
                return;
            }

            Locale.Culture = Application.systemLanguage switch
            {
                SystemLanguage.Chinese or SystemLanguage.ChineseSimplified or SystemLanguage.ChineseTraditional => CultureInfo.GetCultureInfo("zh"),
                SystemLanguage.English => CultureInfo.GetCultureInfo("en"),
                _ => CultureInfo.InvariantCulture
            };
        }

        public override void OnInitializeMelon()
        {
            // Prevent Chinese characters from being garbled
            SetConsoleOutputCP(65001);

            AssemblyLoader.LoadAssemblies();

            MelonLogger.Msg("Loading mod settings...");

            ConfigLoader.LoadConfig();

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
            ApplyPatches();

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

        public override void OnGUI()
        {
            GuiSizes.SetupStyles();
            base.OnGUI();
        }
    }
}

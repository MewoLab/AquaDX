using System.Collections.Generic;
using System.IO;
using System.Reflection;
using MelonLoader;
using AquaMai.Config;
using AquaMai.Config.Interfaces;
using AquaMai.Config.Migration;

namespace AquaMai.Core;

public static class ConfigLoader
{
    private static string ConfigFile => "AquaMai.toml";
    private static string ConfigExampleFile(string lang) => $"AquaMai.{lang}.toml";
    private static string OldConfigFile(string version) => $"AquaMai.toml.old-v{version}.";

    private static Config.Config config;

    public static Config.Config Config => config;

    public static bool LoadConfig(Assembly modsAssembly)
    {
        Utility.LogFunction = MelonLogger.Msg;

        config = new(
            new Config.Reflection.ReflectionManager(
                new Config.Reflection.SystemReflectionProvider(modsAssembly)));

        if (!File.Exists(ConfigFile))
        {
            var examples = GenerateExamples();
            foreach (var (lang, example) in examples)
            {
                var filename = ConfigExampleFile(lang);
                File.WriteAllText(filename, example);
            }
            MelonLogger.Error("======================================!!!");
            MelonLogger.Error("AquaMai.toml not found! Please create it.");
            MelonLogger.Error("找不到配置文件 AquaMai.toml！请创建。");
            MelonLogger.Error("Example copied to AquaMai.en.toml");
            MelonLogger.Error("示例已复制到 AquaMai.zh.toml");
            MelonLogger.Error("=========================================");
            return false;
        }

        var configText = File.ReadAllText(ConfigFile);
        var configView = new ConfigView(configText);
        var configVersion = ConfigMigrationManager.Instance.GetVersion(configView);
        if (configVersion != ConfigMigrationManager.Instance.latestVersion)
        {
            File.WriteAllText(OldConfigFile(configVersion), configText);
            configView = (ConfigView)ConfigMigrationManager.Instance.Migrate(configView);
        }

        // Read AquaMai.toml to load settings
        ConfigParser.Instance.Parse(config, configView);

        return true;
    }

    public static void SaveConfig(string lang)
    {
        File.WriteAllText(ConfigFile, SerailizeCurrentConfig(lang));
    }

    private static string SerailizeCurrentConfig(string lang) =>
        new ConfigSerializer(new IConfigSerializer.Options()
        {
            Lang = lang,
            IncludeBanner = true,
            OverrideLocaleValue = true
        }).Serialize(config);

    private static IDictionary<string, string> GenerateExamples()
    {
        var examples = new Dictionary<string, string>();
        foreach (var lang in (string[]) ["en", "zh"])
        {
            examples[lang] = SerailizeCurrentConfig(lang);
        }
        return examples;
    }
}

using System.Collections.Generic;
using System.IO;
using System.Reflection;
using MelonLoader;
using AquaMai.Config;
using AquaMai.Config.Interfaces;
using AquaMai.Config.Migration;

namespace AquaMai;

public static class ConfigLoader
{
    private static string ConfigFile => "AquaMai.toml";
    private static string ConfigExampleFile(string lang) => $"AquaMai.{lang}.toml";

    private static Config.Config config;

    public static Config.Config Config => config;

    public static void LoadConfig(Assembly modsAssembly)
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
            return;
        }

        var configView = new ConfigView(File.ReadAllText(ConfigFile));
        configView = (ConfigView)ConfigMigrationManager.Instance.Migrate(configView);

        // Read AquaMai.toml to load settings
        ConfigParser.Instance.Parse(config, configView);
    }

    public static IDictionary<string, string> GenerateExamples()
    {
        var examples = new Dictionary<string, string>();
        foreach (var lang in (string[]) ["en", "zh"])
        {
            var configSerializer = new ConfigSerializer(new IConfigSerializer.Options()
            {
                Lang = lang,
                IncludeBanner = true
            });
            examples[lang] = configSerializer.Serialize(config);
        }
        return examples;
    }
}

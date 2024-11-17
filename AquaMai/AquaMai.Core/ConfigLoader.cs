using System.Collections.Generic;
using System.IO;
using System.Reflection;
using AquaMai.Config;
using AquaMai.Config.Interfaces;
using MelonLoader;

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

        // Read AquaMai.toml to load settings
        new ConfigParser().Parse(config, File.ReadAllText(ConfigFile));
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

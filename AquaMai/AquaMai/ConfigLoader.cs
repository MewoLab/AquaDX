using System.Collections.Generic;
using System.IO;
using AquaMai.Config;
using MelonLoader;

namespace AquaMai;

public static class ConfigLoader
{
    private static string ConfigFile => "AquaMai.toml";
    private static string ConfigExampleFile(string lang) => $"AquaMai.{lang}.toml";

    private static Config.Config config = new(
        new Config.Reflection.ReflectionManager(
            new Config.Reflection.SystemReflectionProvider(
                AssemblyLoader.GetAssembly(AssemblyLoader.AssemblyName.Mods))));

    public static Config.Config Config => config;

    public static void LoadConfig()
    {
        Utility.LogFunction = MelonLogger.Msg;

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
        ConfigParser.Parse(config, File.ReadAllText(ConfigFile));
    }

    public static IDictionary<string, string> GenerateExamples()
    {
        var examples = new Dictionary<string, string>();
        foreach (var lang in (string[]) ["en", "zh"])
        {
            examples[lang] = ConfigSerializer.Serialize(config, new ConfigSerializer.Options()
            {
                Lang = lang,
                IncludeBanner = true
            });
        }
        return examples;
    }
}

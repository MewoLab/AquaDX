using System;
using System.Reflection;
using MelonLoader;

namespace AquaMai;

public static class ConfigLoader
{
    private static void DoLoadConfig()
    {
        Config.Reflection.ReflectionManager.Load(new Config.Reflection.SystemReflectionProvider(typeof(AquaMai).Assembly));

        // Check if AquaMai.toml exists
        if (!Config.ConfigLoader.ConfigFileExists())
        {
            Config.ConfigSerializer.WriteExamples();
            MelonLogger.Error("======================================!!!");
            MelonLogger.Error("AquaMai.toml not found! Please create it.");
            MelonLogger.Error("找不到配置文件 AquaMai.toml！请创建。");
            MelonLogger.Error("Example copied to AquaMai.en.toml");
            MelonLogger.Error("示例已复制到 AquaMai.zh.toml");
            MelonLogger.Error("=========================================");
            return;
        }

        // Read AquaMai.toml to load settings
        Config.ConfigLoader.Load();
    }

    private static void ResolveConfigAssembly()
    {
        var executingAssembly = Assembly.GetExecutingAssembly();
        using var stream = executingAssembly.GetManifestResourceStream("AquaMai.Config.dll");
        var assemblyRawBytes = new byte[stream.Length];
        stream.Read(assemblyRawBytes, 0, assemblyRawBytes.Length);
        AppDomain.CurrentDomain.Load(assemblyRawBytes);
    }

    public static void LoadConfig()
    {
        ResolveConfigAssembly();
        DoLoadConfig();
    }
}

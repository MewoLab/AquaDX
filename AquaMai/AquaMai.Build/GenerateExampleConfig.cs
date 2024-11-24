using System;
using System.IO;
using AquaMai.Config.Interfaces;
using AquaMai.Config.HeadlessLoader;
using Microsoft.Build.Framework;
using Microsoft.Build.Utilities;

public class GenerateExampleConfig : Task
{
    [Required]
    public string DllPath { get; set; }

    [Required]
    public string OutputPath { get; set; }

    public override bool Execute()
    {
        try
        {
            var configInterface = HeadlessConfigLoader.LoadFromPacked(DllPath);
            var config = configInterface.CreateConfig();
            foreach (var lang in (string[]) ["en", "zh"])
            {
                var configSerializer = configInterface.CreateConfigSerializer(new IConfigSerializer.Options()
                {
                    Lang = lang,
                    IncludeBanner = true,
                    OverrideLocaleValue = true
                });
                var example = configSerializer.Serialize(config);
                File.WriteAllText(Path.Combine(OutputPath, $"AquaMai.{lang}.toml"), example);
            }

            return true;
        }
        catch (Exception e)
        {
            Log.LogErrorFromException(e, true);
            return false;
        }
    }
}

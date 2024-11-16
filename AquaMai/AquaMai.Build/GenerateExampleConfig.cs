using System;
using System.IO;
using AquaMai.Config;
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
            var config = HeadlessConfigLoader.LoadFromPacked(DllPath);
            foreach (var lang in (string[]) ["en", "zh"])
            {
                var example = ConfigSerializer.Serialize(config, new ConfigSerializer.Options()
                {
                    Lang = lang,
                    IncludeBanner = true
                });
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

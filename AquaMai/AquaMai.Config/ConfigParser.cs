using System;
using Tomlet.Models;
using AquaMai.Config.Interfaces;
using AquaMai.Config.Reflection;
using AquaMai.Config.Migration;
using System.Linq;

namespace AquaMai.Config;

public class ConfigParser : IConfigParser
{
    public readonly static ConfigParser Instance = new();

    private readonly static string[] supressUnrecognizedConfigPaths = ["Version"];
    private readonly static string[] supressUnrecognizedConfigPathSuffixes = [
        ".Disabled", // For section enable state.
        ".Disable", // For section enable state, but the wrong key, warn later.
        ".Enabled", // For section enable state, but the wrong key, warn later.
        ".Enable", // For section enable state, but the wrong key, warn later.
    ];

    private ConfigParser()
    {}

    public void Parse(IConfig config, string tomlString)
    {
        var configView = new ConfigView(tomlString);
        Parse(config, configView);
    }

    public void Parse(IConfig config, IConfigView configView)
    {
        var configVersion = ConfigMigrationManager.Instance.GetVersion(configView);
        if (configVersion != ConfigMigrationManager.Instance.latestVersion)
        {
            throw new InvalidOperationException($"Config version mismatch: expected {ConfigMigrationManager.Instance.latestVersion}, got {configVersion}");
        }
        Hydrate((Config)config, ((ConfigView)configView).root, "");
    }

    private static void Hydrate(Config config, TomlValue value, string path)
    {
        if (config.ReflectionManager.TryGetSection(path, out var section))
        {
            ParseSectionEnableState(config, (ReflectionManager.Section)section, value, path);
        }

        if (value is TomlTable table)
        {
            bool isLeaf = true;
            foreach (var subKey in table.Keys)
            {
                var subValue = table.GetValue(subKey);
                var subPath = path == "" ? subKey : $"{path}.{subKey}";
                if (subValue is TomlTable)
                {
                    isLeaf = false;
                }
                Hydrate(config, subValue, subPath);
            }
            // A leaf dictionary, which has no child dictionaries, must be a section.
            if (isLeaf && section == null)
            {
                Utility.Log($"Unrecognized config section: {path}");
            }
        }
        else
        {
            // It's an config entry value (or a primitive type for enabling a section).
            if (!config.ReflectionManager.ContainsSection(path) &&
                !config.ReflectionManager.ContainsEntry(path) &&
                !supressUnrecognizedConfigPaths.Any(s => path.Equals(s, StringComparison.OrdinalIgnoreCase)) &&
                !supressUnrecognizedConfigPathSuffixes.Any(suffix => path.EndsWith(suffix, StringComparison.OrdinalIgnoreCase)))
            {
                Utility.Log($"Unrecognized config entry: {path}");
                return;
            }

            if (config.ReflectionManager.TryGetEntry(path, out var entry))
            {
                try
                {
                    var parsedValue = Utility.ParseTomlValue(entry.Field.FieldType, value);
                    config.SetEntryValue(entry, parsedValue);
                }
                catch (Exception e)
                {
                    Utility.Log($"Error hydrating config ({path} = {value.StringValue}): {e.Message}");
                }
            }
        }
    }

    public static void ParseSectionEnableState(
        Config config,
        ReflectionManager.Section section,
        TomlValue value,
        string path)
    {
        if (value is TomlTable table)
        {
            foreach (var unexpectedKey in (string[]) ["Enable", "Enabled", "Disable"])
            {
                if (table.ContainsKey(unexpectedKey))
                {
                    Utility.Log($"Unexpected key \"{unexpectedKey}\" for enable status under \"{path}\". Only \"Disabled\" is parsed.");
                }
            }

            if (table.TryGetValue("Disabled", out var disableValue) && !section.Attribute.AlwaysEnabled)
            {
                var disabled = Utility.IsTruty(disableValue, path + ".Disabled");
                config.SetSectionEnabled(section, !disabled);
            }
            else
            {
                config.SetSectionEnabled(section, true);
            }
        }
        else
        {
            config.SetSectionEnabled(section, Utility.IsTruty(value, path));
        }
    }
}

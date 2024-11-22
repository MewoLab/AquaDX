using System.Reflection;
using System;
using Tomlet.Models;
using AquaMai.Config.Interfaces;
using AquaMai.Config.Reflection;
using AquaMail.Config;
using AquaMai.Config.Migration;

namespace AquaMai.Config;

public class ConfigParser : IConfigParser
{
    public readonly static ConfigParser Instance = new();

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
            foreach (var subKey in table.Keys)
            {
                var subValue = table.GetValue(subKey);
                var subPath = path == "" ? subKey : $"{path}.{subKey}";
                Hydrate(config, subValue, subPath);
            }
        }
        else
        {
            // It's an config entry value (or a primitive type for enabling a section).
            if (!config.ReflectionManager.ContainsSection(path) && !config.ReflectionManager.ContainsEntry(path))
            {
                Utility.Log($"Unrecognized config entry: {path}");
                return;
            }

            if (config.ReflectionManager.TryGetEntry(path, out var entry))
            {
                try
                {
                    var parsedValue = ParseValue(entry.Field.FieldType, value);
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
            foreach (var unexpectedKey in (string[]) ["Enable", "Enabled", "Disabled"])
            {
                if (table.ContainsKey(unexpectedKey))
                {
                    Utility.Log($"Unexpected key \"{unexpectedKey}\" for enable status under \"{path}\". Only \"Disable\" is parsed.");
                }
            }

            if (table.TryGetValue("Disable", out var disableValue))
            {
                var disabled = Utility.IsTruty(disableValue, path + ".Disable");
                config.SetSectionEnabled(section, !disabled);
            }
        }
        else
        {
            config.SetSectionEnabled(section, Utility.IsTruty(value, path));
        }
    }

    private static object ParseValue(Type type, TomlValue value)
    {
        if (type == typeof(bool))
        {
            return Utility.IsTruty(value);
        }
        else if (Utility.IsNumberType(type))
        {
            if (TryGetTomlNumberObject(value, out var numberObject))
            {
                return Convert.ChangeType(numberObject, type);
            }
            else
            {
                throw new InvalidCastException($"Non-number TOML type: {value.GetType().Name}");
            }
        }
        else if (type == typeof(string))
        {
            if (value is TomlString @string)
            {
                return @string.Value;
            }
            else
            {
                throw new InvalidCastException($"Non-string TOML type: {value.GetType().Name}");
            }
        }
        else if (type.IsEnum)
        {
            if (value is TomlString @string)
            {
                try
                {
                    return Enum.Parse(type, @string.Value);
                }
                catch
                {
                    throw new InvalidCastException($"Invalid enum {type.FullName} value: {@string.SerializedValue}");
                }
            }
            else if (value is TomlLong @long)
            {
                if (Enum.IsDefined(type, @long.Value))
                {
                    try
                    {
                        return Enum.ToObject(type, @long.Value);
                    }
                    catch
                    {}
                }
                throw new InvalidCastException($"Invalid enum {type.FullName} value: {@long.Value}");
            }
            else
            {
                throw new InvalidCastException($"Non-enum TOML type: {value.GetType().Name}");
            }
        }
        else
        {
            var currentMethod = MethodBase.GetCurrentMethod();
            throw new NotImplementedException($"Unsupported config entry type: {type.FullName}. Please implement in {currentMethod.DeclaringType.FullName}.{currentMethod.Name}");
        }
    }

    private static bool TryGetTomlNumberObject(TomlValue value, out object numberObject)
    {
        if (value is TomlLong @long)
        {
            numberObject = @long.Value;
            return true;
        }
        else if (value is TomlDouble @double)
        {
            numberObject = @double.Value;
            return true;
        }
        else
        {
            numberObject = null;
            return false;
        }
    }
}

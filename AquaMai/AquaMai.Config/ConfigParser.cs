using System.Reflection;
using System;
using Tomlet.Models;
using Tomlet;
using AquaMai.Config.Reflection;

namespace AquaMai.Config;

public static class ConfigParser
{
    public static void Parse(Config config, string tomlString)
    {
        Hydrate(config, new TomlParser().Parse(tomlString), "");
    }

    private static void Hydrate(Config config, TomlValue value, string path)
    {
        if (config.reflectionManager.TryGetSection(path, out var section))
        {
            ParseSectionEnableState(config, section, value, path);
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
            if (!config.reflectionManager.ContainsSection(path) && !config.reflectionManager.ContainsEntry(path))
            {
                Utility.Log($"Unrecognized config entry: {path}");
                return;
            }

            if (config.reflectionManager.TryGetEntry(path, out var entry))
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

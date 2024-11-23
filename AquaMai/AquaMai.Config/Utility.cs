using System;
using System.Reflection;
using Tomlet.Models;

namespace AquaMai.Config;

public static class Utility
{
    public static Action<string> LogFunction = Console.WriteLine;

    public static bool IsTruty(TomlValue value, string path = null)
    {
        return value switch
        {
            TomlBoolean boolean => boolean.Value,
            TomlLong @long => @long.Value != 0,
            _ => throw new ArgumentException(
                path == null
                    ? $"Non-boolish TOML type {value.GetType().Name} value: {value}"
                    : $"When parsing {path}, got non-boolish TOML type {value.GetType().Name} value: {value}")
        };
    }

    public static bool IsIntegerType(Type type)
    {
        return type == typeof(sbyte) || type == typeof(short) || type == typeof(int) || type == typeof(long)
            || type == typeof(byte) || type == typeof(ushort) || type == typeof(uint) || type == typeof(ulong);
    }

    public static bool IsFloatType(Type type)
    {
        return type == typeof(float) || type == typeof(double);
    }

    public static bool IsNumberType(Type type)
    {
        return IsIntegerType(type) || IsFloatType(type);
    }

    public static T ParseTomlValue<T>(TomlValue value)
    {
        return (T)ParseTomlValue(typeof(T), value);
    }

    public static object ParseTomlValue(Type type, TomlValue value)
    {
        if (type == typeof(bool))
        {
            return IsTruty(value);
        }
        else if (IsNumberType(type))
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

    public static bool TomlTryGetValueCaseInsensitive(TomlTable table, string key, out TomlValue value)
    {
        // Prefer exact match
        if (table.TryGetValue(key, out value))
        {
            return true;
        }
        // Fallback to case-insensitive match
        foreach (var kvp in table)
        {
            if (string.Equals(kvp.Key, key, StringComparison.OrdinalIgnoreCase))
            {
                value = kvp.Value;
                return true;
            }
        }
        value = null;
        return false;
    }

    public static bool TomlContainsKeyCaseInsensitive(TomlTable table, string key)
    {
        // Prefer exact match
        if (table.ContainsKey(key))
        {
            return true;
        }
        // Fallback to case-insensitive match
        foreach (var kvp in table)
        {
            if (string.Equals(kvp.Key, key, StringComparison.OrdinalIgnoreCase))
            {
                return true;
            }
        }
        return false;
    }

    public static string ToPascalCase(string str)
    {
        return str.Length switch
        {
            0 => str,
            1 => char.ToUpperInvariant(str[0]).ToString(),
            _ => char.ToUpperInvariant(str[0]) + str.Substring(1)
        };
    }

    // We can test the configuration related code without loading the mod into the game.
    public static void Log(string message)
    {
        LogFunction(message);
    }
}

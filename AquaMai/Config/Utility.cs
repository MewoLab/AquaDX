using System;
using System.Linq;
using Tomlet.Models;

namespace AquaMai.Config;

public static class Utility
{
    public static readonly bool isRunningInUnity = AppDomain.CurrentDomain.GetAssemblies().Any(assembly => assembly.FullName.Contains("UnityEngine"));

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

    // We can test the configuration related code without loading the mod into the game.
    public static void Log(string message)
    {
        if (isRunningInUnity)
        {
            MelonLoader.MelonLogger.Msg(message);
        }
        else
        {
            System.Console.WriteLine(message);
        }
    }
}

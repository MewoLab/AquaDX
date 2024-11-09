using System.Reflection;
using MAI2System;

namespace AquaMai.Helpers;

public class GameInfo
{
    public static uint GameVersion { get; } = GetGameVersion();

    private static uint GetGameVersion()
    {
        return (uint)typeof(ConstParameter).GetField("NowGameVersion", BindingFlags.Public | BindingFlags.Static | BindingFlags.FlattenHierarchy).GetValue(null);
    }

    public static string GameId { get; } = GetGameId();

    private static string GetGameId()
    {
        return typeof(ConstParameter).GetField("GameIDStr", BindingFlags.Public | BindingFlags.Static | BindingFlags.FlattenHierarchy).GetValue(null) as string;
    }
}

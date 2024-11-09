using System;
using System.Diagnostics;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Process;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "Triggers for executing commands at certain events",
    zh: "在一定时机执行命令的触发器")]
public class Triggers
{
    [ConfigEntry(
        en: "Execute some command on game idle",
        zh: """
            在游戏闲置的时候执行指定的命令脚本
            比如说可以在游戏闲置是降低显示器的亮度
            """)]
    private static readonly string ExecOnIdle = "";

    [ConfigEntry(
        en: "Execute some command on game start",
        zh: "在玩家登录的时候执行指定的命令脚本")]
    private static readonly string ExecOnEntry = "";

    [HarmonyPrefix]
    [HarmonyPatch(typeof(AdvertiseProcess), "OnStart")]
    public static void AdvertiseProcessPreStart()
    {
        if (!string.IsNullOrWhiteSpace(ExecOnIdle))
        {
            Exec(ExecOnIdle);
        }
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(EntryProcess), "OnStart")]
    public static void EntryProcessPreStart()
    {
        if (!string.IsNullOrWhiteSpace(ExecOnEntry))
        {
            Exec(ExecOnEntry);
        }
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(MusicSelectProcess), "OnStart")]
    public static void MusicSelectProcessPreStart()
    {
        if (!string.IsNullOrWhiteSpace(ExecOnEntry))
        {
            Exec(ExecOnEntry);
        }
    }

    private static void Exec(string command)
    {
        var process = new System.Diagnostics.Process();
        process.StartInfo.FileName = "cmd.exe";
        process.StartInfo.Arguments = "/c " + command;
        process.StartInfo.UseShellExecute = true;
        process.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
        process.StartInfo.WorkingDirectory = Environment.CurrentDirectory;

        process.Start();
    }
}

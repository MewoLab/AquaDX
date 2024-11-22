using System.Diagnostics;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Process;

namespace AquaMai.Mods.GameSystem;

[ConfigSection(
    en: "Remove useless 2s delays to speed up the game boot process",
    zh: """
        在自检界面，每个屏幕结束的时候都会等两秒才进入下一个屏幕，很浪费时间
        开了这个选项之后就不会等了
        """)]
public class RemoveStartupDelay
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(PowerOnProcess), "OnStart")]
    public static void PrePowerOnStart(ref float ____waitTime)
    {
        ____waitTime = 0f;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(StartupProcess), "OnUpdate")]
    public static void PreStartupUpdate(byte ____state, ref Stopwatch ___timer)
    {
        if (____state == 8)
        {
            Traverse.Create(___timer).Field("elapsed").SetValue(2 * 10000000L);
        }
    }
}

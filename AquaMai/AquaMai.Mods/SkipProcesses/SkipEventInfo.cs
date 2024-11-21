using System.Collections.Generic;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Process;
using Process.Information;

namespace AquaMai.Mods.SkipProcesses;

[ConfigSection(
    en: "Skip possible prompts like \"New area discovered\", \"New songs added\", \"There are events\" during game login/registration",
    zh: "跳过登录 / 注册游戏时候可能的 “发现了新的区域哟” “乐曲增加” “有活动哟” 之类的提示")]
public class SkipEventInfo
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(InformationProcess), "OnStart")]
    public static void InformationProcessPostStart(ref uint ____state)
    {
        ____state = 3;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(RegionalSelectProcess), "OnStart")]
    public static void RegionalSelectProcessPreStart(ref Queue<int>[] ____discoverList)
    {
        ____discoverList = new Queue<int>[] { new Queue<int>(), new Queue<int>() };
    }
}

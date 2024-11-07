using System.Collections.Generic;
using HarmonyLib;
using Process;
using Process.Information;

namespace AquaMai.Mods.TimeSaving
{
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
}

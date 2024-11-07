using System.Diagnostics;
using HarmonyLib;
using Process;

namespace AquaMai.Mods.TimeSaving
{
    public class ImproveLoadSpeed
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
}

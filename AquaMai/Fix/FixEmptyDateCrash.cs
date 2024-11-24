using System;
using System.Globalization;
using HarmonyLib;
using MelonLoader;
using Manager;

namespace AquaMai.Fix
{
    /*
     * Fix a specific crash caused by the system using an incompatible date format.
     * This leads to the game trying to parse an empty date.
     */
    public class FixEmptyDateCrash
    {
        private static readonly CultureInfo JapaneseCulture = new CultureInfo("ja-JP");

        /**
         * Replace the return value to DateTime.MinValue if the input string is empty or null.
         */
        [HarmonyPrefix]
        [HarmonyPatch(typeof(DateTime), "Parse", typeof(string))]
        public static bool FixEmptyDate(ref DateTime __result, string s)
        {
            if (string.IsNullOrWhiteSpace(s))
            {
                __result = TimeManager.GetNowTime();
                MelonLogger.Msg($"[FixEmptyDateCrash] Empty date string, defaulting to {__result.ToString("yyyy/MM/dd HH:mm:ss", JapaneseCulture)}");
                return false;
            }

            return true;
        }
    }
}

using HarmonyLib;

namespace AquaMai.Mods.UX
{
    public class CustomVersionString
    {
        /*
         * Patch displayVersionString Property Getter
         */
        [HarmonyPrefix]
        [HarmonyPatch(typeof(MAI2System.Config), "displayVersionString", MethodType.Getter)]
        public static bool GetDisplayVersionString(ref string __result)
        {
            if (string.IsNullOrEmpty(AquaMai.AppConfig.UX.CustomVersionString))
            {
                return true;
            }

            __result = AquaMai.AppConfig.UX.CustomVersionString;
            // Return false to block the original method
            return false;
        }
    }
}

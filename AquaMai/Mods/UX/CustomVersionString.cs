using AquaMai.Config.Attributes;
using HarmonyLib;

[ConfigSection(
    en: "Set the version string displayed at the top-right corner of the screen",
    zh: "把右上角的版本更改为自定义文本")]
public class CustomVersionString
{
    [ConfigEntry]
    private static readonly string VersionString = "";

    /*
     * Patch displayVersionString Property Getter
     */
    [HarmonyPrefix]
    [HarmonyPatch(typeof(MAI2System.Config), "displayVersionString", MethodType.Getter)]
    public static bool GetDisplayVersionString(ref string __result)
    {
        if (string.IsNullOrEmpty(VersionString))
        {
            return true;
        }

        __result = VersionString;
        // Return false to block the original method
        return false;
    }
}

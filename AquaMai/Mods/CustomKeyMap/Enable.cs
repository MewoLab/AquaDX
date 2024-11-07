using DB;
using HarmonyLib;

namespace AquaMai.Mods.CustomKeyMap;

public class Enable
{
    [HarmonyPatch(typeof(JvsButtonTableRecord), MethodType.Constructor, typeof(int), typeof(string), typeof(string), typeof(int), typeof(string), typeof(int), typeof(int), typeof(int))]
    [HarmonyPostfix]
    public static void JvsButtonTableRecordConstructor(JvsButtonTableRecord __instance, string Name)
    {
        var prop = (DB.KeyCodeID)typeof(Config).GetProperty(Name).GetValue(AquaMai.AppConfig.CustomKeyMap);
        __instance.SubstituteKey = prop;
    }
}

using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;

namespace AquaMai.Mods.Tweaks;

[ConfigSection(
    en: "Prevent gray network caused by mistakenly thinking it's an AimeDB server issue",
    zh: "防止因错误认为 AimeDB 服务器问题引起的灰网，建议开启")]
public class IgnoreAimeServerError
{
    [HarmonyPatch(typeof(OperationManager), "IsAliveAimeServer", MethodType.Getter)]
    [HarmonyPrefix]
    public static bool Prefix(ref bool __result)
    {
        __result = true;
        return false;
    }
}

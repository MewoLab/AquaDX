using HarmonyLib;
using Manager;

namespace AquaMai.Mods.Fix;

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

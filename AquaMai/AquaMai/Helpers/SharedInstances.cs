using HarmonyLib;
using Main;
using Process;

namespace AquaMai.Helpers;

public class SharedInstances
{
    public static ProcessDataContainer ProcessDataContainer { get; private set; }
    public static GameMainObject GameMainObject { get; private set; }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(ProcessDataContainer), MethodType.Constructor)]
    public static void OnCreateProcessDataContainer(ProcessDataContainer __instance)
    {
        ProcessDataContainer = __instance;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(GameMainObject), "Awake")]
    public static void OnCreateGameMainObject(GameMainObject __instance)
    {
        GameMainObject = __instance;
    }
}

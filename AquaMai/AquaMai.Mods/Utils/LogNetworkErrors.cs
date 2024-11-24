using System.Diagnostics;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;
using Manager.Operation;
using MelonLoader;
using Net.Packet;

namespace AquaMai.Mods.Utils;

[ConfigSection(exampleHidden: true, defaultOn: true)]
public class LogNetworkErrors
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(Packet), "ProcImpl")]
    public static void Postfix(PacketState __result, Packet __instance)
    {
        if (__result == PacketState.Error)
        {
            MelonLogger.Msg($"[LogNetworkErrors] {__instance.Query.Api}: {__instance.Status}");
        }
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(DataDownloader), "NotifyOffline")]
    public static void DataDownloader()
    {
        MelonLogger.Msg("[LogNetworkErrors] DataDownloader NotifyOffline");
        var stackTrace = new StackTrace();
        MelonLogger.Msg(stackTrace.ToString());
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(OnlineCheckInterval), "NotifyOffline")]
    public static void OnlineCheckInterval()
    {
        MelonLogger.Msg("[LogNetworkErrors] OnlineCheckInterval NotifyOffline");
        var stackTrace = new StackTrace();
        MelonLogger.Msg(stackTrace.ToString());
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(OperationManager), "IsAliveAimeServer", MethodType.Getter)]
    public static void IsAliveAimeServer(bool __result)
    {
        if (__result == false)
            MelonLogger.Msg($"[LogNetworkErrors] IsAliveAimeServer Is {__result}");
    }
}

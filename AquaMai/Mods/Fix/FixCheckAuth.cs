using AMDaemon.Allnet;
using AquaMai.Attributes;
using HarmonyLib;
using Manager;
using Manager.Operation;

namespace AquaMai.Mods.Fix;

[ConfigSection(defaultOn: true, hideInExample: true)]
public class FixCheckAuth
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(OperationManager), "CheckAuth_Proc")]
    private static void PostCheckAuthProc(ref OperationData ____operationData)
    {
        if (Auth.GameServerUri.StartsWith("http://") || Auth.GameServerUri.StartsWith("https://"))
        {
            ____operationData.ServerUri = Auth.GameServerUri;
        }
    }
}

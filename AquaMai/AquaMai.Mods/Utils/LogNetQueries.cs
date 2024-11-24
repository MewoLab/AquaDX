using System.Collections.Generic;
using System.Net;
using System.Reflection;
using System.Reflection.Emit;
using System.Runtime.CompilerServices;
using Net;
using Net.Packet;
using MelonLoader;
using HarmonyLib;
using AquaMai.Core.Attributes;
using AquaMai.Config.Attributes;

namespace AquaMai.Mods.Utils;

using BaseNetQuery = Net.VO.NetQuery<Net.VO.VOSerializer, Net.VO.VOSerializer>;

[ConfigSection(
    en: "Log network requests to the MelonLoader console",
    zh: "将网络请求输出到 MelonLoader 控制台")]
public class LogNetQueries
{
    [ConfigEntry]
    private static readonly bool url = true;

    [ConfigEntry]
    private static readonly bool request = true;
    [ConfigEntry]
    private static readonly string requestOmittedApis = "UploadUserPhotoApi,UploadUserPortraitApi";

    [ConfigEntry]
    private static readonly bool response = true;
    [ConfigEntry]
    private static readonly string responseOmittedApis = "GetGameEventApi";

    private static HashSet<string> requestOmittedApiList = [];
    private static HashSet<string> responseOmittedApiList = [];

    private static readonly ConditionalWeakTable<INetQuery, string> originalApiName = new();

    public static void OnBeforePatch()
    {
        requestOmittedApiList = [.. requestOmittedApis.Split(',')];
        responseOmittedApiList = [.. responseOmittedApis.Split(',')];
    }

    // The original API name is only available from the constructor of NetQuery.
    [HarmonyTranspiler]
    [HarmonyPatch(typeof(BaseNetQuery), MethodType.Constructor, [typeof(string), typeof(ulong)])]
    public static IEnumerable<CodeInstruction> ConstructorTranspiler(IEnumerable<CodeInstruction> instructions)
    {
        return
        [
            new CodeInstruction(OpCodes.Ldarg_0),
            new CodeInstruction(OpCodes.Ldarg_1),
            new CodeInstruction(OpCodes.Call, typeof(LogNetQueries).GetMethod(nameof(SaveOriginalApiName), BindingFlags.NonPublic | BindingFlags.Static)),
            .. instructions
        ];
    }

    private static void SaveOriginalApiName(INetQuery netQuery, string api)
    {
        originalApiName.Add(netQuery, api);
    }

    private static string GetApiName(INetQuery netQuery)
    {
        return originalApiName.TryGetValue(netQuery, out var api)
            ? api
            : $"<Maybe {netQuery.Api}>";
    }

    [EnableIf(nameof(url))]
    [HarmonyPostfix]
    [HarmonyPatch(typeof(Packet), "Create")]
    public static void PostCreate(Packet __instance)
    {
        MelonLogger.Msg($"[LogNetQueries] {GetApiName(__instance.Query)} URL: {InspectNetPacketUrl(__instance)}");
    }

    private static string InspectNetPacketUrl(Packet __instance)
    {
        if (Traverse.Create(__instance).Field("Client").GetValue() is not NetHttpClient client)
        {
            return "<NetHttpClient is null>";
        }
        if (Traverse.Create(client).Field("_request").GetValue() is not HttpWebRequest request)
        {
            return "<HttpWebRequest is null>";
        }
        return request.RequestUri.ToString();
    }

    [EnableIf(nameof(request))]
    [HarmonyPrefix]
    [HarmonyPatch(typeof(Packet), "ProcImpl")]
    public static void PreProcImpl(Packet __instance)
    {
        if (__instance.State != PacketState.Ready)
        {
            return;
        }
        var netQuery = __instance.Query;
        var body = netQuery.GetRequest();
        var info = requestOmittedApiList.Contains(netQuery.Api)
            ? $"<{body.Length} bytes omitted>"
            : body;
        MelonLogger.Msg($"[LogNetQueries] {GetApiName(netQuery)} Request: {info}");
    }

    [EnableIf(nameof(response))]
    [HarmonyPostfix]
    [HarmonyPatch(typeof(BaseNetQuery), "SetResponse")]
    public static void PostSetResponse(BaseNetQuery __instance, string str)
    {
        var api = GetApiName(__instance);
        var info = responseOmittedApiList.Contains(api)
            ? $"<{str.Length} bytes omitted>"
            : str;
        MelonLogger.Msg($"[LogNetQueries] {GetApiName(__instance)} Response: {info}");
    }
}

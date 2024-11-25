using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Runtime.CompilerServices;
using System.Text;
using Net;
using Net.Packet;
using MelonLoader;
using MelonLoader.TinyJSON;
using HarmonyLib;
using AquaMai.Core.Attributes;
using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;

namespace AquaMai.Mods.Utils;

[ConfigSection(
    en: "Log network requests to the MelonLoader console.",
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
    [ConfigEntry(
        en: "Only print error responses, without the successful ones.",
        zh: "仅输出出错的响应，不输出成功的响应")]
    private static readonly bool responseErrorOnly = false;

    private static HashSet<string> requestOmittedApiList = [];
    private static HashSet<string> responseOmittedApiList = [];

    private static readonly ConditionalWeakTable<NetHttpClient, HttpWebResponse> errorResponse = new();

    public static void OnBeforePatch()
    {
        requestOmittedApiList = [.. requestOmittedApis.Split(',')];
        responseOmittedApiList = [.. responseOmittedApis.Split(',')];

        if (responseErrorOnly && !response)
        {
            MelonLogger.Warning("[LogNetQueries] `responseErrorOnly` is enabled but `response` is disabled. Will not print any response.");
        }
    }

    private static string GetApiName(INetQuery netQuery)
    {
        return Shim.RemoveApiSuffix(netQuery.Api);
    }

    [EnableIf(nameof(url))]
    [HarmonyPostfix]
    [HarmonyPatch(typeof(Packet), "Create")]
    public static void PostCreate(Packet __instance)
    {
        MelonLogger.Msg($"[LogNetQueries] {GetApiName(__instance.Query)} URL: {MaybeGetNetPacketUrl(__instance)}");
    }

    private static string MaybeGetNetPacketUrl(Packet __instance)
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

    // Record the error responses of NetHttpClient to display. These responses could not be acquired in other ways.
    [HarmonyPrefix]
    [HarmonyPatch(typeof(NetHttpClient), "SetError")]
    public static void PreSetError(NetHttpClient __instance, HttpWebResponse response)
    {
        if (response != null)
        {
            errorResponse.Add(__instance, response);
        }
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(Packet), "ProcImpl")]
    public static void PreProcImpl(Packet __instance)
    {
        if (request && __instance.State == PacketState.Ready)
        {
            var netQuery = __instance.Query;
            var api = GetApiName(netQuery);
            var displayRequest = InspectRequest(api, netQuery.GetRequest());
            MelonLogger.Msg($"[LogNetQueries] {api} Request: {displayRequest}");
        }
        else if (
            response &&
            __instance.State == PacketState.Process &&
            Traverse.Create(__instance).Field("Client").GetValue() is NetHttpClient client)
        {
            if (client.State == NetHttpClient.StateDone && !responseErrorOnly)
            {
                var netQuery = __instance.Query;
                var api = GetApiName(netQuery);
                var displayResponse = InspectResponse(api, client.GetResponse().ToArray());
                MelonLogger.Msg($"[LogNetQueries] {api} Response: {displayResponse}");
            }
            else if (client.State == NetHttpClient.StateError)
            {
                var displayError = InspectError(client);
                MelonLogger.Warning($"[LogNetQueries] {GetApiName(__instance.Query)} Error: {displayError}");
            }
        }
    }

    private static string InspectRequest(string api, string request) =>
        requestOmittedApiList.Contains(api)
            ? $"<{request.Length} characters omitted>"
            : (request == "" ? "<empty request>" : request);

    private static string InspectResponse(string api, byte[] response)
    {
        try
        {
            var decoded = Encoding.UTF8.GetString(response);
            if (responseOmittedApiList.Contains(api))
            {
                return $"<{decoded.Length} characters omitted>";
            }
            else if (decoded == "")
            {
                return "<empty response>";
            }
            else if (decoded.IndexOf("\n") != -1)
            {
                return JSON.Dump(decoded);
            }
            else
            {
                return decoded;
            }
        }
        catch (Exception e)
        {
            // Always non-empty when decoding fails.
            return $"<Failed to decode text ({JSON.Dump(e.Message)}): {response.Length} bytes " +
                (responseOmittedApiList.Contains(api)
                    ? "omitted"
                    : "[" + BitConverter.ToString(response).Replace("-", " ")) + "]" +
                ">";
        }
    }

    private static string InspectError(NetHttpClient client) => "<" +
        $"WebExceptionStatus.{client.WebException}: " +
        $"HttpStatus = {client.HttpStatus}, " +
        $"Error = {JSON.Dump(client.Error)}, " +
        $"Response = " +
            (errorResponse.TryGetValue(client, out var response)
                ? InspectErrorResponse(response)
                : "null") +
        ">";

    private static string InspectErrorResponse(HttpWebResponse response)
    {
        try
        {
            var webConnectionStream = response.GetResponseStream();
            var memoryStream = new MemoryStream();
            webConnectionStream.CopyTo(memoryStream);
            return InspectResponse(null, memoryStream.ToArray());
        }
        catch (Exception e)
        {
            // The stream has alraedy been consumed?
            return $"<Failed to read response stream ({JSON.Dump(e.Message)})>";
        }
    }
}

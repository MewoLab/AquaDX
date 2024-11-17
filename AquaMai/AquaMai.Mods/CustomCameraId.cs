using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using HarmonyLib;
using Manager;
using MelonLoader;
using UnityEngine;
using AquaMai.Config.Attributes;

namespace AquaMai.Mods;

[ConfigSection(
    en: """
        Enable custom CameraId
        If enabled, you can customize the game to use the specified camera
        """,
    zh: """
        是否启用自定义摄像头ID
        启用后可以指定游戏使用的摄像头
        """)]
public class CustomCameraId
{
    [ConfigEntry(
        en: "Print the camera list to the log when starting, can be used as a basis for modification",
        zh: "启动时打印摄像头列表到日志中，可以作为修改的依据")]
    public static bool PrintCameraList;

    [ConfigEntry(
        en: "DX Pass 1P",
        zh: "DX Pass 1P")]
    public static int LeftQrCamera;

    [ConfigEntry(
        en: "DX Pass 2P",
        zh: "DX Pass 2P")]
    public static int RightQrCamera;

    [ConfigEntry(
        en: "Player Camera",
        zh: "玩家摄像头")]
    public static int PhotoCamera;

    [ConfigEntry(
        en: "WeChat QRCode Camera",
        zh: "二维码扫描摄像头")]
    public static int ChimeCamera;
  
    private static readonly Dictionary<string, string> CameraTypeMap = new()
    {
        ["LeftQrCamera"] = "QRLeft",
        ["RightQrCamera"] = "QRRight",
        ["PhotoCamera"] = "Photo",
        ["ChimeCamera"] = "Chime",
    };

    [HarmonyPrefix]
    [HarmonyPatch(typeof(CameraManager), "CameraInitialize")]
    public static bool CameraInitialize(CameraManager __instance, ref IEnumerator __result)
    {
        __result = CameraInitialize(__instance);
        return false;
    }

    private static IEnumerator CameraInitialize(CameraManager __instance)
    {
        var textureCache = new WebCamTexture[WebCamTexture.devices.Length];
        SortedDictionary<CameraManager.CameraTypeEnum, WebCamTexture> webCamTextures = [];
        foreach (var (configEntry, cameraTypeName) in CameraTypeMap)
        {
            int deviceId = Traverse.Create(typeof(CustomCameraId)).Field(configEntry).GetValue<int>();
            if (deviceId < 0 || deviceId >= WebCamTexture.devices.Length)
            {
                MelonLogger.Warning($"[CustomCameraId] Ignoring custom camera {configEntry}: camera ID {deviceId} out of range");
                continue;
            }

            if (!Enum.TryParse<CameraManager.CameraTypeEnum>(cameraTypeName, out var cameraType))
            {
                MelonLogger.Warning($"[CustomCameraId] Ignoring custom camera {configEntry}: camera type {cameraTypeName} not present");
                continue;
            }

            if (textureCache[deviceId] != null)
            {
                webCamTextures[cameraType] = textureCache[deviceId];
            }
            else
            {
                var webCamTexture = new WebCamTexture(WebCamTexture.devices[deviceId].name);
                webCamTextures[cameraType] = webCamTexture;
                textureCache[deviceId] = webCamTexture;
            }
        }

        int textureCount = webCamTextures.Count;
        __instance.isAvailableCamera = new bool[textureCount];
        __instance.cameraProcMode = new CameraManager.CameraProcEnum[textureCount];

        int textureIndex = 0;
        foreach (var (cameraType, webCamTexture) in webCamTextures)
        {
            __instance.isAvailableCamera[textureIndex] = true;
            __instance.cameraProcMode[textureIndex] = CameraManager.CameraProcEnum.Good;
            CameraManager.DeviceId[(int)cameraType] = textureIndex;
            textureIndex++;
        }
        Traverse.Create(__instance).Field("_webcamtex").SetValue(webCamTextures.Values.ToArray());

        CameraManager.IsReady = true;
        yield break;
    }

    public static void OnBeforePatch()
    {
        if (!PrintCameraList)
        {
            return;
        }

        WebCamDevice[] devices = WebCamTexture.devices;
        string cameraList = "Connected Web Cameras:\n";
        for (int i = 0; i < devices.Length; i++)
        {
            WebCamDevice webCamDevice = devices[i];
            WebCamTexture webCamTexture = new WebCamTexture(webCamDevice.name);
            webCamTexture.Play();
            cameraList += "==================================================\n";
            cameraList += "Name: " + webCamDevice.name + "\n";
            cameraList += $"ID: {i}\n";
            cameraList += $"Resolution: {webCamTexture.width} * {webCamTexture.height}\n";
            cameraList += $"FPS: {webCamTexture.requestedFPS}\n";
            webCamTexture.Stop();
        }
        cameraList += "==================================================";
        
        foreach (var line in cameraList.Split('\n'))
        {
            MelonLogger.Msg($"[CustomCameraId] {line}");
        }
    }
}

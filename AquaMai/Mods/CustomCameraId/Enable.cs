using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using HarmonyLib;
using Manager;
using MelonLoader;
using UnityEngine;

namespace AquaMai.Mods.CustomCameraId;

public class Enable
{
    private static readonly Dictionary<string, string> CameraTypeMap = new Dictionary<string, string>
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
            int deviceId = Traverse.Create(AquaMai.AppConfig.CustomCameraId).Field(configEntry).GetValue<int>();
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
}

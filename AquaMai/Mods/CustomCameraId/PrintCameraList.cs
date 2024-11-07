using MelonLoader;
using UnityEngine;

namespace AquaMai.Mods.CustomCameraId;

public class PrintCameraList
{
    public static void DoCustomPatch(HarmonyLib.Harmony _)
    {
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

using System;
using System.Collections.Generic;
using System.IO;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;
using Process;
using UnityEngine;
using UnityEngine.UI;

namespace AquaMai.Mods.Visual;

[ConfigSection(example: ConfigSectionExample.HiddenDefaultOn)]
public class CustomLogo
{
    private static List<Sprite> segaLogo = new();
    private static List<Sprite> allNetLogo = new();

    public static void DoCustomPatch(HarmonyLib.Harmony h)
    {
        EnumSprite(segaLogo, Path.Combine(Environment.CurrentDirectory, "LocalAssets", "SegaLogo"));
        EnumSprite(allNetLogo, Path.Combine(Environment.CurrentDirectory, "LocalAssets", "AllNetLogo"));
    }

    private static void EnumSprite(List<Sprite> collection, string path)
    {
        if (!Directory.Exists(path)) return;
        foreach (var file in Directory.EnumerateFiles(path, "*.png"))
        {
            var data = File.ReadAllBytes(file);
            var texture2D = new Texture2D(1, 1, TextureFormat.RGBA32, false);
            if (texture2D.LoadImage(data))
            {
                collection.Add(Sprite.Create(texture2D, new Rect(0f, 0f, texture2D.width, texture2D.height), new Vector2(0.5f, 0.5f)));
            }
        }
    }

    [HarmonyPatch(typeof(AdvertiseProcess), "OnStart")]
    [HarmonyPostfix]
    private static void AdvProcessPostFix(AdvertiseMonitor[] ____monitors)
    {
        if (segaLogo.Count > 0)
        {
            var logo = segaLogo[UnityEngine.Random.Range(0, segaLogo.Count)];
            foreach (var monitor in ____monitors)
            {
                monitor.transform.Find("Canvas/Main/SegaAllNet_LOGO/NUL_ADT_SegaAllNet_LOGO/SegaLogo").GetComponent<Image>().sprite = logo;
            }
        }

        if (allNetLogo.Count > 0)
        {
            var logo = allNetLogo[UnityEngine.Random.Range(0, allNetLogo.Count)];
            foreach (var monitor in ____monitors)
            {
                monitor.transform.Find("Canvas/Main/SegaAllNet_LOGO/NUL_ADT_SegaAllNet_LOGO/AllNetLogo").GetComponent<Image>().sprite = logo;
            }
        }
    }
}

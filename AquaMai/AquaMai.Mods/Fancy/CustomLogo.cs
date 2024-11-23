using System.Collections.Generic;
using System.IO;
using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;
using HarmonyLib;
using Monitor;
using Process;
using UnityEngine;
using UnityEngine.UI;

namespace AquaMai.Mods.Fancy;

[ConfigSection(
    en: "Replace the \"SEGA\" and \"ALL.Net\" logos with custom ones.",
    zh: "用自定义的图片替换「SEGA」和「ALL.Net」的标志")]
public class CustomLogo
{
    [ConfigEntry(
        en: "Replace the \"SEGA\" logo with a random PNG image from this directory.",
        zh: "从此目录中随机选择一张 PNG 图片用于「SEGA」标志")]
    private static readonly string segaLogoDir = "LocalAssets/SegaLogo";

    [ConfigEntry(
        en: "Replace the \"ALL.Net\" logo with a random PNG image from this directory.",
        zh: "从此目录中随机选择一张 PNG 图片用于「ALL.Net」标志")]
    private static readonly string allNetLogoDir = "LocalAssets/AllNetLogo";

    private readonly static List<Sprite> segaLogo = [];
    private readonly static List<Sprite> allNetLogo = [];

    public static void OnBeforePatch()
    {
        EnumSprite(segaLogo, FileSystem.ResolvePath(segaLogoDir));
        EnumSprite(allNetLogo, FileSystem.ResolvePath(allNetLogoDir));
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

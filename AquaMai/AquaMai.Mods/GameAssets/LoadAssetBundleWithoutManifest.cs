using System.Collections.Generic;
using System.Linq;
using HarmonyLib;
using UnityEngine;
using Manager;
using Util;
using AquaMai.Config.Attributes;

namespace AquaMai.Mods.GameAssets;

[ConfigSection(
    en: "Load all existing \".ab\" image resources regardless of the AssetBundleImages manifest",
    zh: """
        加载所有存在的 .ab 图片资源（无视 AssetBundleImages.manifest）
        导入了删除曲包之类的话，应该需要开启这个
        """)]
public class LoadAssetBundleWithoutManifest
{
    private static HashSet<string> abFiles = new HashSet<string>();

    [HarmonyPostfix]
    [HarmonyPatch(typeof(OptionDataManager), "CheckAssetBundle")]
    public static void PostCheckAssetBundle(ref Safe.ReadonlySortedDictionary<string, string> abs)
    {
        foreach (var ab in abs)
        {
            abFiles.Add(ab.Key);
        }
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(AssetBundleManifest), "GetAllAssetBundles")]
    public static bool PreGetAllAssetBundles(AssetBundleManifest __instance, ref string[] __result)
    {
        __result = abFiles.ToArray();
        return false;
    }
}

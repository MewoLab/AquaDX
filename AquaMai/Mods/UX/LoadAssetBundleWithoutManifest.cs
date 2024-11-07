using System.Collections.Generic;
using System.Linq;
using HarmonyLib;
using UnityEngine;
using Manager;
using Util;

namespace AquaMai.Mods.UX
{
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
}

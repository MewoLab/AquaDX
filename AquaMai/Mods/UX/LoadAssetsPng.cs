using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using HarmonyLib;
using UnityEngine;
using System.Text.RegularExpressions;
using MAI2.Util;
using Manager;
using MelonLoader;
using Monitor;
using AquaMai.Config.Attributes;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "Load Jacket images from \"LocalAssets\\000000{MusicID}.png\" (for self-made charts)",
    zh: "通过游戏目录下 `LocalAssets\\000000{MusicID}.png` 加载封面（自制谱用）")]
public class LoadAssetsPng
{
    private static string[] imageExts = [".jpg", ".png", ".jpeg"];
    private static Dictionary<string, string> jacketPaths = new();
    private static Dictionary<string, string> tabTitlePaths = new();
    private static Dictionary<string, string> localAssetsContents = new();

    [HarmonyPrefix]
    [HarmonyPatch(typeof(DataManager), "LoadMusicBase")]
    public static void LoadMusicPostfix(List<string> ____targetDirs)
    {
        foreach (var aDir in ____targetDirs)
        {
            if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\jacket")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\jacket")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    var idStr = Path.GetFileName(file).Substring("ui_jacket_".Length, 6);
                    jacketPaths[idStr] = file;
                }

            if (Directory.Exists(Path.Combine(aDir, @"Common\Sprites\Tab\Title")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"Common\Sprites\Tab\Title")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    tabTitlePaths[Path.GetFileNameWithoutExtension(file).ToLowerInvariant()] = file;
                }
        }

        MelonLogger.Msg($"[LoadAssetsPng] Loaded {jacketPaths.Count} Jacket, {tabTitlePaths.Count} Tab Titles from AssetBundleImages.");

        if (Directory.Exists(Path.Combine(Environment.CurrentDirectory, "LocalAssets")))
            foreach (var laFile in Directory.EnumerateFiles(Path.Combine(Environment.CurrentDirectory, "LocalAssets")))
            {
                if (!imageExts.Contains(Path.GetExtension(laFile).ToLowerInvariant())) continue;
                localAssetsContents[Path.GetFileNameWithoutExtension(laFile).ToLowerInvariant()] = laFile;
            }

        MelonLogger.Msg($"[LoadAssetsPng] Loaded {localAssetsContents.Count} LocalAssets.");
    }

    private static string GetJacketPath(string id)
    {
        return localAssetsContents.TryGetValue(id, out var laPath) ? laPath : jacketPaths.GetValueOrDefault(id);
    }

    public static Texture2D GetJacketTexture2D(string id)
    {
        var path = GetJacketPath(id);
        if (path == null)
        {
            return null;
        }

        var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
        texture.LoadImage(File.ReadAllBytes(path));
        return texture;
    }

    public static Texture2D GetJacketTexture2D(int id)
    {
        return GetJacketTexture2D($"{id:000000}");
    }

    /*
    [HarmonyPatch]
    public static class TabTitleLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            // Fxxk unity
            // game load tab title by call Resources.Load<Sprite> directly
            // patching Resources.Load<Sprite> need this stuff
            // var method = typeof(Resources).GetMethods(BindingFlags.Public | BindingFlags.Static).First(it => it.Name == "Load" && it.IsGenericMethod).MakeGenericMethod(typeof(Sprite));
            // return [method];
            // but it not work, game will blackscreen if add prefix or postfix
            //
            // patching AssetBundleManager.LoadAsset will lead game memory error
            // return [AccessTools.Method(typeof(AssetBundleManager), "LoadAsset", [typeof(string)], [typeof(Object)])];
            // and this is not work because game not using this
            //
            // we load them manually after game load and no need to hook the load progress
        }

        public static bool Prefix(string path, ref Object __result)
        {
            if (!path.StartsWith("Common/Sprites/Tab/Title/")) return true;
            var filename = Path.GetFileNameWithoutExtension(path).ToLowerInvariant();
            var locPath = localAssetsContents.TryGetValue(filename, out var laPath) ? laPath : tabTitlePaths.GetValueOrDefault(filename);
            if (locPath is null) return true;

            var texture = new Texture2D(1, 1);
            texture.LoadImage(File.ReadAllBytes(locPath));
            __result = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f));
            MelonLogger.Msg($"GetTabTitleSpritePrefix {locPath} {__result}");
            return false;
        }
    }
    */

    [HarmonyPostfix]
    [HarmonyPatch(typeof(MusicSelectMonitor), "Initialize")]
    public static void TabTitleLoader(MusicSelectMonitor __instance, Dictionary<int, Sprite> ____genreSprite, Dictionary<int, Sprite> ____versionSprite)
    {
        var genres = Singleton<DataManager>.Instance.GetMusicGenres();
        foreach (var (id, genre) in genres)
        {
            if (____genreSprite.GetValueOrDefault(id) is not null) continue;
            var filename = genre.FileName.ToLowerInvariant();
            var locPath = localAssetsContents.TryGetValue(filename, out var laPath) ? laPath : tabTitlePaths.GetValueOrDefault(filename);
            if (locPath is null) continue;
            var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
            texture.LoadImage(File.ReadAllBytes(locPath));
            ____genreSprite[id] = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f));
        }

        var versions = Singleton<DataManager>.Instance.GetMusicVersions();
        foreach (var (id, version) in versions)
        {
            if (____versionSprite.GetValueOrDefault(id) is not null) continue;
            var filename = version.FileName.ToLowerInvariant();
            var locPath = localAssetsContents.TryGetValue(filename, out var laPath) ? laPath : tabTitlePaths.GetValueOrDefault(filename);
            if (locPath is null) continue;
            var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
            texture.LoadImage(File.ReadAllBytes(locPath));
            ____versionSprite[id] = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f));
        }
    }

    [HarmonyPatch]
    public static class JacketLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            return [AM.GetMethod("GetJacketThumbTexture2D", [typeof(string)]), AM.GetMethod("GetJacketTexture2D", [typeof(string)])];
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_Jacket_(\d+)(_s)?\.png");
            if (matches.Count < 1)
            {
                return true;
            }

            var id = matches[0].Groups[1].Value;

            var texture = GetJacketTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"Jacket/UI_Jacket_{id}.png");

            return false;
        }
    }
}

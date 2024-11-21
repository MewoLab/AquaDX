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
using AquaMai.Core.Helpers;

namespace AquaMai.Mods.GameAssets;

[ConfigSection(
    en: "Load asset images from the configured directory (for self-made charts)",
    zh: "从指定目录下加载资源图片（自制谱用）")]
public class LoadLocalImages
{
    [ConfigEntry]
    private static readonly string LocalAssetsDir = "LocalAssets";

    private static string[] imageExts = [".png", ".jpg", ".jpeg"];
    private static Dictionary<string, string> jacketPaths = new();
    private static Dictionary<string, string> framePaths = new();
    private static Dictionary<string, string> platePaths = new();
    private static Dictionary<string, string> framemaskPaths = new();
    private static Dictionary<string, string> framepatternPaths = new();
    private static Dictionary<string, string> iconPaths = new();
    private static Dictionary<string, string> charaPaths = new();
    private static Dictionary<string, string> partnerPaths = new();
    //private static Dictionary<string, string> navicharaPaths = new();
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

            if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\frame")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\frame")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    var idStr = Path.GetFileName(file).Substring("ui_frame_".Length, 6);
                    framePaths[idStr] = file;
                }

            if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\nameplate")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\nameplate")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    var idStr = Path.GetFileName(file).Substring("ui_plate_".Length, 6);
                    platePaths[idStr] = file;
                }

            if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\framemask")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\framemask")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    var idStr = Path.GetFileName(file).Substring("ui_framemask_".Length, 6);
                    framemaskPaths[idStr] = file;
                }

            if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\framepattern")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\framepattern")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    var idStr = Path.GetFileName(file).Substring("ui_framepattern_".Length, 6);
                    framepatternPaths[idStr] = file;
                }

            if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\icon")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\icon")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    var idStr = Path.GetFileName(file).Substring("ui_icon_".Length, 6);
                    iconPaths[idStr] = file;
                }

            if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\chara")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\chara")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    var idStr = Path.GetFileName(file).Substring("ui_chara_".Length, 6);
                    charaPaths[idStr] = file;
                }

            if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\partner")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\partner")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    var idStr = Path.GetFileName(file).Substring("ui_Partner_".Length, 6);
                    partnerPaths[idStr] = file;
                }
            //if (Directory.Exists(Path.Combine(aDir, @"AssetBundleImages\navichara\sprite\parts\ui_navichara_21")))
            //  foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"AssetBundleImages\navichara\sprite\parts\ui_navichara_", charaid)))
            //{
            //  if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
            //var idStr = Path.GetFileName(file).Substring("ui_navichara_".Length, 6);
            //        navicharaPaths[idStr] = file;
            //  }

            if (Directory.Exists(Path.Combine(aDir, @"Common\Sprites\Tab\Title")))
                foreach (var file in Directory.GetFiles(Path.Combine(aDir, @"Common\Sprites\Tab\Title")))
                {
                    if (!imageExts.Contains(Path.GetExtension(file).ToLowerInvariant())) continue;
                    tabTitlePaths[Path.GetFileNameWithoutExtension(file).ToLowerInvariant()] = file;
                }
        }

        MelonLogger.Msg($"[LoadLocalImages] Loaded {jacketPaths.Count} Jacket, {platePaths.Count} NamePlate, {framePaths.Count} Frame, {framemaskPaths.Count} FrameMask, {framepatternPaths.Count} FramePattern, {iconPaths.Count} Icon, {charaPaths.Count} Chara, {partnerPaths.Count} PartnerLogo, {tabTitlePaths.Count} Tab Titles from AssetBundleImages.");

        var resolvedDir = FileSystem.ResolvePath(LocalAssetsDir);
        if (Directory.Exists(resolvedDir))
            foreach (var laFile in Directory.EnumerateFiles(resolvedDir))
            {
                if (!imageExts.Contains(Path.GetExtension(laFile).ToLowerInvariant())) continue;
                localAssetsContents[Path.GetFileNameWithoutExtension(laFile).ToLowerInvariant()] = laFile;
            }

        MelonLogger.Msg($"[LoadLocalImages] Loaded {localAssetsContents.Count} LocalAssets.");
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

    private static string GetFramePath(string id)
    {
        return framePaths.GetValueOrDefault(id);
    }

    public static Texture2D GetFrameTexture2D(string id)
    {
        var path = GetFramePath(id);
        if (path == null)
        {
            return null;
        }

        var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
        texture.LoadImage(File.ReadAllBytes(path));
        return texture;
    }
    private static string GetPlatePath(string id)
    {
        return platePaths.GetValueOrDefault(id);
    }

    public static Texture2D GetPlateTexture2D(string id)
    {
        var path = GetPlatePath(id);
        if (path == null)
        {
            return null;
        }

        var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
        texture.LoadImage(File.ReadAllBytes(path));
        return texture;
    }

    private static string GetFrameMaskPath(string id)
    {
        return framemaskPaths.GetValueOrDefault(id);
    }

    public static Texture2D GetFrameMaskTexture2D(string id)
    {
        var path = GetFrameMaskPath(id);
        if (path == null)
        {
            return null;
        }

        var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
        texture.LoadImage(File.ReadAllBytes(path));
        return texture;
    }

    private static string GetFramePatternPath(string id)
    {
        return framepatternPaths.GetValueOrDefault(id);
    }

    public static Texture2D GetFramePatternTexture2D(string id)
    {
        var path = GetFramePatternPath(id);
        if (path == null)
        {
            return null;
        }

        var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
        texture.LoadImage(File.ReadAllBytes(path));
        return texture;
    }
    private static string GetIconPath(string id)
    {
        return iconPaths.GetValueOrDefault(id);
    }

    public static Texture2D GetIconTexture2D(string id)
    {
        var path = GetIconPath(id);
        if (path == null)
        {
            return null;
        }

        var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
        texture.LoadImage(File.ReadAllBytes(path));
        return texture;
    }
    private static string GetCharaPath(string id)
    {
        return charaPaths.GetValueOrDefault(id);
    }

    public static Texture2D GetCharaTexture2D(string id)
    {
        var path = GetCharaPath(id);
        if (path == null)
        {
            return null;
        }

        var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
        texture.LoadImage(File.ReadAllBytes(path));
        return texture;
    }
    private static string GetPartnerPath(string id)
    {
        return partnerPaths.GetValueOrDefault(id);
    }

    public static Texture2D GetPartnerTexture2D(string id)
    {
        var path = GetPartnerPath(id);
        if (path == null)
        {
            return null;
        }

        var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
        texture.LoadImage(File.ReadAllBytes(path));
        return texture;
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

    [HarmonyPatch]
    public static class FrameLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            return [AM.GetMethod("GetFrameThumbTexture2D", [typeof(string)]), AM.GetMethod("GetFrameTexture2D", [typeof(string)])];
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_Frame_(\d+)(_s)?\.png");
            if (matches.Count < 1)
            {
                return true;
            }

            var id = matches[0].Groups[1].Value;

            var texture = GetFrameTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"Frame/UI_Frame_{id}.png");

            return false;
        }
    }

    [HarmonyPatch]
    public static class PlateLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            return [AM.GetMethod("GetPlateTexture2D", [typeof(string)])];
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_Plate_(\d+)\.png");
            if (matches.Count < 1)
            {
                return true;
            }

            var id = matches[0].Groups[1].Value;

            var texture = GetPlateTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"NamePlate/UI_Plate_{id}.png");

            return false;
        }
    }

    [HarmonyPatch]
    public static class FrameMaskLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            return [AM.GetMethod("GetFrameMaskTexture2D", [typeof(string)])];
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_FrameMask_(\d+)\.png");
            if (matches.Count < 1)
            {
                return true;
            }

            var id = matches[0].Groups[1].Value;

            var texture = GetFrameMaskTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"FrameMask/UI_FrameMask_{id}.png");

            return false;
        }
    }

    [HarmonyPatch]
    public static class FramePatternLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            return [AM.GetMethod("GetFramePatternTexture2D", [typeof(string)])];
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_FramePattern_(\d+)\.png");
            if (matches.Count < 1)
            {
                return true;
            }

            var id = matches[0].Groups[1].Value;

            var texture = GetFramePatternTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"FramePattern/UI_FramePattern_{id}.png");

            return false;
        }
    }

    [HarmonyPatch]
    public static class IconLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            var method = AM.GetMethod("GetIconTexture2D", new[] { typeof(string) });
            if (method != null)
            {
                yield return method;
            }
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_Icon_(\d+)\.png");
            if (matches.Count < 1)
            {
                return true;
            }
            MelonLogger.Msg(filename);
            var id = matches[0].Groups[1].Value;

            var texture = GetIconTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"Icon/UI_Icon_{id}.png");

            return false;
        }
    }

    [HarmonyPatch]
    public static class CharaLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            return [AM.GetMethod("GetCharacterTexture2D", [typeof(string)])];
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_Chara_(\d+)\.png");
            if (matches.Count < 1)
            {
                return true;
            }

            var id = matches[0].Groups[1].Value;

            var texture = GetCharaTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"Chara/UI_Chara_{id}.png");

            return false;
        }
    }
  
    [HarmonyPatch]
    public static class PartnerLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            return [AM.GetMethod("GetPartnerTexture2D", [typeof(string)])];
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_Partner_(\d+)\.png");
            if (matches.Count < 1)
            {
                return true;
            }

            var id = matches[0].Groups[1].Value;

            var texture = GetPartnerTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"Partner/UI_Partner_{id}.png");

            return false;
        }
    }
    /*
    [HarmonyPatch]
    public static class FrameLoader
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var AM = typeof(AssetManager);
            return [AM.GetMethod("GetFrameThumbTexture2D", [typeof(string)]), AM.GetMethod("GetFrameTexture2D", [typeof(string)])];
        }

        public static bool Prefix(string filename, ref Texture2D __result, AssetManager __instance)
        {
            var matches = Regex.Matches(filename, @"UI_Frame_(\d+)\.png");
            if (matches.Count < 1)
            {
                return true;
            }

            var id = matches[0].Groups[1].Value;

            var texture = GetFrameTexture2D(id);
            __result = texture ?? __instance.LoadAsset<Texture2D>($"Frame/UI_Frame_{id}.png");

            return false;
        }
    }
    */
}

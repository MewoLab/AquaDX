using System.Linq;
using AquaMai.Config.Attributes;
using HarmonyLib;
using MAI2.Util;
using Manager;
using MelonLoader;
using Monitor.Game;
using UnityEngine;

namespace AquaMai.Mods.GameSystem.Assets;

[ConfigSection(
    en: """
        Use the png jacket above as MV if no .dat found in the movie folder
        Use together with `LoadLocalImages`
        """,
    zh: """
        如果 movie 文件夹中没有 dat 格式的 MV 的话，就用歌曲的封面做背景，而不是显示迪拉熊的笑脸
        请和 `LoadLocalImages` 一起用
        """)]
public class UseJacketAsDummyMovie
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameCtrl), "IsReady")]
    public static void LoadLocalBgaAwake(GameObject ____movieMaskObj)
    {
        var music = Singleton<DataManager>.Instance.GetMusic(GameManager.SelectMusicID[0]);
        if (music is null) return;

        var moviePath = Singleton<OptionDataManager>.Instance.GetMovieDataPath($"{music.movieName.id:000000}") + ".dat";
        if (!moviePath.Contains("dummy")) return;

        var jacket = LoadLocalImages.GetJacketTexture2D(music.movieName.id);
        if (jacket is null)
        {
            MelonLogger.Msg("No jacket found for music " + music);
            return;
        }

        var components = ____movieMaskObj.GetComponentsInChildren<Component>(false);
        var movies = components.Where(it => it.name == "Movie");

        foreach (var movie in movies)
        {
            // If I create a new RawImage component, the jacket will be not be displayed
            // I think it will be difficult to make it work with RawImage
            // So I change the material that plays video to default sprite material
            // The original player is actually a sprite renderer and plays video with a custom material
            var sprite = movie.GetComponent<SpriteRenderer>();
            sprite.sprite = Sprite.Create(jacket, new Rect(0, 0, jacket.width, jacket.height), new Vector2(0.5f, 0.5f));
            sprite.material = new Material(Shader.Find("Sprites/Default"));
        }
    }
}

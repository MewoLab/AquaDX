using System.Collections.Generic;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;
using UI;
using UnityEngine;
using UnityEngine.UI;

namespace AquaMai.Mods.Fancy;

[ConfigSection(
    en: """
        Custom track start difficulty image (not really custom difficulty)
        Requires CustomSkins to be enabled
        Will load four image resources through custom skins: musicBase, musicTab, musicLvBase, musicLvText
        """,
    zh: """
        自定义在歌曲开始界面上显示的难度贴图 (并不是真的自定义难度)
        需要启用自定义皮肤功能
        会通过自定义皮肤加载四个图片资源: musicBase, musicTab, musicLvBase, musicLvText
        """)]
public class CustomTrackStartDiff
{
    // 自定义在歌曲开始界面上显示的难度 (并不是真的自定义难度)
    // 需要启用自定义皮肤功能
    // 会加载四个图片资源: musicBase, musicTab, musicLvBase, musicLvText

    [HarmonyPostfix]
    [HarmonyPatch(typeof(TrackStartMonitor), "SetTrackStart")]
    private static void DisableTabs(
        MultipleImage ____musicBaseImage,
        MultipleImage ____musicTabImage,
        SpriteCounter ____difficultySingle,
        SpriteCounter ____difficultyDouble,
        Image ____levelTextImage,
        List<ResultMonitor.SpriteSheet> ____musicLevelSpriteSheets,
        TimelineRoot ____musicDetail
    )
    {
        var texture = CustomSkins.CustomTrackStart[0];
        if (texture != null)
        {
            ____musicBaseImage.MultiSprites[6] = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f), 100f);
            ____musicBaseImage.ChangeSprite(6);
        }

        texture = CustomSkins.CustomTrackStart[1];
        if (texture != null)
        {
            ____musicTabImage.MultiSprites[6] = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f), 100f);
            ____musicTabImage.ChangeSprite(6);
        }

        texture = CustomSkins.CustomTrackStart[2];
        if (texture != null)
        {
            var lvBase = Traverse.Create(____musicDetail).Field<MultipleImage>("_lv_Base").Value;
            lvBase.MultiSprites[6] = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f), 100f);
            lvBase.ChangeSprite(6);
        }

        texture = CustomSkins.CustomTrackStart[3];
        if (texture != null)
        {
            var original = ____musicLevelSpriteSheets[0].Sheet;
            var sheet = new Sprite[original.Length];
            for (var i = 0; i < original.Length; i++)
            {
                var sprite = original[i];
                sheet[i] = Sprite.Create(texture, sprite.textureRect, new Vector2(0.5f, 0.5f), 100f);
            }

            ____difficultySingle.SetSpriteSheet(sheet);
            ____difficultyDouble.SetSpriteSheet(sheet);
            ____levelTextImage.sprite = sheet[14];
        }
    }
}

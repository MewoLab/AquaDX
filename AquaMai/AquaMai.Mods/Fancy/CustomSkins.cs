using System;
using System.Collections.Generic;
using System.IO;
using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;
using HarmonyLib;
using MelonLoader;
using Monitor;
using Monitor.Game;
using Process;
using UnityEngine;

namespace AquaMai.Mods.Fancy;

[ConfigSection(
    en: """
        Provide the ability to use custom skins (advanced feature).
        Load skin textures from custom paths.
        """,
    zh: """
        提供自定义皮肤的能力（高级功能）
        从自定义路径中加载皮肤贴图
        """)]
public class CustomSkins
{
    [ConfigEntry]
    private static readonly string skinsDir = "LocalAssets/Skins";

    private static readonly List<string> ImageExts = [".png", ".jpg", ".jpeg"];
    private static readonly List<string> SlideFanFields = ["_normalSlideFan", "_eachSlideFan", "_breakSlideFan", "_breakSlideFanEff"];
    private static readonly List<string> CustomTrackStartFields = ["_musicBase", "_musicTab", "_musicLvBase", "_musicLvText"];

    private static Sprite customOutline;
    private readonly static Sprite[,] customSlideFan = new Sprite[4, 11];

    public static readonly Sprite[,] CustomJudge = new Sprite[2, ((int)NoteJudge.ETiming.End + 1)];
    public static readonly Sprite[,,,] CustomJudgeSlide = new Sprite[2, 3, 2, ((int)NoteJudge.ETiming.End + 1)];
    public static readonly Texture2D[] CustomTrackStart = new Texture2D[4];

    private static bool LoadIntoGameNoteImageContainer(string fieldName, int? idx1, int? idx2, Texture2D texture)
    {
        // 先确定确实有这个 Field, 如果没有的话可以直接跳过这个文件
        var fieldTraverse = Traverse.Create(typeof(GameNoteImageContainer)).Field(fieldName);
        if (!fieldTraverse.FieldExists())
        {
            MelonLogger.Msg($"[CustomNoteSkin] Cannot found field {fieldName}");
            return false;
        }

        var fieldType = fieldTraverse.GetValueType();
        if (!idx1.HasValue)
        {
            // 目标 Field 应当是单个 Sprite
            if (fieldType != typeof(Sprite))
            {
                MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} is a {fieldType.Name}, not a Sprite");
                return false;
            }

            var target = fieldTraverse.GetValue<Sprite>();
            var pivot = new Vector2(target.pivot.x / target.rect.width, target.pivot.y / target.rect.height);
            var custom = Sprite.Create(
                texture, new Rect(0, 0, texture.width, texture.height), pivot, 1f,
                0, SpriteMeshType.Tight, target.border
            );
            fieldTraverse.SetValue(custom);
        }
        else if (!idx2.HasValue)
        {
            // 目标 Field 是一维数组
            if (fieldType != typeof(Sprite[]))
            {
                MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} is a {fieldType.Name}, not a Sprite[]");
                return false;
            }

            var targetArray = fieldTraverse.GetValue<Sprite[]>();
            var target = targetArray[idx1.Value];
            var pivot = new Vector2(target.pivot.x / target.rect.width, target.pivot.y / target.rect.height);
            var custom = Sprite.Create(
                texture, new Rect(0, 0, texture.width, texture.height), pivot, 1f,
                0, SpriteMeshType.Tight, target.border
            );
            targetArray[idx1.Value] = custom;
        }
        else
        {
            // 目标 Field 是二维数组
            if (fieldType != typeof(Sprite[,]))
            {
                MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} is a {fieldType.Name}, not a Sprite[,]");
                return false;
            }

            var targetArray = fieldTraverse.GetValue<Sprite[,]>();
            var target = targetArray[idx1.Value, idx2.Value];
            var pivot = new Vector2(target.pivot.x / target.rect.width, target.pivot.y / target.rect.height);
            var custom = Sprite.Create(
                texture, new Rect(0, 0, texture.width, texture.height), pivot, 1f,
                0, SpriteMeshType.Tight, target.border
            );
            targetArray[idx1.Value, idx2.Value] = custom;
        }

        return true;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameNotePrefabContainer), "Initialize")]
    private static void LoadNoteSkin()
    {
        var resolvedDir = FileSystem.ResolvePath(skinsDir);
        if (!Directory.Exists(resolvedDir)) return;

        foreach (var laFile in Directory.EnumerateFiles(resolvedDir))
        {
            if (!ImageExts.Contains(Path.GetExtension(laFile).ToLowerInvariant())) continue;
            var texture = new Texture2D(1, 1, TextureFormat.RGBA32, false);
            texture.LoadImage(File.ReadAllBytes(laFile));

            var name = Path.GetFileNameWithoutExtension(laFile);
            var args = name.Split('_');
            // 文件名的格式是 XXXXXXXX_A_B 表示 GameNoteImageContainer._XXXXXXXX[A, B]
            // 视具体情况, A, B 可能不存在
            var fieldName = '_' + args[0];
            int? idx1 = (args.Length < 2) ? null : (int.TryParse(args[1], out var temp) ? temp : null);
            int? idx2 = (args.Length < 3) ? null : (int.TryParse(args[2], out temp) ? temp : null);
            int? idx3 = (args.Length < 4) ? null : (int.TryParse(args[3], out temp) ? temp : null);

            Traverse traverse;

            if (CustomTrackStartFields.Contains(fieldName))
            {
                var i = CustomTrackStartFields.IndexOf(fieldName);
                CustomTrackStart[i] = texture;
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (fieldName == "_outline")
            {
                customOutline = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f), 1f);
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (fieldName == "_judgeNormal" || fieldName == "_judgeBreak")
            {
                if (!idx1.HasValue)
                {
                    MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} needs a index");
                    continue;
                }

                var i = (fieldName == "_judgeBreak") ? 1 : 0;
                CustomJudge[i, idx1.Value] = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f), 1f);
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (fieldName == "_judgeSlideNormal" || fieldName == "_judgeSlideBreak")
            {
                if (!idx1.HasValue || !idx2.HasValue || !idx3.HasValue)
                {
                    MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} needs 3 indices");
                    continue;
                }

                var i = (fieldName == "_judgeSlideBreak") ? 1 : 0;
                Vector2 pivot;
                switch (idx1.Value)
                {
                    case 0 when idx2.Value == 0:
                        pivot = new Vector2(0f, 0.5f);
                        break;
                    case 0 when idx2.Value == 1:
                        pivot = new Vector2(1f, 0.5f);
                        break;
                    case 1 when idx2.Value == 0:
                        pivot = new Vector2(0f, 0.3f);
                        break;
                    case 1 when idx2.Value == 1:
                        pivot = new Vector2(1f, 0.3f);
                        break;
                    case 2 when idx2.Value == 0:
                        pivot = new Vector2(0.5f, 0.8f);
                        break;
                    case 2 when idx2.Value == 1:
                        pivot = new Vector2(0.5f, 0.2f);
                        break;
                    default:
                        pivot = new Vector2(0.5f, 0.5f);
                        break;
                }
                CustomJudgeSlide[i, idx1.Value, idx2.Value, idx3.Value] = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), pivot, 1f);
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (SlideFanFields.Contains(fieldName))
            {
                if (!idx1.HasValue)
                {
                    MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} needs a index");
                    continue;
                }

                var i = SlideFanFields.IndexOf(fieldName);
                customSlideFan[i, idx1.Value] = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(1f, 0.5f), 1f);
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (fieldName == "_touchJust")
            {
                traverse = Traverse.Create(GameNotePrefabContainer.TouchTapB);
                var noticeObject = traverse.Field<GameObject>("NoticeObject").Value;
                var target = noticeObject.GetComponent<SpriteRenderer>();
                var pivot = new Vector2(
                    target.sprite.pivot.x / target.sprite.rect.width,
                    target.sprite.pivot.y / target.sprite.rect.height
                );
                var custom = Sprite.Create(
                    texture, new Rect(0, 0, texture.width, texture.height), pivot, 1f,
                    0, SpriteMeshType.Tight, target.sprite.border
                );
                target.sprite = custom;

                traverse = Traverse.Create(GameNotePrefabContainer.TouchTapC);
                noticeObject = traverse.Field<GameObject>("NoticeObject").Value;
                noticeObject.GetComponent<SpriteRenderer>().sprite = custom;
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (fieldName == "_touchHold")
            {
                if (!idx1.HasValue)
                {
                    MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} needs a index");
                    continue;
                }

                traverse = Traverse.Create(GameNotePrefabContainer.TouchHoldC);
                var target = traverse.Field<SpriteRenderer[]>("ColorsObject").Value;
                var renderer = target[idx1.Value];
                var pivot = new Vector2(
                    renderer.sprite.pivot.x / renderer.sprite.rect.width,
                    renderer.sprite.pivot.y / renderer.sprite.rect.height
                );
                var custom = Sprite.Create(
                    texture, new Rect(0, 0, texture.width, texture.height), pivot, 1f,
                    0, SpriteMeshType.Tight, renderer.sprite.border
                );
                renderer.sprite = custom;
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (fieldName == "_normalTouchBorder")
            {
                if (!idx1.HasValue)
                {
                    MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} needs a index");
                    continue;
                }

                traverse = Traverse.Create(GameNotePrefabContainer.TouchReserve);
                var target = traverse.Field<Sprite[]>("_reserveSingleSprite").Value;
                var targetSprite = target[idx1.Value - 2];
                var pivot = new Vector2(
                    targetSprite.pivot.x / targetSprite.rect.width,
                    targetSprite.pivot.y / targetSprite.rect.height
                );
                target[idx1.Value - 2] = Sprite.Create(
                    texture, new Rect(0, 0, texture.width, texture.height), pivot, 1f,
                    0, SpriteMeshType.Tight, targetSprite.border
                );
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (fieldName == "_eachTouchBorder")
            {
                if (!idx1.HasValue)
                {
                    MelonLogger.Msg($"[CustomNoteSkin] Field {fieldName} needs a index");
                    continue;
                }

                traverse = Traverse.Create(GameNotePrefabContainer.TouchReserve);
                var target = traverse.Field<Sprite[]>("_reserveEachSprite").Value;
                var targetSprite = target[idx1.Value - 2];
                var pivot = new Vector2(
                    targetSprite.pivot.x / targetSprite.rect.width,
                    targetSprite.pivot.y / targetSprite.rect.height
                );
                target[idx1.Value - 2] = Sprite.Create(
                    texture, new Rect(0, 0, texture.width, texture.height), pivot, 1f,
                    0, SpriteMeshType.Tight, targetSprite.border
                );
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
                continue;
            }

            if (LoadIntoGameNoteImageContainer(fieldName, idx1, idx2, texture))
            {
                MelonLogger.Msg($"[CustomNoteSkin] Successfully loaded {name}");
            }
        }
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameCtrl), "Initialize")]
    private static void ChangeOutlineTexture(GameObject ____guideEndPointObj)
    {
        if (____guideEndPointObj != null && customOutline != null)
        {
            ____guideEndPointObj.GetComponent<SpriteRenderer>().sprite = customOutline;
        }
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideFan), "Initialize")]
    private static void ChangeFanTexture(
        SpriteRenderer[] ____spriteLines, SpriteRenderer[] ____effectSprites, bool ___BreakFlag, bool ___EachFlag
    )
    {
        Vector3 position;
        Sprite sprite;
        if (___BreakFlag)
        {
            for (var i = 0; i < 11; i++)
            {
                sprite = customSlideFan[2, i];
                if (sprite != null)
                {
                    ____spriteLines[2 * i].sprite = sprite;
                    position = ____spriteLines[2 * i].transform.localPosition;
                    ____spriteLines[2 * i].transform.localPosition = new Vector3(0, position.y, position.z);
                    ____spriteLines[2 * i].color = Color.white;

                    ____spriteLines[2 * i + 1].sprite = sprite;
                    position = ____spriteLines[2 * i + 1].transform.localPosition;
                    ____spriteLines[2 * i + 1].transform.localPosition = new Vector3(0, position.y, position.z);
                    ____spriteLines[2 * i + 1].color = Color.white;
                }

                sprite = customSlideFan[3, i];
                if (sprite != null)
                {
                    ____effectSprites[2 * i].sprite = sprite;
                    position = ____effectSprites[2 * i].transform.localPosition;
                    ____effectSprites[2 * i].transform.localPosition = new Vector3(0, position.y, position.z);
                    ____effectSprites[2 * i].color = Color.white;

                    ____effectSprites[2 * i + 1].sprite = sprite;
                    position = ____effectSprites[2 * i + 1].transform.localPosition;
                    ____effectSprites[2 * i + 1].transform.localPosition = new Vector3(0, position.y, position.z);
                    ____effectSprites[2 * i + 1].color = Color.white;
                }
            }
        }
        else if (___EachFlag)
        {
            for (var i = 0; i < 11; i++)
            {
                sprite = customSlideFan[1, i];
                if (sprite != null)
                {
                    ____spriteLines[2 * i].sprite = sprite;
                    position = ____spriteLines[2 * i].transform.localPosition;
                    ____spriteLines[2 * i].transform.localPosition = new Vector3(0, position.y, position.z);
                    ____spriteLines[2 * i].color = Color.white;

                    ____spriteLines[2 * i + 1].sprite = sprite;
                    position = ____spriteLines[2 * i + 1].transform.localPosition;
                    ____spriteLines[2 * i + 1].transform.localPosition = new Vector3(0, position.y, position.z);
                    ____spriteLines[2 * i + 1].color = Color.white;
                }
            }
        }
        else
        {
            for (var i = 0; i < 11; i++)
            {
                sprite = customSlideFan[0, i];
                if (sprite != null)
                {
                    ____spriteLines[2 * i].sprite = sprite;
                    position = ____spriteLines[2 * i].transform.localPosition;
                    ____spriteLines[2 * i].transform.localPosition = new Vector3(0, position.y, position.z);
                    ____spriteLines[2 * i].color = Color.white;

                    ____spriteLines[2 * i + 1].sprite = sprite;
                    position = ____spriteLines[2 * i + 1].transform.localPosition;
                    ____spriteLines[2 * i + 1].transform.localPosition = new Vector3(0, position.y, position.z);
                    ____spriteLines[2 * i + 1].color = Color.white;
                }
            }
        }
    }
}

using System.Collections.Generic;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: """
        Invert the Slide hierarchy, so that the new Slide appears on top like Maimai classic
        Enable to support color changing effects achieved by overlaying multiple stars
        """,
    zh: """
        反转 Slide 层级, 使新出现的 Slide 像旧框一样显示在上层
        启用以支持通过叠加多个星星达成的变色效果
        """)]
public class SlideLayerReverse
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideRoot), "Initialize")]
    private static void CalcArrowLayer(
        bool ___BreakFlag, List<SpriteRenderer> ____spriteRenders, List<BreakSlide> ____breakSpriteRenders,
        int ___SlideIndex, int ____baseArrowSortingOrder
    )
    {
        // 原本的 sortingOrder 是 -(SlideIndex + _baseArrowSortingOrder + index)
        // 令 orderBase = SlideIndex + _baseArrowSortingOrder
        // 分配给这条 slide 的 sortingOrder 范围是 -(orderBase + count - 1) ~ -(orderBase)
        // 现在要保留 slide 内部箭头顺序, 但使得 slide 间次序反转
        // 范围会变成 orderBase ~ orderBase + count - 1
        // 其中原本是 -(orderBase) 的箭头应该调整为 orderBase + count - 1
        
        var orderBase = ___SlideIndex + ____baseArrowSortingOrder; // SlideIndex + _baseArrowSortingOrder
        if (!___BreakFlag)
        {
            var lastIdx = ____spriteRenders.Count - 1;
            for (var index = 0; index < ____spriteRenders.Count; index++)
            {
                var renderer = ____spriteRenders[index];
                renderer.sortingOrder = -32700 + orderBase + lastIdx - index;
            }
        }
        else
        {
            var lastIdx = ____breakSpriteRenders.Count - 1;
            for (var index = 0; index < ____breakSpriteRenders.Count; index++)
            {
                var breakSlide = ____breakSpriteRenders[index];
                breakSlide.SetSortingOrder(-32700 + orderBase + lastIdx - index);
            }
        }
    }
    
    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideFan), "Initialize")]
    private static void CalcFanArrowLayer(
        SpriteRenderer[] ____spriteLines, SpriteRenderer[] ____effectSprites,
        int ___SlideIndex, int ____baseArrowSortingOrder
    )
    {
        var orderBase = ___SlideIndex + ____baseArrowSortingOrder; // SlideIndex + _baseArrowSortingOrder
        var lastIdx = ____spriteLines.Length - 1;
        for (var index = 0; index < ____spriteLines.Length; index++)
        {
            var renderer = ____spriteLines[index];
            renderer.sortingOrder = -32700 + orderBase + lastIdx - index;
        }
        lastIdx = ____effectSprites.Length - 1;
        for (var index = 0; index < ____effectSprites.Length; index++)
        {
            var renderer = ____effectSprites[index];
            renderer.sortingOrder = 1000 + orderBase + lastIdx - index;
        }
    }
}

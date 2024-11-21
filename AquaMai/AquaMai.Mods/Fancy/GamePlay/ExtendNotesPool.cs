using System.Collections.Generic;
using AquaMai.Core.Attributes;
using AquaMai.Config.Attributes;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Monitor;
using Monitor.Game;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: "Add notes sprite to the pool to prevent use up",
    zh: "增加更多待命的音符贴图，防止奇怪的自制谱用完音符贴图池")]
[EnableGameVersion(23000)]
public class ExtendNotesPool
{
    [ConfigEntry(
        en: "Number of objects to add",
        zh: "要增加的对象数量")]
    private readonly static int Count = 128;

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameCtrl), "CreateNotePool")]
    public static void CreateNotePool(ref GameCtrl __instance,
        GameObject ____tapListParent, List<TapNote> ____tapObjectList,
        GameObject ____holdListParent, List<HoldNote> ____holdObjectList,
        GameObject ____breakHoldListParent, List<BreakHoldNote> ____breakHoldObjectList,
        GameObject ____starListParent, List<StarNote> ____starObjectList,
        GameObject ____breakStarListParent, List<BreakStarNote> ____breakStarObjectList,
        GameObject ____breakListParent, List<BreakNote> ____breakObjectList,
        GameObject ____touchListParent, List<TouchNoteB> ____touchBObjectList,
        GameObject ____touchCTapListParent, List<TouchNoteC> ____touchCTapObjectList,
        GameObject ____touchCHoldListParent, List<TouchHoldC> ____touchCHoldObjectList,
        GameObject ____slideListParent, List<SlideRoot> ____slideObjectList,
        GameObject ____fanSlideListParent, List<SlideFan> ____fanSlideObjectList,
        GameObject ____slideJudgeListParent, List<SlideJudge> ____judgeSlideObjectList,
        GameObject ____guideListParent, List<NoteGuide> ____guideObjectList,
        GameObject ____barGuideListParent, List<BarGuide> ____barGuideObjectList,
        List<SpriteRenderer> ____arrowObjectList, List<BreakSlide> ____breakArrowObjectList
    )
    {
        for (var i = 0; i < Count; i++)
        {
            var tapNote = Object.Instantiate(GameNotePrefabContainer.Tap, ____tapListParent.transform);
            tapNote.gameObject.SetActive(false);
            tapNote.ParentTransform = ____tapListParent.transform;
            ____tapObjectList.Add(tapNote);

            var holdNote = Object.Instantiate(GameNotePrefabContainer.Hold, ____holdListParent.transform);
            holdNote.gameObject.SetActive(false);
            holdNote.ParentTransform = ____holdListParent.transform;
            ____holdObjectList.Add(holdNote);

            var breakHoldNote = Object.Instantiate(GameNotePrefabContainer.BreakHold, ____breakHoldListParent.transform);
            breakHoldNote.gameObject.SetActive(false);
            breakHoldNote.ParentTransform = ____holdListParent.transform;
            ____breakHoldObjectList.Add(breakHoldNote);

            var starNote = Object.Instantiate(GameNotePrefabContainer.Star, ____starListParent.transform);
            starNote.gameObject.SetActive(false);
            starNote.ParentTransform = ____starListParent.transform;
            ____starObjectList.Add(starNote);

            var breakStarNote = Object.Instantiate(GameNotePrefabContainer.BreakStar, ____breakStarListParent.transform);
            breakStarNote.gameObject.SetActive(false);
            breakStarNote.ParentTransform = ____breakStarListParent.transform;
            ____breakStarObjectList.Add(breakStarNote);

            var breakNote = Object.Instantiate(GameNotePrefabContainer.Break, ____breakListParent.transform);
            breakNote.gameObject.SetActive(false);
            breakNote.ParentTransform = ____breakListParent.transform;
            ____breakObjectList.Add(breakNote);

            var touchNoteB = Object.Instantiate(GameNotePrefabContainer.TouchTapB, ____touchListParent.transform);
            touchNoteB.gameObject.SetActive(false);
            touchNoteB.ParentTransform = ____touchListParent.transform;
            ____touchBObjectList.Add(touchNoteB);

            var touchNoteC = Object.Instantiate(GameNotePrefabContainer.TouchTapC, ____touchCTapListParent.transform);
            touchNoteC.gameObject.SetActive(false);
            touchNoteC.ParentTransform = ____touchCTapListParent.transform;
            ____touchCTapObjectList.Add(touchNoteC);

            var touchHoldC = Object.Instantiate(GameNotePrefabContainer.TouchHoldC, ____touchCHoldListParent.transform);
            touchHoldC.gameObject.SetActive(false);
            touchHoldC.ParentTransform = ____touchCHoldListParent.transform;
            ____touchCHoldObjectList.Add(touchHoldC);

            var slideRoot = Object.Instantiate(GameNotePrefabContainer.Slide, ____slideListParent.transform);
            slideRoot.gameObject.SetActive(false);
            slideRoot.ParentTransform = ____slideListParent.transform;
            ____slideObjectList.Add(slideRoot);

            var slideFan = Object.Instantiate(GameNotePrefabContainer.SlideFan, ____fanSlideListParent.transform);
            slideFan.gameObject.SetActive(false);
            slideFan.ParentTransform = ____fanSlideListParent.transform;
            ____fanSlideObjectList.Add(slideFan);

            var slideJudge = Object.Instantiate(GameNotePrefabContainer.SlideJudge, ____slideJudgeListParent.transform);
            slideJudge.gameObject.SetActive(false);
            slideJudge.ParentTransform = ____slideJudgeListParent.transform;
            slideJudge.SetOption(Singleton<GamePlayManager>.Instance.GetGameScore(__instance.MonitorIndex).UserOption.DispJudge);
            ____judgeSlideObjectList.Add(slideJudge);

            for (var j = 0; j < 50; j++)
            {
                var spriteRenderer = Object.Instantiate(GameNotePrefabContainer.Arrow, ____slideListParent.transform);
                spriteRenderer.gameObject.SetActive(false);
                ____arrowObjectList.Add(spriteRenderer);

                var breakSlide = Object.Instantiate(GameNotePrefabContainer.BreakArrow, ____slideListParent.transform);
                breakSlide.gameObject.SetActive(false);
                ____breakArrowObjectList.Add(breakSlide);
            }

            var noteGuide = Object.Instantiate(GameNotePrefabContainer.Guide, ____guideListParent.transform);
            noteGuide.gameObject.SetActive(false);
            noteGuide.ParentTransform = ____guideListParent.transform;
            ____guideObjectList.Add(noteGuide);

            var barGuide = Object.Instantiate(GameNotePrefabContainer.BarGuide, ____barGuideListParent.transform);
            barGuide.gameObject.SetActive(false);
            barGuide.ParentTransform = ____barGuideListParent.transform;
            ____barGuideObjectList.Add(barGuide);
        }
    }
}

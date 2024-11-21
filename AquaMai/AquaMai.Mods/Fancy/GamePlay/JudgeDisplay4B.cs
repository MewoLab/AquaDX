using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: """
        More detailed judgment display
        Requires CustomSkins to be enabled and the resource file to be downloaded
        https://github.com/hykilpikonna/AquaDX/releases/download/nightly/JudgeDisplay4B.7z
        """,
    zh: """
        更精细的判定表示
        需开启 CustomSkins 并下载资源文件
        https://github.com/hykilpikonna/AquaDX/releases/download/nightly/JudgeDisplay4B.7z
        """)]
public class JudgeDisplay4B
{
    // 精确到子判定的自定义判定显示, 需要启用自定义皮肤功能 (理论上不启用自定义皮肤不会崩游戏, 只不过此时这个功能显然不会生效)

    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideJudge), "Initialize")]
    private static void SlideJudgeDisplay4B(
        SpriteRenderer ___SpriteRenderAdd, SpriteRenderer ___SpriteRender,
        SlideJudge.SlideJudgeType ____judgeType, SlideJudge.SlideAngle ____angle,
        NoteJudge.ETiming judge, float msec, bool isBreak
    )
    {
        var i = isBreak ? 1 : 0;
        Sprite sprite = CustomSkins.CustomJudgeSlide[i, (int)____judgeType, (int)____angle, (int)judge];
        if (sprite != null) {
            ___SpriteRender.sprite = sprite;
        }

        if (isBreak && judge == NoteJudge.ETiming.Critical)
        {
            sprite = CustomSkins.CustomJudgeSlide[i, (int)____judgeType, (int)____angle, (int) NoteJudge.ETiming.End];
            if (sprite != null)
            {
                ___SpriteRenderAdd.sprite = sprite;
            }
        }
    }


    [HarmonyPostfix]
    [HarmonyPatch(typeof(JudgeGrade), "Initialize")]
    private static void JudgeGradeDisplay4B(
        SpriteRenderer ___SpriteRender,
        NoteJudge.ETiming judge, float msec, NoteJudge.EJudgeType type
    )
    {
        var i = (type == NoteJudge.EJudgeType.Break) ? 1 : 0;
        Sprite sprite = CustomSkins.CustomJudge[i, (int)judge];
        if (sprite != null) {
            ___SpriteRender.sprite = sprite;
        }
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(JudgeGrade), "InitializeBreak")]
    private static void JudgeGradeBreakDisplay4B(
        SpriteRenderer ___SpriteRenderAdd,
        NoteJudge.ETiming judge, float msec, NoteJudge.EJudgeType type
    )
    {
        if (judge == NoteJudge.ETiming.Critical)
        {
            var sprite = CustomSkins.CustomJudge[1, (int) NoteJudge.ETiming.End];
            if (sprite != null)
            {
                ___SpriteRenderAdd.sprite = sprite;
            }
        }
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(JudgeGrade), "InitializeBreak")]
    private static void InitializeBreakFix(ref NoteJudge.EJudgeType type)
    {
        type = NoteJudge.EJudgeType.Break;
    }

}

using System.Collections.Generic;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;
using Monitor;
using Monitor.Game;
using Process;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: "Make the Slide Track disappear with an inward-shrinking animation, similar to AstroDX.",
    zh: "使 Slide Track 消失时有类似 AstroDX 一样的向内缩入的动画")]
public class SlideArrowAnimation
{
    private static List<SpriteRenderer> _animatingSpriteRenderers = [];
    
    [HarmonyTranspiler]
    [HarmonyPatch(typeof(SlideRoot), "NoteCheck")]
    private static IEnumerable<CodeInstruction> GetUnVisibleColorHook(IEnumerable<CodeInstruction> instructions)
    {
        var methodGetUnVisibleColor = AccessTools.Method(typeof(SlideRoot), "GetUnVisibleColor");
        
        var oldInstList = new List<CodeInstruction>(instructions);
        var newInstList = new List<CodeInstruction>();
        
        for (var i = 0; i < oldInstList.Count; i++)
        {
            var inst = oldInstList[i];
            if (inst.Calls(methodGetUnVisibleColor))
            {
                // 现在栈上应该有: SpriteRenderer, SlideRoot(this)
                // 这一条 IL 会消耗 this, 调用 GetUnVisibleColor(), 推一个 Color 到栈上
                // 然后接下来的一条 IL 是调用 SpriteRenderer.color 的 setter 把 SpriteRenderer 和 Color 一起消耗掉
                // 我们现在直接用一个 static method 消耗掉 SpriteRenderer 和 this
                // 所以要忽略当前 IL, 再忽略下一条 IL, 然后构造一个 Call
                
                // ReSharper disable once ConvertClosureToMethodGroup
                var redirect = CodeInstruction.Call((SpriteRenderer r, SlideRoot s) => OnSlideArrowDisable(r, s));
                newInstList.Add(redirect);
                i++;  // 跳过下一条 IL
            }
            else
            {
                newInstList.Add(inst);
            }
        }
        return newInstList;
    }

    public static void OnSlideArrowDisable(SpriteRenderer renderer, SlideRoot slideRoot)
    {
        _animatingSpriteRenderers.Add(renderer);
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideRoot), "SetArrowObject")]
    private static void RemoveArrowAnimation(GameObject arrowobj)
    {
        var spriteRenderer = arrowobj.GetComponent<SpriteRenderer>();
        spriteRenderer.transform.localScale = Vector3.one;
        if (_animatingSpriteRenderers.Contains(spriteRenderer))
        {
            _animatingSpriteRenderers.Remove(spriteRenderer);
        }
    }
    
    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideRoot), "SetBreakArrowObject")]
    private static void RemoveBreakArrowAnimation(GameObject breakArrowobj)
    {
        var breakSlideObj = breakArrowobj.GetComponent<BreakSlide>();
        breakSlideObj.SpriteRender.transform.localScale = Vector3.one;
        if (_animatingSpriteRenderers.Contains(breakSlideObj.SpriteRender))
        {
            _animatingSpriteRenderers.Remove(breakSlideObj.SpriteRender);
        }
    }
    
    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameCtrl), "UpdateNotes")]
    private static void OnGameCtrlUpdateNotesLast()
    {
        for (var num = _animatingSpriteRenderers.Count - 1; num >= 0; num--)
        {
            var spriteRenderer = _animatingSpriteRenderers[num];
            if (spriteRenderer == null || !spriteRenderer.gameObject.activeSelf)
            {
                _animatingSpriteRenderers.RemoveAt(num);
            }
            else
            {
                var localScale = spriteRenderer.transform.localScale;
                var scale = localScale.y - NotesManager.GetAddMSec() / 150f;
                if (scale <= 0)
                {
                    spriteRenderer.transform.localScale = new Vector3(1f, 0f, 1f);
                    spriteRenderer.color = new Color(1f, 1f, 1f, 0f);
                    _animatingSpriteRenderers.RemoveAt(num);
                }
                else
                {
                    localScale.y = scale;
                    spriteRenderer.color = new Color(1f, 1f, 1f, Mathf.Sqrt(scale));
                    spriteRenderer.transform.localScale = localScale;
                }
            }
        }
    }
    
    [HarmonyPrefix]
    [HarmonyPatch(typeof(GameProcess), "SetRelease")]
    private static void OnBeforeGameProcessSetRelease()
    {
        _animatingSpriteRenderers.Clear();
    }
}

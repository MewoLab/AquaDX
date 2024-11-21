using AquaMai.Config.Attributes;
using HarmonyLib;
using UnityEngine;

namespace AquaMai.Mods.Fancy;

[ConfigSection(
    en: "Remove the circle mask of the game screen",
    zh: "移除游戏画面的圆形遮罩")]
public class HideMask
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(Main.GameMain), "LateInitialize", typeof(MonoBehaviour), typeof(Transform), typeof(Transform))]
    public static void LateInitialize(MonoBehaviour gameMainObject)
    {
        GameObject.Find("Mask").SetActive(false);
    }
}

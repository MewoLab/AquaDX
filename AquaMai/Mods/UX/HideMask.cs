using AquaMai.Config.Attributes;
using HarmonyLib;
using UnityEngine;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "Remove the circle mask in the game",
    zh: "移除遮罩")]
public class HideMask
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(Main.GameMain), "LateInitialize", typeof(MonoBehaviour), typeof(Transform), typeof(Transform))]
    public static void LateInitialize(MonoBehaviour gameMainObject)
    {
        GameObject.Find("Mask").SetActive(false);
    }
}

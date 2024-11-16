using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;
using HarmonyLib;
using Main;
using UnityEngine;

namespace AquaMai.Mods.Utils;

[ConfigSection(
    en: "Display framerate",
    zh: "显示帧率")]
public class FrameRateDisplay
{
    [HarmonyPatch(typeof(GameMainObject), "Awake")]
    [HarmonyPostfix]
    public static void ShowUi(GameMainObject __instance)
    {
        __instance.gameObject.AddComponent<Ui>();
    }

    private class Ui : MonoBehaviour
    {
        private static float sampleTime = 1f;
        private static int frame;
        private static float time;
        private static float fps;

        public void OnGUI()
        {
            var labelStyle = GUI.skin.GetStyle("label");
            labelStyle.fontSize = GuiSizes.FontSize;
            labelStyle.alignment = TextAnchor.MiddleCenter;

            const float x = 10f;
            const float y = 10f;
            var width = GuiSizes.FontSize * 7f;
            var height = GuiSizes.LabelHeight * 1.5f;

            frame += 1;
            time += Time.deltaTime;

            if (time >= sampleTime)
            {
                fps = frame / time;
                frame = 0;
                time = 0;
            }

            GUI.Box(new Rect(x, y, width, height), "");
            GUI.Label(new Rect(x, y, width, height), $"{fps:0.0} FPS");
        }
    }
}

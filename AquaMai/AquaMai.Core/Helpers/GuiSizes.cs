using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using HarmonyLib;
using UnityEngine;

namespace AquaMai.Core.Helpers;

public static class GuiSizes
{
    public static bool SinglePlayer { get; set; } = false;
    public static float PlayerWidth => Screen.height / 1920f * 1080;
    public static float PlayerCenter => SinglePlayer ? Screen.width / 2f : Screen.width / 2f - PlayerWidth / 2;
    public static int FontSize => (int)(PlayerWidth * .015f);
    public static float LabelHeight => FontSize * 1.5f;
    public static float Margin => PlayerWidth * .005f;

    private static Color backgroundColor = new(147 / 256f, 160 / 256f, 173 / 256f, .8f);

    public static void SetupStyles()
    {
        var buttonStyle = GUI.skin.button;
        buttonStyle.normal.textColor = Color.white;
        buttonStyle.normal.background = Texture2D.whiteTexture;
        buttonStyle.hover.background = Texture2D.whiteTexture;
        buttonStyle.active.background = Texture2D.whiteTexture;
        buttonStyle.border = new RectOffset(0, 0, 0, 0);
        buttonStyle.margin = new RectOffset(0, 0, 0, 0);
        buttonStyle.padding = new RectOffset(10, 10, 10, 10);
        buttonStyle.overflow = new RectOffset(0, 0, 0, 0);

        var boxStyle = GUI.skin.box;
        boxStyle.border = new RectOffset(0, 0, 0, 0);
        boxStyle.normal.background = Texture2D.whiteTexture;

        GUI.backgroundColor = backgroundColor;
    }

    [HarmonyPatch]
    public class BoxBackground
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            return typeof(GUI).GetMethods().Where(x => x.Name == "Box");
        }

        public static void Prefix()
        {
            GUI.backgroundColor = new Color(62 / 256f, 62 / 256f, 66 / 256f, .6f);
        }

        public static void Postfix()
        {
            GUI.backgroundColor = backgroundColor;
        }
    }
}

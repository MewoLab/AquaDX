using System.Collections.Generic;
using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;
using AquaMai.Core.Resources;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Monitor;
using Process;
using UnityEngine;

namespace AquaMai.Mods.Utils;

[ConfigSection(
    en: "Show Network error detail in the game when gray network icon appears",
    zh: "出现灰网时显示原因")]
public class ShowNetErrorDetail
{
    [HarmonyPatch(typeof(CommonProcess), "OnStart")]
    [HarmonyPostfix]
    public static void SetIconStatus(CommonMonitor[] ____monitors)
    {
        ____monitors[0].gameObject.AddComponent<DetailUi>();
    }

    private class DetailUi : MonoBehaviour
    {
        public void OnGUI()
        {
            var errors = new List<string>();
            if (!Singleton<OperationManager>.Instance.IsAliveServer)
            {
                errors.Add(Locale.NetErrIsAliveServer);
            }

            if (!Singleton<OperationManager>.Instance.IsAliveAimeReader)
            {
                errors.Add(Locale.NetErrIsAliveAimeReader);
            }

            if (!Singleton<OperationManager>.Instance.IsAliveAimeServer)
            {
                errors.Add(Locale.NetErrIsAliveAimeServer);
            }

            if (!Singleton<OperationManager>.Instance.WasDownloadSuccessOnce)
            {
                errors.Add(Locale.NetErrWasDownloadSuccessOnce);
            }

            if (errors.Count == 0)
            {
                return;
            }

            var labelStyle = GUI.skin.GetStyle("label");
            labelStyle.fontSize = GuiSizes.FontSize;
            labelStyle.alignment = TextAnchor.MiddleCenter;

            var x = GuiSizes.PlayerCenter + GuiSizes.PlayerWidth * .2f;
            var y = Screen.height * .01f;
            var width = GuiSizes.FontSize * 15f;
            var height = GuiSizes.LabelHeight * errors.Count + GuiSizes.Margin * 2;

            GUI.Box(new Rect(x, y, width, height), "");
            for (var i = 0; i < errors.Count; i++)
            {
                GUI.Label(new Rect(x, y + GuiSizes.Margin + GuiSizes.LabelHeight * i, width, GuiSizes.LabelHeight), errors[i]);
            }
        }
    }
}

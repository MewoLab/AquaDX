using System;
using System.Collections;
using System.IO;
using System.Text.RegularExpressions;
using System.Threading;
using AquaMai.Helpers;
using HarmonyLib;
using Main;
using MelonLoader;
using UnityEngine;

namespace AquaMai.Mods.Utils;

public class ShowErrorLog
{
    private static Ui _errorUi;

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameMain), "ExceptionHandler")]
    private static void ExceptionHandler(GameMain __instance, Exception e)
    {
        if (_errorUi == null)
        {
            _errorUi = new GameObject("ErrorUI").AddComponent<Ui>();
            _errorUi.gameObject.SetActive(true);
        }
        string logFile = $"{MAI2System.Path.ErrorLogPath}{DateTime.Now:yyyyMMddHHmmss}.log";
        MelonLogger.Msg("Error Log:");
        if (File.Exists(logFile))
        {
            MelonLogger.Error(File.ReadAllText(logFile));
            _errorUi.SetErrorLog(File.ReadAllText(logFile));
        }
        else
        {
            MelonLogger.Error(e);
            _errorUi.SetErrorLog(e.ToString());
        }

        Application.quitting += ApplicationOnQuitting;
        _errorUi.StartCoroutine(_errorUi.Show());
    }

    private static void ApplicationOnQuitting()
    {
        Thread.Sleep(Timeout.Infinite);
    }

    private class Ui : MonoBehaviour
    {
        private string _errorLog = "";
        
        public void SetErrorLog(string text)
        {
            _errorLog = "Error Log:\n" + text;
        }

        public void OnGUI()
        {
            var labelStyle = new GUIStyle(GUI.skin.label)
            {
                fontSize = GuiSizes.FontSize,
                alignment = TextAnchor.MiddleLeft,
                normal = new GUIStyleState(){textColor = Color.black}
            };

            var boxStyle = new GUIStyle(GUI.skin.box)
            {
                normal = new GUIStyleState() { background = Texture2D.whiteTexture }
            };
            
            int logLineCount = Regex.Matches(_errorLog, "\n").Count + 1;
            float offset = GuiSizes.PlayerCenter * 0.12f;
            var x = GuiSizes.PlayerCenter / 2f + offset / 2f;
            var y = Screen.height / 1.8f;
            var width = GuiSizes.PlayerCenter - offset;
            var height = GuiSizes.LabelHeight * logLineCount + GuiSizes.Margin * 2;

            GUI.Box(new Rect(x, y, width, height), "", boxStyle);
            GUI.Label(new Rect(x, y, width, height), _errorLog, labelStyle);
            if (!AquaMai.AppConfig.UX.SinglePlayer)
            {
                GUI.Box(new Rect(x + GuiSizes.PlayerWidth, y, width, height), "", boxStyle);
                GUI.Label(new Rect(x + GuiSizes.PlayerWidth, y, width, height), _errorLog, labelStyle);
            }
        }
        
        public IEnumerator Show()
        {
            while (true)
            {
                yield return null; // 让 Unity 处理一帧
            }
        }
    }
}

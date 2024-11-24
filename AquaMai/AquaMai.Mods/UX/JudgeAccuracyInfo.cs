using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Reflection;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;
using Monitor;
using Monitor.Result;
using Process;
using TMPro;
using UnityEngine;
using Object = UnityEngine.Object;

namespace AquaMai.Mods.UX;

[ConfigSection(
    zh: "在游戏总结的计分板中显示击打误差的详细信息（以帧为单位）",
    en: "Show detailed accuracy info in the score board.")]
public class JudgeAccuracyInfo
{
    public class AccuracyEntryList
    {
        public List<float>[] DiffList = new List<float>[TableRowNames.Length];
        public List<float>[] RawDiffList = new List<float>[TableRowNames.Length];
        public HashSet<int> NoteIndices = new();

        public AccuracyEntryList()
        {
            for (int i = 0; i < TableRowNames.Length; i++)
            {
                DiffList[i] = new List<float>();
                RawDiffList[i] = new List<float>();
            }
        }
    }

    public static AccuracyEntryList[] EntryList = new AccuracyEntryList[2];
    
    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameProcess), "OnStart")]
    private static void OnGameProcessStartFinish()
    {
        for (int i = 0; i < EntryList.Length; i++)
        {
            EntryList[i] = new AccuracyEntryList();
        }
    }
    
    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultProcess), "OnRelease")]
    private static void OnResultProcessReleaseFinish()
    {
        for (int i = 0; i < EntryList.Length; i++)
        {
            EntryList[i] = null;
            Controllers[i] = null;
        }
    }
    
    [HarmonyPatch]
    public static class NoteBaseJudgePatch
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            return
            [
                AccessTools.Method(typeof(NoteBase), "Judge"),
                AccessTools.Method(typeof(HoldNote), "JudgeHoldHead"),
                AccessTools.Method(typeof(BreakHoldNote), "JudgeHoldHead"),
                AccessTools.Method(typeof(TouchNoteB), "Judge"),
                AccessTools.Method(typeof(TouchHoldC), "JudgeHoldHead"),
            ];
        }
        
        public static void Postfix(
            NoteBase __instance, bool __result,
            float ___JudgeTimingDiffMsec, float ___AppearMsec, NoteJudge.EJudgeType ___JudgeType, int ___NoteIndex
        )
        {
            var monitor = __instance.MonitorId;
            if (!__result || EntryList[monitor].NoteIndices.Contains(___NoteIndex)) return;
            
            EntryList[monitor].NoteIndices.Add(___NoteIndex);
            
            var raw = (NotesManager.GetCurrentMsec() - ___AppearMsec) - NoteJudge.JudgeAdjustMs;
            switch (___JudgeType)
            {
                case NoteJudge.EJudgeType.Tap:
                case NoteJudge.EJudgeType.Break:
                {
                    EntryList[monitor].DiffList[0].Add(___JudgeTimingDiffMsec);
                    EntryList[monitor].RawDiffList[0].Add(raw);
                    break;
                }
                case NoteJudge.EJudgeType.Touch:
                {
                    EntryList[monitor].DiffList[2].Add(___JudgeTimingDiffMsec);
                    EntryList[monitor].RawDiffList[2].Add(raw);
                    break;
                }
                case NoteJudge.EJudgeType.ExTap:
                {
                    EntryList[monitor].DiffList[3].Add(___JudgeTimingDiffMsec);
                    EntryList[monitor].RawDiffList[3].Add(raw);
                    break;
                }
            }
            
            // MelonLogger.Msg($"{___JudgeType}: {___JudgeTimingDiffMsec}, {raw}");
        }
    }
    
    
    [HarmonyPostfix]
    [HarmonyPatch(typeof(SlideRoot), "Judge")]
    private static void SlideRootJudgePatch(
        SlideRoot __instance, bool __result,
        float ___JudgeTimingDiffMsec, float ___TailMsec, float ___lastWaitTimeForJudge, 
        NoteJudge.EJudgeType ___JudgeType, int ___NoteIndex
    )
    {
        var monitor = __instance.MonitorId;
        if (!__result || EntryList[monitor].NoteIndices.Contains(___NoteIndex)) return;
            
        EntryList[monitor].NoteIndices.Add(___NoteIndex);
        
        var raw = (NotesManager.GetCurrentMsec() - ___TailMsec + ___lastWaitTimeForJudge) - NoteJudge.JudgeAdjustMs;
        EntryList[monitor].DiffList[1].Add(___JudgeTimingDiffMsec - NoteJudge.JudgeAdjustMs);
        EntryList[monitor].RawDiffList[1].Add(raw);
        
        // MelonLogger.Msg($"{___JudgeType}: {___JudgeTimingDiffMsec}, {raw}");
    }
    
    
    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultProcess), "OnStart")]
    private static void OnResultProcessStartFinish(
        ResultMonitor[] ____monitors, ResultProcess.ResultScoreViewType[] ____resultScoreViewType, UserData[] ____userData
    )
    {
        foreach (var monitor in ____monitors)
        {
            var idx = monitor.MonitorIndex;
            if (!____userData[idx].IsEntry) continue;
            
            var fileName = $"Acc_Track_{GameManager.MusicTrackNumber}_Player_{idx}.txt";
            var filePath = Path.Combine(Environment.CurrentDirectory, fileName);
            
            using (var writer = new StreamWriter(filePath))
            {
                for (int i = 0; i < TableRowNames.Length; i++)
                {
                    writer.WriteLine($"Row: {TableRowNames[i]}");
                    writer.WriteLine("  DiffList:");
                    writer.WriteLine($"    {string.Join(", ", EntryList[idx].DiffList[i])}");
                    writer.WriteLine("  RawDiffList:");
                    writer.WriteLine($"    {string.Join(", ", EntryList[idx].RawDiffList[i])}");
                    writer.WriteLine();
                }
            }
            
            var controller = Traverse.Create(monitor).Field<ScoreBoardController>("_scoreBoardController").Value;
            var newController = Object.Instantiate(controller, controller.transform);
            newController.gameObject.GetComponent<Animator>().enabled = false;
            newController.transform.localPosition = Vector3.zero;
            var table = ExtractTextObjs(newController);
            for (var i = 0; i < TableHead.Length; i++)
            {
                table[0, i].text = TableHead[i];
            }

            for (var i = 0; i < TableRowNames.Length; i++)
            {
                table[i + 1, 0].text = TableRowNames[i];
                var num = EntryList[idx].DiffList[i].Count;
                table[i + 1, 1].text = num.ToString();
                if (num <= 0)
                {
                    table[i + 1, 2].text = "——";
                    table[i + 1, 3].text = "——";
                    table[i + 1, 4].text = "——";
                    continue;
                }
                
                var average = EntryList[idx].DiffList[i].Average();
                var averageFrame = average * 0.06f;
                table[i + 1, 2].text = averageFrame.ToString("+0.00;-0.00;0.00", CultureInfo.InvariantCulture);
                var averageRawFrame = EntryList[idx].RawDiffList[i].Average() * 0.06f;
                table[i + 1, 3].text = averageRawFrame.ToString("+0.00;-0.00;0.00", CultureInfo.InvariantCulture);

                if (num <= 1)
                {
                    table[i + 1, 4].text = "——";
                }
                else
                {
                    var deviSqr = EntryList[idx].DiffList[i].Sum(x => (x - average) * (x - average)) / (num - 1);
                    var devi = Mathf.Sqrt(deviSqr) * 0.06f;
                    table[i + 1, 4].text = devi.ToString("0.00", CultureInfo.InvariantCulture);
                }
            }
            
            newController.gameObject.SetActive(____resultScoreViewType[idx] == ResultProcess.ResultScoreViewType.VSResult);
            Controllers[idx] = newController;
        }
    }
    
    private static readonly ScoreBoardController[] Controllers = new ScoreBoardController[2];

    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultMonitor), "ChangeScoreBoard")]
    private static void OnChangeScoreBoard(
        ResultMonitor __instance, ResultProcess.ResultScoreViewType resultScoreType
    )
    {
        Controllers[__instance.MonitorIndex].gameObject.SetActive(resultScoreType == ResultProcess.ResultScoreViewType.VSResult);
    }
    

    private static readonly string[] RowNames = ["_tap", "_hold", "_slide", "_touch", "_break"];
    private static readonly string[] ColumnNames = ["_critical", "_perfect", "_great", "_good", "_miss"];
    private static readonly string[] TableHead = ["", "NUM", "AVG", "RAW", "S.D."];
    private static readonly string[] TableRowNames = ["TAP", "SLD", "TCH", "EX"];
    
    private static TextMeshProUGUI[,] ExtractTextObjs(ScoreBoardController controller)
    {
        var result = new TextMeshProUGUI[RowNames.Length, ColumnNames.Length];
        for (var i = 0; i < RowNames.Length; i++)
        {
            for (int j = 0; j < ColumnNames.Length; j++)
            {
                var trav = Traverse.Create(controller)
                    .Field(RowNames[i])
                    .Field(ColumnNames[j]);
                var text = trav.Field<TextMeshProUGUI>("_numberText").Value;
                text.color = Color.black;
                result[i, j] = text;
                trav.GetValue<ScoreBoardColumnObject>().SetVisibleCloseBox(false);
            }
        }
        return result;
    }
    
}

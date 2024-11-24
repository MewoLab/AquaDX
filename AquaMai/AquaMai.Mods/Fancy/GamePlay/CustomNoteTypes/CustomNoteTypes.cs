using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Reflection.Emit;
using AquaMai.Config.Attributes;
using DB;
using HarmonyLib;
using MAI2.Util;
using Manager;
using MelonLoader;
using Monitor;
using UnityEngine;
using AquaMai.Mods.Fancy.GamePlay.CustomNoteTypes.Libs;

namespace AquaMai.Mods.Fancy.GamePlay.CustomNoteTypes;

[ConfigCollapseNamespace]
[ConfigSection(
    en: "Custom Note Types.",
    zh: "自定义 Note 类型"
)]
public class CustomNoteTypes
{
    /*
     * ========== ========== ========== ========== ========== ========== ========== ==========
     * 以下内容是为了添加新的 MA2 语法用于表示自定义的 note 类型
     * The following part is to add new MA2 command to Sinmai (representing custom note types)
     *
     * New note types:
     *     1. Slide Super-new Super-hot (NMSSS, BRSSS, EXSSS, BXSSS, CNSSS):
     *         Definition: ??SSS [bar] [grid] [start pos] [wait] [duration] [end pos] [slide code (string)]
     *         Represent a slide note with highly customized path (using slide code)
     *
     * TODO (?)
     *     Mine notes (P.S. Mine-slides will automatically progress itself)
     *     Individual tracing duration in conn. slides
     *     Touch-slides / slides not ending in group A
     *     Non-C TouchHold
     *     Spinning tailless star (something like 1$$)
     *     Hyper Speed Definition ?
     */
    public static int TotalMa2RecordCount = -1;
    public static int LastMa2RecordID = -1;
    public static Array Ma2FileRecordData;

    public static void OnAfterPatch()
    {
        var arrayTraverse = Traverse.Create(typeof(Ma2fileRecordID)).Field("s_Ma2fileRecord_Data");
        var targetArray = arrayTraverse.GetValue<Array>();

        var nextId = targetArray.Length;
        object[][] newEntries =
        [
            [nextId++, "NMSSS", "过新过热Slide", NotesTypeID.Def.Slide, SlideType.Slide_MAX, 8, Ma2Category.MA2_Note, 2, 2, 2, 2, 2, 2, 0],
            [nextId++, "BRSSS", "过新过热BreakSlide", NotesTypeID.Def.BreakSlide, SlideType.Slide_MAX, 8, Ma2Category.MA2_Note, 2, 2, 2, 2, 2, 2, 0],
            [nextId++, "EXSSS", "过新过热ExSlide", NotesTypeID.Def.ExSlide, SlideType.Slide_MAX, 8, Ma2Category.MA2_Note, 2, 2, 2, 2, 2, 2, 0],
            [nextId++, "BXSSS", "过新过热ExBreakSlide", NotesTypeID.Def.ExBreakSlide, SlideType.Slide_MAX, 8, Ma2Category.MA2_Note, 2, 2, 2, 2, 2, 2, 0],
            [nextId++, "CNSSS", "过新过热ConnSlide", NotesTypeID.Def.ConnectSlide, SlideType.Slide_MAX, 8, Ma2Category.MA2_Note, 2, 2, 2, 2, 2, 2, 0],
        ];

        // Ma2fileRecordID.Ma2fileRecord_Data is private, so we need this shit.
        var structType = targetArray.GetValue(0).GetType();
        var constructor = AccessTools.Constructor(structType,
        [
            typeof(int), typeof(string), typeof(string), typeof(NotesTypeID.Def), typeof(SlideType), typeof(int),
            typeof(Ma2Category), typeof(int), typeof(int), typeof(int), typeof(int), typeof(int), typeof(int),
            typeof(int)
        ]);

        Ma2FileRecordData = Array.CreateInstance(structType, targetArray.Length + newEntries.Length);
        for (var i = 0; i < targetArray.Length; i++)
        {
            Ma2FileRecordData.SetValue(targetArray.GetValue(i), i);
        }

        for (var i = 0; i < newEntries.Length; i++)
        {
            var j = targetArray.Length + i;
            var obj = constructor.Invoke(newEntries[i]);
            Ma2FileRecordData.SetValue(obj, j);
        }

        arrayTraverse.SetValue(Ma2FileRecordData);
        TotalMa2RecordCount = Ma2FileRecordData.Length;
        LastMa2RecordID = TotalMa2RecordCount - 1;
        MelonLogger.Msg($"[CustomNoteType] MA2 record data extended, total count: {TotalMa2RecordCount}");

        // Initialize related classes ...
        SlideDataBuilder.InitializeHitAreasLookup();
        MelonLogger.Msg($"[CustomNoteType] HitAreasLookup initialized, total count: {SlideDataBuilder.HitAreasLookup.Count}");
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(Ma2fileRecordID), "findID")]
    public static bool FindIDPrefix(string enumName, ref Ma2fileRecordID.Def __result)
    {
        // I don't know why but patching findID() leads to a completely invalid result 
        // Sometimes it will even throw an exception
        // So I can only prefix it and override it
        __result = Ma2fileRecordID.Def.Invalid;
        for (var i = 0; i < TotalMa2RecordCount; i++)
        {
            var item = Ma2FileRecordData.GetValue(i);
            if (Traverse.Create(item).Field<string>("enumName").Value == enumName)
            {
                __result = (Ma2fileRecordID.Def)i;
            }
        }

        return false;
    }

    [HarmonyPatch]
    public static class Ma2RecordValidation
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            return
            [
                // AccessTools.Method(typeof(Ma2fileRecordID), "findID"),
                AccessTools.Method(typeof(Ma2fileRecordID), "clamp"),
                AccessTools.Method(typeof(Ma2fileRecordID), "getClampValue"),
                AccessTools.Method(typeof(Ma2fileRecordID), "isValid"),
                AccessTools.Method(typeof(Ma2fileRecordID_Extension), "isValid"),
            ];
        }

        public static IEnumerable<CodeInstruction> Transpiler(IEnumerable<CodeInstruction> instructions)
        {

            foreach (var inst in instructions)
            {
                if (inst.LoadsConstant(142))
                {
                    var instNew = new CodeInstruction(OpCodes.Ldsfld, AccessTools.Field(typeof(CustomNoteTypes), "TotalMa2RecordCount"));
                    yield return instNew;
                }
                else if (inst.LoadsConstant(141))
                {
                    var instNew = new CodeInstruction(OpCodes.Ldsfld, AccessTools.Field(typeof(CustomNoteTypes), "LastMa2RecordID"));
                    yield return instNew;
                }
                else
                {
                    yield return inst;
                }
            }
        }
    }

    /*
     * ========== ========== ========== ========== ========== ========== ========== ==========
     * 以下内容是给新的 MA2 语法写解析器
     */

    /*
     * 给新建的 noteData 初始化应有的数据, 仅仅是照搬了 NotesReader.loadNote
     */
    public static void PrepareBasicNoteData(NoteData noteData, NotesReader reader,
        MA2Record record, int index, ref int noteIndex, OptionMirrorID mirrorMode)
    {
        noteData.type = record.getType().getNotesTypeId();
        noteData.time.init(record.getBar(), record.getGrid(), reader);
        noteData.end = noteData.time;
        noteData.startButtonPos = MaiGeometry.MirrorInfo[(int)mirrorMode, record.getPos()];
        noteData.index = index;
        var num = record.getGrid() % 96;
        if (num == 0)
        {
            noteData.beatType = NoteData.BeatType.BeatType04;
        }
        else if (num % 48 == 0)
        {
            noteData.beatType = NoteData.BeatType.BeatType08;
        }
        else if (num % 24 == 0)
        {
            noteData.beatType = NoteData.BeatType.BeatType16;
        }
        else if (num % 16 == 0)
        {
            noteData.beatType = NoteData.BeatType.BeatType24;
        }
        else
        {
            noteData.beatType = NoteData.BeatType.BeatTypeOther;
        }
        noteData.indexNote = noteIndex;
        ++noteIndex;
    }

    /*
     * 给新建的 noteData 填入基本的 slide 相关数据, 仅仅是照搬了 NotesReader.loadNote
     */
    public static void PrepareBasicSlideData(NoteData noteData, NotesReader reader, MA2Record record, int noteIndex,
        ref int slideIndex, OptionMirrorID mirrorMode)
    {
        noteData.indexSlide = slideIndex++;
        var slideData = noteData.slideData;
        var slideWaitLen = record.getSlideWaitLen();
        var slideShootLen = record.getSlideShootLen();
        slideData.targetNote = MaiGeometry.MirrorInfo[(int)mirrorMode, record.getSlideEndPos()];
        slideData.shoot.time.init(record.getBar(), record.getGrid() + slideWaitLen, reader);
        slideData.shoot.index = noteIndex;
        slideData.arrive.time.init(record.getBar(), record.getGrid() + slideWaitLen + slideShootLen, reader);
        slideData.arrive.index = noteIndex;
        noteData.end = slideData.arrive.time;
    }

    [HarmonyPrefix]
    [HarmonyPatch(typeof(NotesReader), "loadNote")]
    public static bool LoadCustomNote(NotesReader __instance, ref bool __result, NotesData ____note, int ____playerID,
        MA2Record rec, int index, ref int noteIndex, ref int slideIndex)
    {
        if (rec.getType() < Ma2fileRecordID.Def.End)
        {
            // builtin record type
            return true;
        }

        MelonLogger.Msg($"[CustomNoteType] Custom note | {rec._str.Count} | {rec.getStr(0)} {rec.getStr(1)} {rec.getStr(2)} {rec.getStr(3)} {rec.getStr(4)} {rec.getStr(5)} {rec.getStr(6)} {rec.getStr(7)} {rec.getStr(8)}");

        var flag = true;
        switch (rec.getType().getEnumName())
        {
            case "NMSSS":
            case "BRSSS":
            case "EXSSS":
            case "BXSSS":
            case "CNSSS":
                var noteData = new CustomSlideNoteData();
                var mirrorMode = Singleton<GamePlayManager>.Instance.GetGameScore(____playerID).UserOption.MirrorMode;
                PrepareBasicNoteData(noteData, __instance, rec, index, ref noteIndex, mirrorMode);
                PrepareBasicSlideData(noteData, __instance, rec, noteIndex, ref slideIndex, mirrorMode);
                var success = noteData.ParseSlideCode(rec.getStr(7), mirrorMode);
                if (success)
                {
                    ____note._noteData.Add(noteData);
                }
                else
                {
                    flag = false;
                }
                break;
            default:
                flag = false;
                break;
        }
        __result = flag;
        return false;
    }

    /*
     * ========== ========== ========== ========== ========== ========== ========== ==========
     * 以下内容是为了实现自定义 Slide
     * 
     */

    /*
     * 把 GetSlidePath 和 GetSlideHitArea 和 GetSlideLength 重定向到我可以控制的函数上, 并且多推几个参数进来
     */
    [HarmonyPatch]
    public static class SlideNoteDataHack
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            return
            [
                AccessTools.Method(typeof(SlideRoot), "Initialize"),
                AccessTools.Method(typeof(SlideRoot), "GetSlideArrowNum", [typeof(NoteData)]),
                AccessTools.Method(typeof(StarNote), "Initialize"),
                AccessTools.Method(typeof(BreakStarNote), "Initialize"),
            ];
        }

        public static IEnumerable<CodeInstruction> Transpiler(IEnumerable<CodeInstruction> instructions)
        {
            var methodGetSlidePath = AccessTools.Method(typeof(SlideManager), "GetSlidePath");
            var methodGetSlidePathRedirect = AccessTools.Method(typeof(CustomNoteTypes), "GetSlidePathRedirect");
            var methodGetSlideHitArea = AccessTools.Method(typeof(SlideManager), "GetSlideHitArea");
            var methodGetSlideHitAreaRedirect = AccessTools.Method(typeof(CustomNoteTypes), "GetSlideHitAreaRedirect");
            var methodGetSlideLength = AccessTools.Method(typeof(SlideManager), "GetSlideLength");
            var methodGetSlideLengthRedirect = AccessTools.Method(typeof(CustomNoteTypes), "GetSlideLengthRedirect");
            var fieldSlideData = AccessTools.Field(typeof(NoteData), "slideData");

            var oldInstList = new List<CodeInstruction>(instructions);
            var newInstList = new List<CodeInstruction>();
            CodeInstruction instToInject = null;

            for (var i = 0; i < oldInstList.Count; ++i)
            {
                var inst = oldInstList[i];
                if (inst.LoadsField(fieldSlideData))
                {
                    // 以 GetSlidePath 为例, 我们需要把下面这个调用:
                    // Singleton<SlideManager>.Instance.GetSlidePath(
                    //     noteData.slideData.type, noteData.startButtonPos,
                    //     noteData.slideData.targetNote, this.ButtonId
                    // )
                    // 里的 noteData 拿到手
                    // 所以就记录上一次 ldfld NoteData::slideData 的位置, 往前找一个 IL code
                    // 找到的就是 load 这个 noteData 的位置
                    // 然后在后续调用 GetSlidePath 时, 先重复一遍 load 把这个 noteData 入栈, 然后重定向到一个新的函数上去
                    instToInject = oldInstList[i - 1];
                    newInstList.Add(inst);
                }
                else if (inst.Calls(methodGetSlidePath))
                {
                    newInstList.Add(instToInject!.Clone());
                    newInstList.Add(new CodeInstruction(OpCodes.Call, methodGetSlidePathRedirect));
                    instToInject = null;
                }
                else if (inst.Calls(methodGetSlideHitArea))
                {
                    newInstList.Add(instToInject!.Clone());
                    newInstList.Add(new CodeInstruction(OpCodes.Call, methodGetSlideHitAreaRedirect));
                    instToInject = null;
                }
                else if (inst.Calls(methodGetSlideLength))
                {
                    newInstList.Add(instToInject!.Clone());
                    newInstList.Add(new CodeInstruction(OpCodes.Call, methodGetSlideLengthRedirect));
                    instToInject = null;
                }
                else
                {
                    newInstList.Add(inst);
                }
            }
            return newInstList;
        }
    }


    public static List<Vector4> GetSlidePathRedirect(SlideManager instance, SlideType slideType, int start, int end,
        int starButton, NoteData noteData)
    {
        // MelonLogger.Msg($"[CustomNoteType] GetSlidePath Redirected!");
        // MelonLogger.Msg($"{noteData.indexNote} {noteData.indexSlide} {slideType} {start} {end} {starButton}");
        if (noteData is CustomSlideNoteData data)
        {
            // MelonLogger.Msg($"[CustomNoteType] Successfully injected custom path {data.SlideCode}");
            return data.SlidePathList[starButton];
        }
        return instance.GetSlidePath(slideType, start, end, starButton);
    }

    public static List<SlideManager.HitArea> GetSlideHitAreaRedirect(SlideManager instance, SlideType slideType,
        int start, int end, int starButton, NoteData noteData)
    {
        // MelonLogger.Msg($"[CustomNoteType] GetSlideHitArea Redirected!");
        // MelonLogger.Msg($"{noteData.indexNote} {noteData.indexSlide} {slideType} {start} {end} {starButton}");
        if (noteData is CustomSlideNoteData data)
        {
            // MelonLogger.Msg($"[CustomNoteType] Successfully injected custom hit areas {data.SlideCode}");
            return data.SlideHitAreasList[starButton];
        }
        return instance.GetSlideHitArea(slideType, start, end, starButton);
    }

    public static float GetSlideLengthRedirect(SlideManager instance, SlideType slideType,
        int start, int end, NoteData noteData)
    {
        // MelonLogger.Msg($"[CustomNoteType] GetSlideLength Redirected!");
        // MelonLogger.Msg($"{noteData.indexNote} {noteData.indexSlide} {slideType} {start} {end}");
        if (noteData is CustomSlideNoteData data)
        {
            // MelonLogger.Msg($"[CustomNoteType] Successfully injected custom path length {data.SlideCode}");
            return data.SlidePathLength;
        }
        return instance.GetSlideLength(slideType, start, end);
    }



    [HarmonyPatch]
    public static class Debuging
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            return
            [
                AccessTools.Method(typeof(SlideRoot), "Initialize"),
                // AccessTools.Method(typeof(SlideRoot), "GetSlideArrowNum", []),
                // AccessTools.Method(typeof(SlideRoot), "GetSlideArrowNum", [typeof(NoteData)]),
                // AccessTools.Method(typeof(SlideRoot), "GetArrowData"),
                // AccessTools.Method(typeof(SlideRoot), "totalDistance"),
                // AccessTools.Method(typeof(SlideRoot), "GetActiveArrowNum"),
                // AccessTools.Method(typeof(SlideJudge), "SetJudgeType"),
            ];
        }

        public static void Prefix(MethodBase __originalMethod, object[] __args)
        {
            var msg = "[CustomNoteType] Before ";
            msg += __originalMethod.DeclaringType!.FullName + "." + __originalMethod.Name + " (";
            var infos = __originalMethod.GetParameters()
                .Select((x, i) => x.ParameterType.FullName + " " + x.Name + " = " + GetString(__args[i]))
                .ToArray();
            msg += infos.Length > 0 ? infos.Aggregate((a, b) => a + ", " + b) : "void";
            msg += ")";
            MelonLogger.Msg(msg);
        }

        public static void Postfix(MethodBase __originalMethod, object[] __args)
        {
            var msg = "[CustomNoteType] After ";
            msg += __originalMethod.DeclaringType!.FullName + "." + __originalMethod.Name + " (";
            var infos = __originalMethod.GetParameters()
                .Select((x, i) => x.ParameterType.FullName + " " + x.Name + " = " + GetString(__args[i]))
                .ToArray();
            msg += infos.Length > 0 ? infos.Aggregate((a, b) => a + ", " + b) : "void";
            msg += ")";
            MelonLogger.Msg(msg);
        }

        public static string GetString(object value)
        {
            if (value is CustomSlideNoteData data)
            {
                return $"<CustomSlideNoteData {data.indexNote} {data.indexSlide} {data.SlideCode}>";
            }

            if (value is NoteData data2)
            {
                return $"<NoteData {data2.indexNote} {data2.indexSlide}>";
            }

            return value.ToString();
        }
    }
}

using System;
using System.Collections.Generic;
using System.IO;
using HarmonyLib;
using Mai2.Mai2Cue;
using MAI2.Util;
using Manager;
using MelonLoader;

namespace AquaMai.Mods.UX
{
    public class RandomBgm
    {
        private static List<string> _acbs = new List<string>();

        [HarmonyPostfix]
        [HarmonyPatch(typeof(SoundManager), "Initialize")]
        public static void Init()
        {
            if (!Directory.Exists(Path.Combine(Environment.CurrentDirectory, "LocalAssets", "Mai2Cue"))) return;
            var files = Directory.EnumerateFiles(Path.Combine(Environment.CurrentDirectory, "LocalAssets", "Mai2Cue"));
            foreach (var file in files)
            {
                if (!file.EndsWith(".acb")) continue;
                // Seems there's limit for max opened ACB files
                _acbs.Add(Path.ChangeExtension(file, null));
            }

            MelonLogger.Msg($"Random BGM loaded {_acbs.Count} files");
        }

        [HarmonyPrefix]
        [HarmonyPatch(typeof(SoundManager), "Play")]
        public static void PrePlay(ref SoundManager.AcbID acbID, int cueID)
        {
            if (acbID != SoundManager.AcbID.Default) return;
            if (_acbs.Count == 0) return;
            var cueIndex = (Cue)cueID;
            switch (cueIndex)
            {
                case Cue.BGM_ENTRY:
                case Cue.BGM_COLLECTION:
                case Cue.BGM_RESULT_CLEAR:
                case Cue.BGM_RESULT:
                    var acb = _acbs[UnityEngine.Random.Range(0, _acbs.Count)];
                    acbID = SoundManager.AcbID.Max;
                    var result = Singleton<SoundCtrl>.Instance.LoadCueSheet((int)acbID, acb);
                    MelonLogger.Msg($"Picked {acb} for {cueIndex}, result: {result}");
                    return;
                default:
                    return;
            }
        }

        [HarmonyPrefix]
        [HarmonyPatch(typeof(SoundManager), "PlayBGM")]
        public static bool PrePlayBGM(ref int target)
        {
            switch (target)
            {
                case 0:
                    return true;
                case 1:
                    return false;
                case 2:
                    target = 0;
                    return true;
                default:
                    return false;
            }
        }
    }
}

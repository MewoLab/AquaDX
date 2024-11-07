using System;
using System.Diagnostics;
using HarmonyLib;
using MelonLoader;
using Process;

namespace AquaMai.Mods.UX
{
    public class RunCommandOnEvents
    {
        [HarmonyPrefix]
        [HarmonyPatch(typeof(AdvertiseProcess), "OnStart")]
        public static void AdvertiseProcessPreStart()
        {
            if (!string.IsNullOrWhiteSpace(AquaMai.AppConfig.UX.ExecOnIdle))
            {
                Exec(AquaMai.AppConfig.UX.ExecOnIdle);
            }
        }

        [HarmonyPrefix]
        [HarmonyPatch(typeof(EntryProcess), "OnStart")]
        public static void EntryProcessPreStart()
        {
            if (!string.IsNullOrWhiteSpace(AquaMai.AppConfig.UX.ExecOnEntry))
            {
                Exec(AquaMai.AppConfig.UX.ExecOnEntry);
            }
        }

        [HarmonyPrefix]
        [HarmonyPatch(typeof(MusicSelectProcess), "OnStart")]
        public static void MusicSelectProcessPreStart()
        {
            if (!string.IsNullOrWhiteSpace(AquaMai.AppConfig.UX.ExecOnEntry))
            {
                Exec(AquaMai.AppConfig.UX.ExecOnEntry);
            }
        }

        private static void Exec(string command)
        {
            var process = new System.Diagnostics.Process();
            process.StartInfo.FileName = "cmd.exe";
            process.StartInfo.Arguments = "/c " + command;
            process.StartInfo.UseShellExecute = true;
            process.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
            process.StartInfo.WorkingDirectory = Environment.CurrentDirectory;

            process.Start();
        }
    }
}

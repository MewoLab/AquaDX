using AquaMai.Config.Attributes;
using AquaMai.Config.Types;
using AquaMai.Core.Helpers;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Process;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "One key to retry (1.30+) or skip current chart in gameplay",
    zh: "在游戏中途一键重试（1.30+）或跳过当前谱面")]
public class OneKeyRetrySkip
{
    [ConfigEntry]
    public static readonly KeyCodeOrName RetryKey = KeyCodeOrName.Service;

    [ConfigEntry]
    public static readonly bool RetryLongPress = false;

    [ConfigEntry]
    public static readonly KeyCodeOrName SkipKey = KeyCodeOrName.Service;

    [ConfigEntry]
    public static readonly bool SkipLongPress = true;

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameProcess), "OnUpdate")]
    public static void PostGameProcessUpdate(GameProcess __instance, Message[] ____message, ProcessDataContainer ___container)
    {
        if (KeyListener.GetKeyDownOrLongPress(SkipKey, SkipLongPress))
        {
            var traverse = Traverse.Create(__instance);
            ___container.processManager.SendMessage(____message[0]);
            Singleton<GamePlayManager>.Instance.SetSyncResult(0);
            traverse.Method("SetRelease").GetValue();
        }

        if (KeyListener.GetKeyDownOrLongPress(RetryKey, RetryLongPress) && GameInfo.GameVersion >= 23000)
        {
            // This is original typo in Assembly-CSharp
            Singleton<GamePlayManager>.Instance.SetQuickRetryFrag(flag: true);
        }
    }
}

using AquaMai.Config.Attributes;
using UnityEngine;

namespace AquaMai.Mods.GameSystem;

[ConfigSection(
    en: "Force the frame rate limit to 60 FPS and disable vSync. Do not use if your game has no issues",
    zh: "强制设置帧率上限为 60 帧并关闭垂直同步。如果你的游戏没有问题，请不要使用")]
public class LockFrameRate
{
    public static void OnBeforePatch(HarmonyLib.Harmony h)
    {
        Application.targetFrameRate = 60;
        QualitySettings.vSyncCount = 0;
    }
}

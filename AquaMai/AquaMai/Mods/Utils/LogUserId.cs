using System;
using AquaMai.Config.Attributes;
using HarmonyLib;
using MelonLoader;
using Net.Packet;
using Net.Packet.Mai2;
using Net.VO.Mai2;

namespace AquaMai.Mods.Utils;

[ConfigSection(
    en: "Log user ID on login",
    zh: "登录时将 UserID 输出到日志")]
public class LogUserId
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(PacketGetUserPreview), MethodType.Constructor, typeof(ulong), typeof(string), typeof(Action<ulong, UserPreviewResponseVO>), typeof(Action<PacketStatus>))]
    public static void Postfix(ulong userId)
    {
        MelonLogger.Msg($"UserLogin: {userId}");
    }
}

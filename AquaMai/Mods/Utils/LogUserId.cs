using System;
using HarmonyLib;
using MelonLoader;
using Net.Packet;
using Net.Packet.Mai2;
using Net.VO.Mai2;

namespace AquaMai.Mods.Utils;

public class LogUserId
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(PacketGetUserPreview), MethodType.Constructor, typeof(ulong), typeof(string), typeof(Action<ulong, UserPreviewResponseVO>), typeof(Action<PacketStatus>))]
    public static void Postfix(ulong userId)
    {
        MelonLogger.Msg($"UserLogin: {userId}");
    }
}

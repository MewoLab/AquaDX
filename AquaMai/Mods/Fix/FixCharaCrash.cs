using System;
using System.Collections.Generic;
using AquaMai.Attributes;
using HarmonyLib;
using Process;
using Util;

namespace AquaMai.Mods.Fix;

/**
 * Fix character selection crashing due to missing character data
 */
[ConfigSection(defaultOn: true, hideInExample: true)]
public class FixCharaCrash
{
    // Check if the return is null. If it is, make up a color
    [HarmonyPostfix]
    [HarmonyPatch(typeof(CharacterSelectProces), "GetMapColorData")]
    public static void GetMapColorData(ref CharacterSelectProces __instance, ref CharacterMapColorData __result)
    {
        if (__result != null) return;

        // 1 is a color that definitely exists
        if (MapMaster.GetSlotData(1) == null)
        {
            MapMaster.GetSlotData(1).Load();
        }
        __result = MapMaster.GetSlotData(1);
    }

    // This is called when loading the music selection screen, to display characters on the top screen
    [HarmonyPrefix]
    [HarmonyPatch(typeof(Monitor.CommonMonitor), "SetCharacterSlot", new Type[] { typeof(MessageCharactorInfomationData) })]
    public static bool SetCharacterSlot(ref MessageCharactorInfomationData data, Dictionary<int, CharacterSlotData> ____characterSlotData)
    {
        // Some characters are not found in this dictionary. We simply skip loading those characters
        if (!____characterSlotData.ContainsKey(data.MapKey))
        {
            Console.Log($"Could not get CharacterSlotData for character [Index={data.Index}, MapKey={data.MapKey}], ignoring...");
            return false;
        }

        return true;
    }
}

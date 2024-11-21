using AquaMai.Core.Attributes;
using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;

namespace AquaMai.Mods.GameSystem;

[ConfigSection(
    en: "Hold the bottom four buttons (3456) for quick retry (like in Freedom Mode, default non-utage only)",
    zh: "按住下方四个按钮（3456）快速重开本局游戏（像在 Freedom Mode 中一样，默认仅对非宴谱有效）")]
[EnableGameVersion(23000)]
public class QuickRetry
{
    [ConfigEntry(
        en: "Force enable in Utage",
        zh: "在宴谱中强制启用")]
    private static readonly bool EnableInUtage = false;

    [HarmonyPrefix]
    [HarmonyPatch(typeof(QuickRetry), "IsQuickRetryEnable")]
    public static bool OnQuickRetryIsQuickRetryEnable(ref bool __result)
    {
        if (EnableInUtage)
        {
            __result = true;
        }
        else
        {
            var isUtageProperty = Traverse.Create(typeof(GameManager)).Property("IsUtage");
            __result = !isUtageProperty.PropertyExists() || !isUtageProperty.GetValue<bool>();
        }
        return false;
    }
}

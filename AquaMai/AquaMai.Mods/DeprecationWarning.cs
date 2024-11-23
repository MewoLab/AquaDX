using AquaMai.Config.Attributes;

namespace AquaMai.Mods;

[ConfigSection(
    en: """
        These options have been deprecated and no longer work in the current version.
        Remove them to get rid of the warning message at startup.
        """,
    zh: """
        这些配置项已经被废弃，在当前版本不再生效
        删除它们以去除启动时的警告信息
        """,
    exampleHidden: true)]
public class DeprecationWarning
{
    [ConfigEntry(hideWhenDefault: true)]
    public static readonly bool v1_0_ModKeyMap_TestMode;

    // Print friendly warning messages here.
    // Please keep them up-to-date while refactoring the config.
    public static void OnBeforeAllPatch()
    {
        if (v1_0_ModKeyMap_TestMode)
        {
            MelonLoader.MelonLogger.Warning("ModKeyMap.TestMode has been deprecated (> v1.0). Please use GameSystem.KeyMap.Test instead.");
        }
    }
}

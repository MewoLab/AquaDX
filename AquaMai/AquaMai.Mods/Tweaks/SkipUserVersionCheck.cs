using AquaMai.Config.Attributes;
using HarmonyLib;
using Process.Entry.State;

namespace AquaMai.Mods.Tweaks;

[ConfigSection(
    en: "Allow login with higher data version.",
    zh: """
        原先如果你的账号版本比当前游戏设定的版本高的话，就会不能登录
        开了这个选项之后就可以登录了，不过你的账号版本还是会被设定为当前游戏的版本
        """)]
public class SkipUserVersionCheck
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(ConfirmPlay), "IsValidVersion")]
    public static bool IsValidVersion(ref bool __result)
    {
        __result = true;
        return false;
    }
}

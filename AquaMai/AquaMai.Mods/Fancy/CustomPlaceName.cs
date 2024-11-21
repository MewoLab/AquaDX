using AquaMai.Config.Attributes;
using HarmonyLib;
using Manager;

namespace AquaMai.Mods.Fancy;

[ConfigSection(
    en: """
        Custom shop name in photo
        Also enable shop name display in SDGA
        """,
    zh: """
        自定义拍照的店铺名称
        同时在 SDGA 中会启用店铺名称的显示（但是不会在游戏里有设置）
        """)]
public class CustomPlaceName
{
    [ConfigEntry]
    private static readonly string PlaceName = "";

    [HarmonyPostfix]
    [HarmonyPatch(typeof(OperationManager), "CheckAuth_Proc")]
    public static void CheckAuth_Proc(OperationManager __instance)
    {
        if (string.IsNullOrEmpty(PlaceName))
        {
            return;
        }

        __instance.ShopData.ShopName = PlaceName;
        __instance.ShopData.ShopNickName = PlaceName;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultCardBaseController), "Initialize")]
    public static void Initialize(ResultCardBaseController __instance)
    {
        if (string.IsNullOrEmpty(PlaceName))
        {
            return;
        }

        __instance.SetVisibleStoreName(true);
    }
}

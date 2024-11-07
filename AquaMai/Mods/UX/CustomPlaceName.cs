using HarmonyLib;
using Manager;

namespace AquaMai.Mods.UX;

public class CustomPlaceName
{
    [HarmonyPostfix]
    [HarmonyPatch(typeof(OperationManager), "CheckAuth_Proc")]
    public static void CheckAuth_Proc(OperationManager __instance)
    {
        if (string.IsNullOrEmpty(AquaMai.AppConfig.UX.CustomPlaceName))
        {
            return;
        }

        __instance.ShopData.ShopName = AquaMai.AppConfig.UX.CustomPlaceName;
        __instance.ShopData.ShopNickName = AquaMai.AppConfig.UX.CustomPlaceName;
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultCardBaseController), "Initialize")]
    public static void Initialize(ResultCardBaseController __instance)
    {
        if (string.IsNullOrEmpty(AquaMai.AppConfig.UX.CustomPlaceName))
        {
            return;
        }

        __instance.SetVisibleStoreName(true);
    }
}

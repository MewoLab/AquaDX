using DB;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Process;

namespace AquaMai.Mods.UX
{
    public class DemoMaster
    {
        [HarmonyPostfix]
        [HarmonyPatch(typeof(AdvDemoProcess), "OnStart")]
        public static void AdvDemoProcessPostStart()
        {
            for (int i = 0; i < 2; i++)
            {
                var userOption = Singleton<GamePlayManager>.Instance.GetGameScore(i).UserOption;
                userOption.NoteSpeed = OptionNotespeedID.Speed6_5;
                userOption.TouchSpeed = OptionTouchspeedID.Speed7_0;
            }
        }

        [HarmonyPrefix]
        [HarmonyPatch(typeof(GamePlayManager), "InitializeAdvertise")]
        public static void PreInitializeAdvertise()
        {
            GameManager.SelectDifficultyID[0] = 3;
            GameManager.SelectDifficultyID[1] = 3;
        }
    }
}

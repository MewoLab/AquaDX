using AquaMai.Config.Attributes;
using HarmonyLib;
using Monitor;
using TMPro;
using UI;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: """
        Disable the TRACK X text, DX/Standard display box, and the derakkuma at the bottom of the screen in the song start screen.
        For recording chart confirmation.
        """,
    zh: """
        在歌曲开始界面, 把 TRACK X 字样, DX/标准谱面的显示框, 以及画面下方的滴蜡熊隐藏掉
        录制谱面确认用
        """)]
public class DisableTrackStartTabs
{
    // 在歌曲开始界面, 把 TRACK X 字样, DX/标准谱面的显示框, 以及画面下方的滴蜡熊隐藏掉, 让他看起来不那么 sinmai, 更像是 majdata

    [HarmonyPostfix]
    [HarmonyPatch(typeof(TrackStartMonitor), "SetTrackStart")]
    private static void DisableTabs(
        SpriteCounter ____trackNumber, SpriteCounter ____bossTrackNumber, SpriteCounter ____utageTrackNumber,
        MultipleImage ____musicTabImage, GameObject[] ____musicTabObj, GameObject ____derakkumaRoot,
        TimelineRoot ____musicDetail
    )
    {
        ____trackNumber.transform.parent.gameObject.SetActive(false);
        ____bossTrackNumber.transform.parent.gameObject.SetActive(false);
        ____utageTrackNumber.transform.parent.gameObject.SetActive(false);
        ____musicTabImage.gameObject.SetActive(false);
        ____musicTabObj[0].gameObject.SetActive(false);
        ____musicTabObj[1].gameObject.SetActive(false);
        ____musicTabObj[2].gameObject.SetActive(false);
        ____derakkumaRoot.SetActive(false);
        var traverse = Traverse.Create(____musicDetail);
        traverse.Field<MultipleImage>("_achivement_Base").Value.ChangeSprite(1);
        traverse.Field<MultipleImage>("_clearRank_Base").Value.ChangeSprite(1);
        traverse.Field<TextMeshProUGUI>("_achivement_Text").Value.gameObject.SetActive(false);
        traverse.Field<TextMeshProUGUI>("_achivement_decimal_Text").Value.gameObject.SetActive(false);
        traverse.Field<TextMeshProUGUI>("_achivement_percent_Text").Value.gameObject.SetActive(false);
        traverse.Field<MultipleImage>("_clearRank_Image").Value.gameObject.SetActive(false);
        traverse.Field<GameObject>("_deluxScore_Obj").Value.SetActive(false);
        traverse.Field<MultipleImage>("_comboRank_Image").Value.ChangeSprite(0);
        traverse.Field<MultipleImage>("_syncRank_Image").Value.ChangeSprite(0);
    }
}

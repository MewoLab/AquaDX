using System.Collections.Generic;
using System.Linq;
using AquaMai.Attributes;
using AquaMai.Helpers;
using AquaMai.Resources;
using HarmonyLib;
using MAI2.Util;
using Manager;
using Manager.MaiStudio;
using Manager.UserDatas;
using Monitor;
using Process;
using UnityEngine;

namespace AquaMai.Utils;

[GameVersion(23500)]
public class SelectionDetail
{
    private static readonly Window[] window = new Window[2];
    private static MusicSelectProcess.MusicSelectData SelectData { get; set; }
    private static readonly int[] difficulty = new int[2];

    [HarmonyPostfix]
    [HarmonyPatch(typeof(MusicSelectMonitor), "UpdateRivalScore")]
    public static void ScrollUpdate(MusicSelectProcess ____musicSelect, MusicSelectMonitor __instance)
    {
        int player;
        if (__instance == ____musicSelect.MonitorArray[0])
        {
            player = 0;
        }
        else if (__instance == ____musicSelect.MonitorArray[1])
        {
            player = 1;
        }
        else
        {
            return;
        }

        if (window[player] != null)
        {
            window[player].Close();
        }

        var userData = Singleton<UserDataManager>.Instance.GetUserData(player);
        if (!userData.IsEntry) return;

        if (____musicSelect.IsRandomIndex()) return;

        SelectData = ____musicSelect.GetMusic(0);
        if (SelectData == null) return;
        difficulty[player] = ____musicSelect.GetDifficulty(player);

        window[player] = player == 0 ? __instance.gameObject.AddComponent<P1Window>() : __instance.gameObject.AddComponent<P2Window>();
    }

    private class P1Window : Window
    {
        protected override int player => 0;
    }

    private class P2Window : Window
    {
        protected override int player => 1;
    }

    private abstract class Window : MonoBehaviour
    {
        protected abstract int player { get; }
        private UserData userData => Singleton<UserDataManager>.Instance.GetUserData(player);

        public void OnGUI()
        {
            var dataToShow = new List<string>();
            dataToShow.Add($"ID: {SelectData.MusicData.name.id}");
            dataToShow.Add(MusicDirHelper.LookupPath(SelectData.MusicData.name.id).Split('/').Reverse().ToArray()[3]);
            if (SelectData.MusicData.genreName is not null) // SelectData.MusicData.genreName.str may not correct
                dataToShow.Add(Singleton<DataManager>.Instance.GetMusicGenre(SelectData.MusicData.genreName.id)?.genreName);
            if (SelectData.MusicData.AddVersion is not null)
                dataToShow.Add(Singleton<DataManager>.Instance.GetMusicVersion(SelectData.MusicData.AddVersion.id)?.genreName);
            var notesData = SelectData.MusicData.notesData[difficulty[player]];
            dataToShow.Add($"{notesData?.level}.{notesData?.levelDecimal}");

            var rate = CalcB50(SelectData.MusicData, difficulty[player]);
            if (rate > 0)
            {
                dataToShow.Add(string.Format(Locale.RatingUpWhenSSSp, rate));
            }

            var playCount = Shim.GetUserScoreList(userData)[difficulty[player]].FirstOrDefault(it => it.id == SelectData.MusicData.name.id)?.playcount ?? 0;
            if (playCount > 0)
            {
                dataToShow.Add(string.Format(Locale.PlayCount, playCount));
            }


            var width = GuiSizes.FontSize * 15f;
            var x = GuiSizes.PlayerCenter - width / 2f + GuiSizes.PlayerWidth * player;
            var y = Screen.height * 0.87f;

            var labelStyle = GUI.skin.GetStyle("label");
            labelStyle.fontSize = GuiSizes.FontSize;
            labelStyle.alignment = TextAnchor.MiddleCenter;

            GUI.Box(new Rect(x, y, width, dataToShow.Count * GuiSizes.LabelHeight + 2 * GuiSizes.Margin), "");
            for (var i = 0; i < dataToShow.Count; i++)
            {
                GUI.Label(new Rect(x, y + GuiSizes.Margin + i * GuiSizes.LabelHeight, width, GuiSizes.LabelHeight), dataToShow[i]);
            }
        }

        private uint CalcB50(MusicData musicData, int difficulty)
        {
            var theory = new UserRate(musicData.name.id, difficulty, 1010000, (uint)musicData.version);
            var list = theory.OldFlag ? userData.RatingList.RatingList : userData.RatingList.NewRatingList;
            var userLowRate = list.Last();
            var userSongRate = list.FirstOrDefault(it => it.MusicId == musicData.name.id && it.Level == difficulty);

            if (!userSongRate.Equals(default(UserRate)))
            {
                return theory.SingleRate - userSongRate.SingleRate;
            }

            if (theory.SingleRate > userLowRate.SingleRate)
            {
                return theory.SingleRate - userLowRate.SingleRate;
            }

            return 0;
        }

        public void Close()
        {
            Destroy(this);
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using AquaMai.Attributes;
using AquaMai.Helpers;
using AquaMai.Resources;
using DB;
using HarmonyLib;
using MAI2.Util;
using MAI2System;
using Manager;
using Manager.MaiStudio;
using Manager.UserDatas;
using MelonLoader;
using Monitor.Entry.Parts.Screens;
using Net.Packet;
using Net.Packet.Helper;
using Process;
using Process.UserDataNet.State.UserDataULState;
using UnityEngine;

namespace AquaMai.Mods.UX;

[ConfigSection(
    en: "Save immediate after playing a song",
    zh: "打完一首歌的时候立即向服务器保存成绩")]
public class ImmediateSave
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(StateULUserAime), "RequestUploadUserPlayLogData")]
    public static bool PreRequestUploadUserPlayLogData(StateULUserAime __instance)
    {
        Traverse.Create(__instance).Method("RequestUploadUserPortraitData").GetValue();
        return false;
    }

    private static SavingUi ui;

    [HarmonyPostfix]
    [HarmonyPatch(typeof(ResultProcess), "OnStart")]
    public static void ResultProcessOnStart()
    {
        var doneCount = 0;

        void CheckSaveDone()
        {
            doneCount++;
            if (doneCount != 4) return;
            if (ui == null) return;
            UnityEngine.Object.Destroy(ui);
            ui = null;
        }

        for (int i = 0; i < 2; i++)
        {
            var userData = Singleton<UserDataManager>.Instance.GetUserData(i);
            if (!userData.IsEntry || userData.IsGuest())
            {
                doneCount += 2;
                continue;
            }

            if (ui == null)
            {
                ui = SharedInstances.GameMainObject.gameObject.AddComponent<SavingUi>();
            }

            SaveDataFix(userData);
            PacketHelper.StartPacket(Shim.CreatePacketUploadUserPlaylog(i, userData, (int)GameManager.MusicTrackNumber - 1,
                delegate
                {
                    MelonLogger.Msg("Playlog saved");
                    CheckSaveDone();
                },
                delegate(PacketStatus err)
                {
                    SoundManager.PlaySE(Mai2.Mai2Cue.Cue.SE_ENTRY_AIME_ERROR, i);
                    MelonLogger.Error("Playlog save error");
                    MelonLogger.Error(err);
                    MessageHelper.ShowMessage("Playlog save error");
                    CheckSaveDone();
                }));
            PacketHelper.StartPacket(Shim.CreatePacketUpsertUserAll(i, userData, delegate(int code)
            {
                if (code == 1)
                {
                    MelonLogger.Msg("UserAll saved");
                    CheckSaveDone();
                }
                else
                {
                    SoundManager.PlaySE(Mai2.Mai2Cue.Cue.SE_ENTRY_AIME_ERROR, i);
                    MelonLogger.Error("UserAll upsert error");
                    MelonLogger.Error(code);
                    MessageHelper.ShowMessage("UserAll upsert error");
                    CheckSaveDone();
                }
            }, delegate(PacketStatus err)
            {
                SoundManager.PlaySE(Mai2.Mai2Cue.Cue.SE_ENTRY_AIME_ERROR, i);
                MelonLogger.Error("UserAll upsert error");
                MelonLogger.Error(err);
                MessageHelper.ShowMessage("UserAll upsert error");
                CheckSaveDone();
            }));
        }
    }


    private static void SaveDataFix(UserData userData)
    {
        UserDetail detail = userData.Detail;
        detail.EventWatchedDate = TimeManager.GetDateString(TimeManager.PlayBaseTime);
        userData.CalcTotalValue();
        float num = 0f;
        try
        {
            if (userData.RatingList.RatingList.Any())
            {
                num = userData.RatingList.RatingList.Last().SingleRate;
            }
        }
        catch
        {
        }

        num = (float)Math.Ceiling((double)((num + 1f) / GameManager.TheoryRateBorderNum) * 10.0);
        float num2 = 0f;
        try
        {
            if (userData.RatingList.NextRatingList.Any())
            {
                num2 = userData.RatingList.NewRatingList.Last().SingleRate;
            }
        }
        catch
        {
        }

        num2 = (float)Math.Ceiling((double)((num2 + 1f) / GameManager.TheoryRateBorderNum) * 10.0);
        string logDateString = TimeManager.GetLogDateString(TimeManager.PlayBaseTime);
        string timeJp = (string.IsNullOrEmpty(userData.Detail.DailyBonusDate) ? TimeManager.GetLogDateString(0L) : userData.Detail.DailyBonusDate);
        if (userData.IsEntry && userData.Detail.IsNetMember >= 2 && !GameManager.IsEventMode && TimeManager.GetUnixTime(logDateString) > TimeManager.GetUnixTime(timeJp) && Singleton<UserDataManager>.Instance.IsSingleUser() && !GameManager.IsFreedomMode && !GameManager.IsCourseMode && !DoneEntry.IsWeekdayBonus(userData))
        {
            userData.Detail.DailyBonusDate = logDateString;
        }

        List<UserRate> list = new List<UserRate>();
        List<UserRate> list2 = new List<UserRate>();
        IEnumerable<UserScore>[] scoreList = Shim.GetUserScoreList(userData);
        List<UserRate> ratingList = userData.RatingList.RatingList;
        List<UserRate> newRatingList = userData.RatingList.NewRatingList;
        int achive = RatingTableID.Rate_22.GetAchive();
        for (int j = 0; j < scoreList.Length; j++)
        {
            if (scoreList[j] == null)
            {
                continue;
            }

            foreach (UserScore item2 in scoreList[j])
            {
                if (achive <= item2.achivement)
                {
                    continue;
                }

                MusicData music = Singleton<DataManager>.Instance.GetMusic(item2.id);
                if (music == null)
                {
                    continue;
                }

                UserRate item = new UserRate(item2.id, j, item2.achivement, (uint)music.version);
                if (item.OldFlag)
                {
                    if (num <= (float)item.Level && !ratingList.Contains(item))
                    {
                        list.Add(item);
                    }
                }
                else if (num2 <= (float)item.Level && !newRatingList.Contains(item))
                {
                    list2.Add(item);
                }
            }
        }

        list.Sort();
        list.Reverse();
        if (list.Count > 10)
        {
            list.RemoveRange(10, list.Count - 10);
        }

        userData.RatingList.NextRatingList = list;
        list2.Sort();
        list2.Reverse();
        if (list2.Count > 10)
        {
            list2.RemoveRange(10, list2.Count - 10);
        }

        userData.RatingList.NextNewRatingList = list2;

        userData.Detail.LastPlayCredit = 0;
        userData.Detail.LastPlayMode = 0;
        if (GameManager.IsFreedomMode)
        {
            userData.Detail.LastPlayMode = 1;
        }

        if (GameManager.IsCourseMode)
        {
            userData.Detail.LastPlayMode = 2;
        }

        userData.Detail.LastGameId = ConstParameter.GameIDStr;
        userData.Detail.LastRomVersion = Singleton<SystemConfig>.Instance.config.romVersionInfo.versionNo.versionString;
        userData.Detail.LastDataVersion = Singleton<SystemConfig>.Instance.config.dataVersionInfo.versionNo.versionString;
    }

    private class SavingUi : MonoBehaviour
    {
        public void OnGUI()
        {
            var y = Screen.height * .075f;
            var width = GuiSizes.FontSize * 20f;
            var x = GuiSizes.PlayerCenter + GuiSizes.PlayerWidth / 2f - width;
            var rect = new Rect(x, y, width, GuiSizes.LabelHeight * 2.5f);

            var labelStyle = GUI.skin.GetStyle("label");
            labelStyle.fontSize = (int)(GuiSizes.FontSize * 1.2);
            labelStyle.alignment = TextAnchor.MiddleCenter;

            GUI.Box(rect, "");
            GUI.Label(rect, Locale.SavingDontExit);
        }
    }
}

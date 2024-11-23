using System;
using System.Collections.Generic;
using AquaMai.Config.Interfaces;
using AquaMai.Config.Types;

namespace AquaMai.Config.Migration;

public class ConfigMigration_V1_0_V2_0 : IConfigMigration
{
    public string FromVersion => "1.0";
    public string ToVersion => "2.0";

    public IConfigView Migrate(IConfigView src)
    {
        var dst = new ConfigView();

        dst.SetValue("Version", ToVersion);

        // UX.*
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "UX.Locale", "General.Locale", "");
        MapBooleanTrueToSectionEnable(src, dst, "UX.SinglePlayer", "GameSystem.SinglePlayer");
        MapBooleanTrueToSectionEnable(src, dst, "UX.HideMask", "Fancy.HideMask");
        MapBooleanTrueToSectionEnable(src, dst, "UX.LoadAssetsPng", "GameAssets.LoadLocalImages");
        MapBooleanTrueToSectionEnable(src, dst, "UX.LoadAssetBundleWithoutManifest", "GameAssets.LoadAssetBundleWithoutManifest");
        MapBooleanTrueToSectionEnable(src, dst, "UX.RandomBgm", "Fancy.RandomBgm");
        MapBooleanTrueToSectionEnable(src, dst, "UX.DemoMaster", "Fancy.DemoMaster");
        MapBooleanTrueToSectionEnable(src, dst, "UX.ExtendTimer", "GameSystem.DisableTimeout");
        MapBooleanTrueToSectionEnable(src, dst, "UX.ImmediateSave", "UX.ImmediateSave");
        MapBooleanTrueToSectionEnable(src, dst, "UX.LoadLocalBga", "GameAssets.UseJacketAsDummyMovie");
        if (src.GetValueOrDefault<bool>("UX.CustomFont"))
        {
            dst.SetValue("GameSystem.Fonts.Paths", "LocalAssets/font.ttf");
            dst.SetValue("GameSystem.Fonts.AddAsFallback", false);
        }
        MapBooleanTrueToSectionEnable(src, dst, "UX.TouchToButtonInput", "GameSystem.TouchToButtonInput");
        MapBooleanTrueToSectionEnable(src, dst, "UX.HideHanabi", "Fancy.GamePlay.HideHanabi");
        MapBooleanTrueToSectionEnable(src, dst, "UX.SlideFadeInTweak", "Fancy.GamePlay.SlideFadeInTweak");
        MapBooleanTrueToSectionEnable(src, dst, "UX.JudgeAccuracyInfo", "UX.JudgeAccuracyInfo");
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "UX.CustomVersionString", "Fancy.CustomVersionString.VersionString", "");
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "UX.CustomPlaceName", "Fancy.CustomPlaceName.PlaceName", "");
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "UX.ExecOnIdle", "Fancy.Triggers.ExecOnIdle", "");
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "UX.ExecOnEntry", "Fancy.Triggers.ExecOnEntry", "");

        // Cheat.*
        var unlockTickets = src.GetValueOrDefault<bool>("Cheat.TicketUnlock");
        var unlockMaps = src.GetValueOrDefault<bool>("Cheat.MapUnlock");
        var unlockUtage = src.GetValueOrDefault<bool>("Cheat.UnlockUtage");
        if (unlockTickets ||
            unlockMaps ||
            unlockUtage)
        {
            dst.SetValue("GameSystem.Unlock.Tickets", unlockTickets);
            dst.SetValue("GameSystem.Unlock.Maps", unlockMaps);
            dst.SetValue("GameSystem.Unlock.Utage", unlockUtage);
        }

        // Fix.*
        MapBooleanTrueToSectionEnable(src, dst, "Fix.SkipVersionCheck", "GameSystem.SkipUserVersionCheck");
        if (!src.GetValueOrDefault<bool>("Fix.RemoveEncryption"))
        {
            dst.SetValue("GameSystem.RemoveEncryption.Disabled", true); // Enabled by default in V2
        }
        MapBooleanTrueToSectionEnable(src, dst, "Fix.ForceAsServer", "GameSettings.ForceAsServer");
        if (src.GetValueOrDefault<bool>("Fix.ForceFreePlay"))
        {
            dst.SetValue("GameSettings.CreditConfig.IsFreePlay", true);
        }
        if (src.GetValueOrDefault<bool>("Fix.ForcePaidPlay"))
        {
            dst.SetValue("GameSettings.CreditConfig.IsFreePlay", false);
            dst.SetValue("GameSettings.CreditConfig.LockCredits", 24);
        }
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "Fix.ExtendNotesPool", "Fancy.GamePlay.ExtendNotesPool", 0);
        MapBooleanTrueToSectionEnable(src, dst, "Fix.FrameRateLock", "GameSystem.LockFrameRate");
        if (src.GetValueOrDefault<bool>("Font.FontFix") &&
            !src.GetValueOrDefault<bool>("UX.CustomFont"))
        {
            dst.SetValue("GameSystem.Fonts.Paths", "%SYSTEMROOT%/Fonts/msyhbd.ttc");
            dst.SetValue("GameSystem.Fonts.AddAsFallback", true);
        }
        MapBooleanTrueToSectionEnable(src, dst, "Fix.RealisticRandomJudge", "Fancy.GamePlay.RealisticRandomJudge");
        if (src.GetValueOrDefault<bool>("Fix.HanabiFix"))
        {
            dst.SetValue("GameSystem.SinglePlayer.FixHanabi", true);
        }
        if (!src.GetValueOrDefault<bool>("Fix.HanabiFix") &&
            src.GetValueOrDefault<bool>("UX.SinglePlayer"))
        {
            dst.SetValue("GameSystem.SinglePlayer.FixHanabi", false); // `true` by default in V2
        }
        MapBooleanTrueToSectionEnable(src, dst, "Fix.IgnoreAimeServerError", "Fix.Stability.IgnoreAimeServerError");
        MapBooleanTrueToSectionEnable(src, dst, "Fix.TouchResetAfterTrack", "Fix.Stability.ResetTouchAfterTrack");

        // Utils.*
        MapBooleanTrueToSectionEnable(src, dst, "Utils.LogUserId", "Utils.LogUserId");
        MapValueToEntryValueIfNonNullOrDefault<double>(src, dst, "Utils.JudgeAdjustA", "GameSettings.JudgeAdjust.A", 0);
        MapValueToEntryValueIfNonNullOrDefault<double>(src, dst, "Utils.JudgeAdjustB", "GameSettings.JudgeAdjust.B", 0);
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "Utils.TouchDelay", "GameSettings.JudgeAdjust.TouchDelay", 0);
        MapBooleanTrueToSectionEnable(src, dst, "Utils.SelectionDetail", "UX.SelectionDetail");
        MapBooleanTrueToSectionEnable(src, dst, "Utils.ShowNetErrorDetail", "Utils.ShowNetErrorDetail");
        MapBooleanTrueToSectionEnable(src, dst, "Utils.ShowErrorLog", "Utils.ShowErrorLog");
        MapBooleanTrueToSectionEnable(src, dst, "Utils.FrameRateDisplay", "Utils.DisplayFrameRate");
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "Utils.TouchPanelBaudRate", "GameSystem.TouchPanelBaudRate.BaudRate", 0);

        // TimeSaving.*
        MapBooleanTrueToSectionEnable(src, dst, "TimeSaving.SkipWarningScreen", "SkipProcesses.SkipWarningScreen");
        MapBooleanTrueToSectionEnable(src, dst, "TimeSaving.ImproveLoadSpeed", "GameSystem.RemoveStartupDelay");
        MapBooleanTrueToSectionEnable(src, dst, "TimeSaving.SkipToMusicSelection", "SkipProcesses.SkipToMusicSelection");
        MapBooleanTrueToSectionEnable(src, dst, "TimeSaving.SkipEventInfo", "SkipProcesses.SkipEventInfo");
        MapBooleanTrueToSectionEnable(src, dst, "TimeSaving.IWontTapOrSlideVigorously", "SkipProcesses.SkipDontTapOrSlideVigorously");
        MapBooleanTrueToSectionEnable(src, dst, "TimeSaving.SkipGameOverScreen", "SkipProcesses.SkipGameOverScreen");
        MapBooleanTrueToSectionEnable(src, dst, "TimeSaving.SkipTrackStart", "SkipProcesses.SkipTrackStart");
        MapBooleanTrueToSectionEnable(src, dst, "TimeSaving.ShowQuickEndPlay", "UX.ShowQuickEndPlay");

        // Visual.*
        if (src.GetValueOrDefault<bool>("Visual.CustomSkins"))
        {
            dst.SetValue("Fancy.CustomSkins.SkinsDir", "LocalAssets/Skins");
        }
        MapBooleanTrueToSectionEnable(src, dst, "Visual.JudgeDisplay4B", "Fancy.GamePlay.JudgeDisplay4B");
        MapBooleanTrueToSectionEnable(src, dst, "Visual.CustomTrackStartDiff", "Fancy.CustomTrackStartDiff");
        MapBooleanTrueToSectionEnable(src, dst, "Visual.TrackStartProcessTweak", "Fancy.GamePlay.TrackStartProcessTweak");
        MapBooleanTrueToSectionEnable(src, dst, "Visual.DisableTrackStartTabs", "Fancy.DisableTrackStartTabs");
        MapBooleanTrueToSectionEnable(src, dst, "Visual.FanJudgeFlip", "Fancy.GamePlay.FanJudgeFlip");
        MapBooleanTrueToSectionEnable(src, dst, "Visual.BreakSlideJudgeBlink", "Fancy.GamePlay.BreakSlideJudgeBlink");
        MapBooleanTrueToSectionEnable(src, dst, "Visual.SlideArrowAnimation", "Fancy.GamePlay.SlideArrowAnimation");
        MapBooleanTrueToSectionEnable(src, dst, "Visual.SlideLayerReverse", "Fancy.GamePlay.SlideLayerReverse");

        // ModKeyMap.*
        var keyQuickSkip = src.GetValueOrDefault("ModKeyMap.QuickSkip", "None");
        var keyInGameRetry = src.GetValueOrDefault("ModKeyMap.InGameRetry", "None");
        var keyInGameSkip = src.GetValueOrDefault("ModKeyMap.InGameSkip", "None");
        var keyPractiseMode = src.GetValueOrDefault("ModKeyMap.PractiseMode", "None");
        var keyHideSelfMadeCharts = src.GetValueOrDefault("ModKeyMap.HideSelfMadeCharts", "None");
        if (keyQuickSkip != "None")
        {
            dst.SetValue("UX.OneKeyEntryEnd.Key", keyQuickSkip);
            MapValueToEntryValueIfNonNull<bool>(src, dst, "ModKeyMap.QuickSkipLongPress", "UX.OneKeyEntryEnd.LongPress");
        }
        if (keyInGameRetry != "None" || keyInGameSkip != "None")
        {
            dst.SetValue("UX.OneKeyRetrySkip.RetryKey", keyInGameRetry);
            if (keyInGameRetry != "None")
            {
                MapValueToEntryValueIfNonNull<bool>(src, dst, "ModKeyMap.InGameRetryLongPress", "UX.OneKeyRetrySkip.RetryLongPress");
            }
            dst.SetValue("UX.OneKeyRetrySkip.SkipKey", keyInGameSkip);
            if (keyInGameSkip != "None")
            {
                MapValueToEntryValueIfNonNull<bool>(src, dst, "ModKeyMap.InGameSkipLongPress", "UX.OneKeyRetrySkip.SkipLongPress");
            }
        }
        if (keyPractiseMode != "None")
        {
            dst.SetValue("PracticeMode.Key", keyPractiseMode);
            MapValueToEntryValueIfNonNull<bool>(src, dst, "ModKeyMap.PractiseModeLongPress", "PracticeMode.LongPress");
        }
        if (keyHideSelfMadeCharts != "None")
        {
            dst.SetValue("UX.HideSelfMadeCharts.Key", keyHideSelfMadeCharts);
            MapValueToEntryValueIfNonNull<bool>(src, dst, "ModKeyMap.HideSelfMadeChartsLongPress", "UX.HideSelfMadeCharts.LongPress");
        }
        MapBooleanTrueToSectionEnable(src, dst, "ModKeyMap.EnableNativeQuickRetry", "GameSystem.QuickRetry");
        if (src.TryGetValue<string>("ModKeyMap.TestMode", out var testMode) &&
            testMode != "" &&
            testMode != "Test")
        {
            dst.SetValue("DeprecationWarning.v1_0_ModKeyMap_TestMode", true);
        }
        MapValueToEntryValueIfNonNullOrDefault(src, dst, "ModKeyMap.TestModeLongPress", "GameSystem.KeyMap.TestProof", false);

        // WindowState.*
        if (src.GetValueOrDefault<bool>("WindowState.Enable"))
        {
            MapValueOrDefaultToEntryValue(src, dst, "WindowState.Windowed", "GameSystem.Window.Windowed", false);
            MapValueOrDefaultToEntryValue(src, dst, "WindowState.Width", "GameSystem.Window.Width", 0);
            MapValueOrDefaultToEntryValue(src, dst, "WindowState.Height", "GameSystem.Window.Height", 0);
        }

        // CustomCameraId.*
        if (src.GetValueOrDefault<bool>("CustomCameraId.Enable"))
        {
            dst.EnsureDictionary("GameSystem.CustomCameraId");
            MapValueToEntryValueIfNonNullOrDefault(src, dst, "CustomCameraId.PrintCameraList", "GameSystem.CustomCameraId.PrintCameraList", false);
            MapValueToEntryValueIfNonNullOrDefault(src, dst, "CustomCameraId.LeftQrCamera", "GameSystem.CustomCameraId.LeftQrCamera", 0);
            MapValueToEntryValueIfNonNullOrDefault(src, dst, "CustomCameraId.RightQrCamera", "GameSystem.CustomCameraId.RightQrCamera", 0);
            MapValueToEntryValueIfNonNullOrDefault(src, dst, "CustomCameraId.PhotoCamera", "GameSystem.CustomCameraId.PhotoCamera", 0);
            MapValueToEntryValueIfNonNullOrDefault(src, dst, "CustomCameraId.ChimeCamera", "GameSystem.CustomCameraId.ChimeCamera", 0);
        }

        // TouchSensitivity.*
        if (src.GetValueOrDefault<bool>("TouchSensitivity.Enable"))
        {
            dst.EnsureDictionary("GameSettings.TouchSensitivity");
            var areas = new[]
            {
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8",
                "C1", "C2",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8",
                "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8",
            };
            foreach (var area in areas)
            {
                MapValueToEntryValueIfNonNull<int>(src, dst, $"TouchSensitivity.{area}", $"GameSettings.TouchSensitivity.{area}");
            }
        }

        // CustomKeyMap.*
        if (src.GetValueOrDefault<bool>("CustomKeyMap.Enable"))
        {
            dst.EnsureDictionary("GameSystem.KeyMap");
            var keys = new[]
            {
                "Test", "Service",
                "Button1_1P", "Button3_1P", "Button4_1P", "Button2_1P", "Button5_1P", "Button6_1P", "Button7_1P", "Button8_1P",
                "Select_1P",
                "Button1_2P", "Button2_2P", "Button3_2P", "Button4_2P", "Button5_2P", "Button6_2P", "Button7_2P", "Button8_2P",
                "Select_2P"
            };
            foreach (var key in keys)
            {
                if (src.TryGetValue<string>($"CustomKeyMap.{key}", out var value) &&
                    Enum.TryParse<KeyCodeID>(value, out var keyCode))
                {
                    dst.SetValue($"GameSystem.KeyMap.{key}", keyCode.ToString());
                }
            }
        }

        return dst;
    }

    // An value in the old config maps to an entry value in the new config.
    // Any existing value, including zero, is valid.
    private void MapValueToEntryValueIfNonNull<T>(IConfigView src, ConfigView dst, string srcKey, string dstKey)
    {
        if (src.TryGetValue<T>(srcKey, out var value))
        {
            dst.SetValue(dstKey, value);
        }
    }

    // An value in the old config maps to an entry value in the new config.
    // Null or default value is ignored.
    private void MapValueToEntryValueIfNonNullOrDefault<T>(IConfigView src, ConfigView dst, string srcKey, string dstKey, T defaultValue)
    {
        if (src.TryGetValue<T>(srcKey, out var value) && !EqualityComparer<T>.Default.Equals(value, defaultValue))
        {
            dst.SetValue(dstKey, value);
        }
    }

    // An value in the old config maps to an entry value in the new config.
    // Null value is replaced with a default value.
    private void MapValueOrDefaultToEntryValue<T>(IConfigView src, ConfigView dst, string srcKey, string dstKey, T defaultValue)
    {
        if (src.TryGetValue<T>(srcKey, out var value))
        {
            dst.SetValue(dstKey, value);
        }
        else
        {
            dst.SetValue(dstKey, defaultValue);
        }
    }

    // An boolean value in the old config maps to a default-off section's enable in the new config.
    private void MapBooleanTrueToSectionEnable(IConfigView src, ConfigView dst, string srcKey, string dstKey)
    {
        if (src.GetValueOrDefault<bool>(srcKey))
        {
            dst.EnsureDictionary(dstKey);
        }
    }
}

using System;
using System.Collections.Generic;
using System.Diagnostics;
using AquaMai.Config.Types;
using HarmonyLib;
using Main;
using Manager;
using MelonLoader;
using UnityEngine;

namespace AquaMai.Core.Helpers;

public static class KeyListener
{
    private static readonly Dictionary<KeyCodeOrName, int> _keyPressFrames = [];
    private static readonly Dictionary<KeyCodeOrName, int> _keyPressFramesPrev = [];

    static KeyListener()
    {
        foreach (KeyCodeOrName key in Enum.GetValues(typeof(KeyCodeOrName)))
        {
            _keyPressFrames[key] = 0;
            _keyPressFramesPrev[key] = 0;
        }
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameMainObject), "Update")]
    public static void CheckLongPush()
    {
        foreach (KeyCodeOrName key in Enum.GetValues(typeof(KeyCodeOrName)))
        {
            _keyPressFramesPrev[key] = _keyPressFrames[key];
            if (GetKeyPush(key))
            {
# if DEBUG
                MelonLogger.Msg($"CheckLongPush {key} is push {_keyPressFrames[key]}");
# endif
                _keyPressFrames[key]++;
            }
            else
            {
                _keyPressFrames[key] = 0;
            }
        }
    }

    public static bool GetKeyPush(KeyCodeOrName key) =>
        key switch
        {
            KeyCodeOrName.None => false,
            < KeyCodeOrName.Select1P => Input.GetKey(key.GetKeyCode()),
            KeyCodeOrName.Test => InputManager.GetSystemInputPush(InputManager.SystemButtonSetting.ButtonTest),
            KeyCodeOrName.Service => InputManager.GetSystemInputPush(InputManager.SystemButtonSetting.ButtonService),
            KeyCodeOrName.Select1P => InputManager.GetButtonPush(0, InputManager.ButtonSetting.Select),
            KeyCodeOrName.Select2P => InputManager.GetButtonPush(1, InputManager.ButtonSetting.Select),
            _ => throw new ArgumentOutOfRangeException(nameof(key), key, "我也不知道这是什么键")
        };

    public static bool GetKeyDown(KeyCodeOrName key)
    {
        // return key switch
        // {
        //     KeyCodeOrName.None => false,
        //     < KeyCodeOrName.Select1P => Input.GetKeyDown(key.GetKeyCode()),
        //     KeyCodeOrName.Test => InputManager.GetSystemInputDown(InputManager.SystemButtonSetting.ButtonTest),
        //     KeyCodeOrName.Service => InputManager.GetSystemInputDown(InputManager.SystemButtonSetting.ButtonService),
        //     KeyCodeOrName.Select1P => InputManager.GetButtonDown(0, InputManager.ButtonSetting.Select),
        //     KeyCodeOrName.Select2P => InputManager.GetButtonDown(1, InputManager.ButtonSetting.Select),
        //     _ => throw new ArgumentOutOfRangeException(nameof(key), key, "我也不知道这是什么键")
        // };

        // 不用这个，我们检测按键是否弹起以及弹起之前按下的时间是否小于 30，这样可以防止要长按时按下的时候就触发
        return _keyPressFrames[key] == 0 && 0 < _keyPressFramesPrev[key] && _keyPressFramesPrev[key] < 30;
    }

    public static bool GetKeyDownOrLongPress(KeyCodeOrName key, bool isLongPress)
    {
        bool ret;
        if (isLongPress)
        {
            ret = _keyPressFrames[key] == 60;
        }
        else
        {
            ret = GetKeyDown(key);
        }

# if DEBUG
        if (ret)
        {
            MelonLogger.Msg($"Key {key} is pressed, long press: {isLongPress}");
            MelonLogger.Msg(new StackTrace());
        }
# endif
        return ret;
    }

    private static KeyCode GetKeyCode(this KeyCodeOrName keyCodeOrName) =>
        keyCodeOrName switch
        {
            KeyCodeOrName.Alpha0 => KeyCode.Alpha0,
            KeyCodeOrName.Alpha1 => KeyCode.Alpha1,
            KeyCodeOrName.Alpha2 => KeyCode.Alpha2,
            KeyCodeOrName.Alpha3 => KeyCode.Alpha3,
            KeyCodeOrName.Alpha4 => KeyCode.Alpha4,
            KeyCodeOrName.Alpha5 => KeyCode.Alpha5,
            KeyCodeOrName.Alpha6 => KeyCode.Alpha6,
            KeyCodeOrName.Alpha7 => KeyCode.Alpha7,
            KeyCodeOrName.Alpha8 => KeyCode.Alpha8,
            KeyCodeOrName.Alpha9 => KeyCode.Alpha9,
            KeyCodeOrName.Keypad0 => KeyCode.Keypad0,
            KeyCodeOrName.Keypad1 => KeyCode.Keypad1,
            KeyCodeOrName.Keypad2 => KeyCode.Keypad2,
            KeyCodeOrName.Keypad3 => KeyCode.Keypad3,
            KeyCodeOrName.Keypad4 => KeyCode.Keypad4,
            KeyCodeOrName.Keypad5 => KeyCode.Keypad5,
            KeyCodeOrName.Keypad6 => KeyCode.Keypad6,
            KeyCodeOrName.Keypad7 => KeyCode.Keypad7,
            KeyCodeOrName.Keypad8 => KeyCode.Keypad8,
            KeyCodeOrName.Keypad9 => KeyCode.Keypad9,
            KeyCodeOrName.F1 => KeyCode.F1,
            KeyCodeOrName.F2 => KeyCode.F2,
            KeyCodeOrName.F3 => KeyCode.F3,
            KeyCodeOrName.F4 => KeyCode.F4,
            KeyCodeOrName.F5 => KeyCode.F5,
            KeyCodeOrName.F6 => KeyCode.F6,
            KeyCodeOrName.F7 => KeyCode.F7,
            KeyCodeOrName.F8 => KeyCode.F8,
            KeyCodeOrName.F9 => KeyCode.F9,
            KeyCodeOrName.F10 => KeyCode.F10,
            KeyCodeOrName.F11 => KeyCode.F11,
            KeyCodeOrName.F12 => KeyCode.F12,
            KeyCodeOrName.Insert => KeyCode.Insert,
            KeyCodeOrName.Delete => KeyCode.Delete,
            KeyCodeOrName.Home => KeyCode.Home,
            KeyCodeOrName.End => KeyCode.End,
            KeyCodeOrName.PageUp => KeyCode.PageUp,
            KeyCodeOrName.PageDown => KeyCode.PageDown,
            KeyCodeOrName.UpArrow => KeyCode.UpArrow,
            KeyCodeOrName.DownArrow => KeyCode.DownArrow,
            KeyCodeOrName.LeftArrow => KeyCode.LeftArrow,
            KeyCodeOrName.RightArrow => KeyCode.RightArrow,
            _ => throw new ArgumentOutOfRangeException(nameof(keyCodeOrName), keyCodeOrName, "游戏功能键需要单独处理")
        };
}

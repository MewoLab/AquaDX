using System;
using System.Collections.Generic;
using System.Diagnostics;
using HarmonyLib;
using Main;
using Manager;
using MelonLoader;
using UnityEngine;

namespace AquaMai.Mods.ModKeyMap;

public static class ModKeyListener
{
    private static readonly Dictionary<ModKeyCode, int> _keyPressFrames = new();
    private static readonly Dictionary<ModKeyCode, int> _keyPressFramesPrev = new();

    static ModKeyListener()
    {
        foreach (ModKeyCode key in Enum.GetValues(typeof(ModKeyCode)))
        {
            _keyPressFrames[key] = 0;
            _keyPressFramesPrev[key] = 0;
        }
    }

    [HarmonyPostfix]
    [HarmonyPatch(typeof(GameMainObject), "Update")]
    public static void CheckLongPush()
    {
        foreach (ModKeyCode key in Enum.GetValues(typeof(ModKeyCode)))
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

    public static bool GetKeyPush(ModKeyCode key) =>
        key switch
        {
            ModKeyCode.None => false,
            < ModKeyCode.Select1P => Input.GetKey(key.GetKeyCode()),
            ModKeyCode.Test => InputManager.GetSystemInputPush(InputManager.SystemButtonSetting.ButtonTest),
            ModKeyCode.Service => InputManager.GetSystemInputPush(InputManager.SystemButtonSetting.ButtonService),
            ModKeyCode.Select1P => InputManager.GetButtonPush(0, InputManager.ButtonSetting.Select),
            ModKeyCode.Select2P => InputManager.GetButtonPush(1, InputManager.ButtonSetting.Select),
            _ => throw new ArgumentOutOfRangeException(nameof(key), key, "我也不知道这是什么键")
        };

    public static bool GetKeyDown(ModKeyCode key)
    {
        // return key switch
        // {
        //     ModKeyCode.None => false,
        //     < ModKeyCode.Select1P => Input.GetKeyDown(key.GetKeyCode()),
        //     ModKeyCode.Test => InputManager.GetSystemInputDown(InputManager.SystemButtonSetting.ButtonTest),
        //     ModKeyCode.Service => InputManager.GetSystemInputDown(InputManager.SystemButtonSetting.ButtonService),
        //     ModKeyCode.Select1P => InputManager.GetButtonDown(0, InputManager.ButtonSetting.Select),
        //     ModKeyCode.Select2P => InputManager.GetButtonDown(1, InputManager.ButtonSetting.Select),
        //     _ => throw new ArgumentOutOfRangeException(nameof(key), key, "我也不知道这是什么键")
        // };

        // 不用这个，我们检测按键是否弹起以及弹起之前按下的时间是否小于 30，这样可以防止要长按时按下的时候就触发
        return _keyPressFrames[key] == 0 && 0 < _keyPressFramesPrev[key] && _keyPressFramesPrev[key] < 30;
    }

    public static bool GetKeyDownOrLongPress(ModKeyCode key, bool isLongPress)
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
            MelonLogger.Msg($"Key {key} is pressed");
            MelonLogger.Msg(new StackTrace());
        }
# endif
        return ret;
    }

    private static KeyCode GetKeyCode(this ModKeyCode modKeyCode) =>
        modKeyCode switch
        {
            ModKeyCode.Alpha0 => KeyCode.Alpha0,
            ModKeyCode.Alpha1 => KeyCode.Alpha1,
            ModKeyCode.Alpha2 => KeyCode.Alpha2,
            ModKeyCode.Alpha3 => KeyCode.Alpha3,
            ModKeyCode.Alpha4 => KeyCode.Alpha4,
            ModKeyCode.Alpha5 => KeyCode.Alpha5,
            ModKeyCode.Alpha6 => KeyCode.Alpha6,
            ModKeyCode.Alpha7 => KeyCode.Alpha7,
            ModKeyCode.Alpha8 => KeyCode.Alpha8,
            ModKeyCode.Alpha9 => KeyCode.Alpha9,
            ModKeyCode.Keypad0 => KeyCode.Keypad0,
            ModKeyCode.Keypad1 => KeyCode.Keypad1,
            ModKeyCode.Keypad2 => KeyCode.Keypad2,
            ModKeyCode.Keypad3 => KeyCode.Keypad3,
            ModKeyCode.Keypad4 => KeyCode.Keypad4,
            ModKeyCode.Keypad5 => KeyCode.Keypad5,
            ModKeyCode.Keypad6 => KeyCode.Keypad6,
            ModKeyCode.Keypad7 => KeyCode.Keypad7,
            ModKeyCode.Keypad8 => KeyCode.Keypad8,
            ModKeyCode.Keypad9 => KeyCode.Keypad9,
            ModKeyCode.F1 => KeyCode.F1,
            ModKeyCode.F2 => KeyCode.F2,
            ModKeyCode.F3 => KeyCode.F3,
            ModKeyCode.F4 => KeyCode.F4,
            ModKeyCode.F5 => KeyCode.F5,
            ModKeyCode.F6 => KeyCode.F6,
            ModKeyCode.F7 => KeyCode.F7,
            ModKeyCode.F8 => KeyCode.F8,
            ModKeyCode.F9 => KeyCode.F9,
            ModKeyCode.F10 => KeyCode.F10,
            ModKeyCode.F11 => KeyCode.F11,
            ModKeyCode.F12 => KeyCode.F12,
            ModKeyCode.Insert => KeyCode.Insert,
            ModKeyCode.Delete => KeyCode.Delete,
            ModKeyCode.Home => KeyCode.Home,
            ModKeyCode.End => KeyCode.End,
            ModKeyCode.PageUp => KeyCode.PageUp,
            ModKeyCode.PageDown => KeyCode.PageDown,
            ModKeyCode.UpArrow => KeyCode.UpArrow,
            ModKeyCode.DownArrow => KeyCode.DownArrow,
            ModKeyCode.LeftArrow => KeyCode.LeftArrow,
            ModKeyCode.RightArrow => KeyCode.RightArrow,
            _ => throw new ArgumentOutOfRangeException(nameof(modKeyCode), modKeyCode, "游戏功能键需要单独处理")
        };
}

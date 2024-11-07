using System;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using UnityEngine;

namespace AquaMai.Mods.WindowState;

public class Enable
{
    private const int GWL_STYLE = -16;
    private const int WS_WHATEVER = 0x14CF0000;

    private static IntPtr hwnd = IntPtr.Zero;

    public static void DoCustomPatch(HarmonyLib.Harmony h)
    {
        if (AquaMai.AppConfig.WindowState.Windowed)
        {
            var alreadyWindowed = Screen.fullScreenMode == FullScreenMode.Windowed;
            if (AquaMai.AppConfig.WindowState.Width == 0 || AquaMai.AppConfig.WindowState.Height == 0)
            {
                Screen.fullScreenMode = FullScreenMode.Windowed;
            }
            else
            {
                alreadyWindowed = false;
                Screen.SetResolution(AquaMai.AppConfig.WindowState.Width, AquaMai.AppConfig.WindowState.Height, FullScreenMode.Windowed);
            }

            hwnd = GetWindowHandle();
            if (alreadyWindowed)
            {
                SetResizeable();
            }
            else
            {
                Task.Run(async () =>
                {
                    await Task.Delay(3000);
                    // Screen.SetResolution has delay
                    SetResizeable();
                });
            }
        }
        else
        {
            var width = AquaMai.AppConfig.WindowState.Width == 0 ? Display.main.systemWidth : AquaMai.AppConfig.WindowState.Width;
            var height = AquaMai.AppConfig.WindowState.Height == 0 ? Display.main.systemHeight : AquaMai.AppConfig.WindowState.Height;
            Screen.SetResolution(width, height, FullScreenMode.FullScreenWindow);
        }
    }

    public static void SetResizeable()
    {
        if (hwnd == IntPtr.Zero) return;
        SetWindowLongPtr(hwnd, GWL_STYLE, WS_WHATEVER);
    }

    private delegate bool EnumThreadDelegate(IntPtr hwnd, IntPtr lParam);

    [DllImport("user32.dll")]
    static extern bool EnumThreadWindows(int dwThreadId, EnumThreadDelegate lpfn, IntPtr lParam);

    [DllImport("Kernel32.dll")]
    static extern int GetCurrentThreadId();

    static IntPtr GetWindowHandle()
    {
        IntPtr returnHwnd = IntPtr.Zero;
        var threadId = GetCurrentThreadId();
        EnumThreadWindows(threadId,
            (hWnd, lParam) =>
            {
                if (returnHwnd == IntPtr.Zero) returnHwnd = hWnd;
                return true;
            }, IntPtr.Zero);
        return returnHwnd;
    }

    [DllImport("user32.dll")]
    static extern IntPtr SetWindowLongPtr(IntPtr hWnd, int nIndex, int dwNewLong);
}

using AquaMai.Config.Attributes;
using HarmonyLib;
using Process;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay;

[ConfigSection(
    en: """
        Delayed the animation of the song start screen
        For recording chart confirmation
        """,
    zh: """
        推迟了歌曲开始界面的动画
        录制谱面确认用
        """)]
public class TrackStartProcessTweak
{
    // 总之这个 Patch 没啥用, 是我个人用 sinmai 录谱面确认时用得到, 顺手也写进来了
    // 具体而言就是推迟了歌曲开始界面的动画便于后期剪辑

    [HarmonyPrefix]
    [HarmonyPatch(typeof(TrackStartProcess), "OnUpdate")]
    private static bool DelayAnimation(
        TrackStartProcess.TrackStartSequence ____state,
        ref float ____timeCounter,
        ProcessDataContainer ___container
    )
    {
        if (____state == TrackStartProcess.TrackStartSequence.Wait)
        {
            // 将开始动画（就是“噔噔, 噔 噔噔”）推迟
            float temp = ____timeCounter + Time.deltaTime;
            if (____timeCounter < 1.0f && temp >= 1.0f)
            {
                // 这是用来让转场动画继续播放的, 原本就是这个时候 notify 的同时开始播放开始动画
                // 现在把开始动画往后延
                ___container.processManager.NotificationFadeIn();
            }
            ____timeCounter = temp;
            if (____timeCounter >= 3.0f)
            {
                return true;
                // 原 method 的逻辑是这样
                // case TrackStartProcess.TrackStartSequence.Wait:
                //   this._timeCounter += Time.deltaTime;
                //   if ((double) this._timeCounter >= 1.0)
                //   {
                //     this._timeCounter = 0.0f;
                //     this._state = TrackStartProcess.TrackStartSequence.Disp;
                //     /* 一些开始播放开始动画的代码 */
                //     this.container.processManager.NotificationFadeIn();
                //     break;
                //   }
                //   break;
                // 所以只要在 prefix 里面等到 timeCounter 达到我们想要的值以后再执行原 method 就好
                // 这里有个细节: NotificationFadeIn() 会被执行两遍, 这其实不好, 是个潜在 bug
                // 不过由于此处把开始动画往后推了 2s, 转场动画已经结束把 Process 释放掉了, 所以第二遍会找不到 Process 就没效果
            }
            return false;
        }
        else if (____state == TrackStartProcess.TrackStartSequence.DispEnd)
        {
            // 将开始动画结束以后的转场动画推迟
            ____timeCounter += Time.deltaTime;  // timeCounter 会在先前由原本的 method 归零
            if (____timeCounter >= 1.0f)
            {
                return true;
            }
            return false;
        }
        return true;
    }


}


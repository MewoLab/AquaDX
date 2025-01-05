# Chunithm National Matching Guide

The national matching game mode allows up to 4 players on any server (YES, ANY SERVER) to play together.
In this game mode, for example, you can play with RinNET or Missless players as well.
This is a guide on how to set up your client for national matching.

This is tested on Chusan 2.27.

## Pre-requisites

- Play the normal game at least once so that you have a profile on the server.
- NAT Type must not be Symmetric ([Check here](https://www.checkmynat.com/))
- Your firewall must be turned off (or [add a rule that allows chusanApp](#firewall-rules))

## Setting Up

![](img/chu3-matching.png)

1. Go to the AquaNet website and set your matching server to "Yukiotoko"  
   (To go to the settings page, click on the gear icon in the top right corner of your profile, switch to chuni tab, scroll down, click "Select Matching Server")
2. Make sure you use [Dniel97's open-source segatools](https://gitea.tendokyu.moe/Dniel97/SEGAguide/wiki/SDHD)  
   If you're using fufubot segatools, please override it with Dniel97's version (don't forget to update `segatools.ini`).
3. Patch your `chusanApp.exe` using [Two-Torial's open-source patcher](https://patcher.two-torial.xyz/)  
   (Make sure you disable "Set all timer to 999", enable "No encryption", "No TLS", "Patch for head-to-head play")
4. Make sure you only have official options, but add the option [ARRR](https://pixeldrain.com/u/D2jjN3of).  
   (Remove things like A999, AOMN, etc.)
5. Pet your cat 🐈
6. Launch!


### Firewall Rules

Below is a simple command to add firewall rules for Chunithm.

```shell
@echo off
set /p gamedirectory = Make sure this is run as admin and enter game path (e.g. C:\SegaGames\Chunithm\bin\chusanApp.exe)\n
netsh advfirewall firewall add rule name="Chunithm National Matching Inbound" dir=in action=allow profile=any program="%gamedirectory%" enable=yes
netsh advfirewall firewall add rule name="Chunithm National Matching Outbound" dir=out action=allow profile=any program="%gamedirectory%" enable=yes
```

## Troubleshooting

**Q: Game crashes when entering match mode**

Make sure you are using Dniel97's segatools.

**Q: After matching, timer shows 999 seconds and nobody can start**

Make sure you have patched your `chusanApp.exe` correctly.

**Q: Online battle icon gray / "Unable to select after the event time"**

Make sure your time zone is set to JST (UTC+9).

**Q: This window show up when joining.**

![](img/chu3-matching-error.png)

If there is only one player, then yea it's because there are not enough players.
Otherwise, it's because one of the players has a bad network environment (e.g. Symmetric NAT).
Try again with someone who played this mode before, if it still doesn't work, then it's probably you.

## How to Play

When you enter the matching mode, it will assign to you a matching room if other people are online, or create a new room otherwise. 
Then, after four people are present or after a specific amount of time has passed, the game will start.
Everyone will be asked to pick a song at the start, even though your song might not be the first one to be played.
After songs are picked, other players will play the song on the SAME DIFFICULTY as what you picked.
(So be a nice person and don't pick 15 if there are new players alright? 🥺)

If there are less than 4 players when the timer runs out, the game will fill in the empty slots with bots.
The bots will randomly select a song (mostly under Lv10).

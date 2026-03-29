<!--
NOTE: We discovered that there have been a trend of people abusing AI to sell open-sourced
software on various Chinese platforms such as CSDN or JueJin.

This is a free and open-source server. If you paid for it, you have been scammed.
The official source code is available at https://github.com/MewoLab/AquaDX.

Additionally, we would like to remind you that all commercial use of this software including
selling it on any platform is strictly prohibited as per the CC By-NC-SA license.


注意：我们发现有一些人滥用 AI 生成文案在中国的一些平台上（如 CSDN 或 掘金）销售开源软件。

这是一个免费且开源的服务器。如果您付费购买了这个软件，说明您被骗了。
官方源代码可以在以下地址获取：https://github.com/MewoLab/AquaDX。

另外，我们想提醒您，根据 CC By-NC-SA 许可证，此软件禁止一切商业用途，
包括在任何平台上出卖此软件。
--->

# AquaDX

Multipurpose game server for ALL.Net games.

## Related Projects

* [AquaMai](https://github.com/MuNET-OSS/AquaMai): A maimai DX mod that adds many features to the game.
* [AquaNet](./AquaNet): The primary AquaDX web frontend, hosted publically at [aquadx.net](https://aquadx.net/)

## Supported Games

> [!WARNING]  
> CHUNITHM pre-NEW!! and maimai pre-DX are no longer supported after February 6th, 2026 and all associated data will be removed.

| Game                   | Latest Ver.         | Initial Ver.        | Notes                                                       | Web UI | Data Import |
|------------------------|---------------------|---------------------|-------------------------------------------------------------|--------|-------------|
| SDHD: CHUNITHM         | 2.45 (X-VERSE-X)    | 2.00 (NEW)          |                                                             | ✅    |  ❗         | 
| SDEZ: maimai DX        | 1.65 (CiRCLE PLUS)  | 1.00 (DX)           | Missing circle (teams) support                              | ✅    |  ✅         | 
| SDGA: maimai DX (Intl) | 1.60 (CiRCLE)       | 1.00 (DX)           | Thanks [@Clansty](https://github.com/clansty)               | ✅    |  ✅         | 
| SDED: Card Maker       | 1.39                | N/A                 | Thanks [@Becods](https://github.com/Becods)                 | ❌    |  ❌         | 
| SDDT: O.N.G.E.K.I.     | 1.50 (Re:Fresh)     | N/A                 | Thanks [@PenguinCaptain](https://github.com/PenguinCaptain) | ✅    |  ❌         | 
| SBZV: Project DIVA     | 7.10                | N/A                 | Minimal support                                             | ❌    |  ❌         | 
| SDFE: Wacca            | 3.07 (Reverse)      | N/A                 | Later versions are EOS patches, network will not work       | ✅    |  ❌         | 

<!-- A majority of them have been left as N/A for initial version as they do not appear to have restrictions -->

Check out these docs for more information.
* [Game specific notes](docs/game_specific_notes.md)
* [Frequently asked questions](docs/frequently_asked_questions.md)
  
## Usage

### Public Instance

1. Ensure your game can boot to title screen.
2. Go to [https://aquadx.net](https://aquadx.net) and sign up (or log in).
3. Access the [Setup Connection page](https://aquadx.net/setup) and follow the instructions provided.

If you encounter any issue, please report via Discord, QQ, (both available on the website) or the GitHub issue tracker.

> [!TIP]  
> Your card's access code can be identified in all supported games on their title screen.<br>
> Press the "access code" and scan your card to retrieve it.

### Self Hosting (Advanced)

Please read the [self-hosting guide](docs/self-hosting.md) if you want to host your own server. 
This is only for advanced users and developers. 
Do not ask for support if you are not familiar with programming or networking.

## License

AquaDX uses the [CC By-NC-SA](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en) license:

* **Attribution** — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
* **NonCommercial** — You may not use the material for commercial purposes.
* **ShareAlike** — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.

## Attributions
* **samnyan**: The creator and developer of the original Aqua server
* **Akasaka Ryuunosuke**: providing all the DIVA protocol information
* **Dom Eori**: Developer of forked Aqua server, from v0.0.17 and up
* All devs who contribute to the [MiniMe server](https://dev.s-ul.net/djhackers/minime)
* All contributors by merge requests, issues and other channels

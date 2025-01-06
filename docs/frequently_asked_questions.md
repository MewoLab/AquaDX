# Frequently asked questions

## Game

### Will you share game or update files?
No.

### Where I can find game patches or get one?
Use a search engine and scroll through some forums, you will eventually find them.

### Can I use unmodified cabinets or games with this server?
No. Most games require patches to properly run. You can find which patches are required in the [game specific notes](game_specific_notes.md).

### Will you add [game name] support?
If a game is not supported, chances are that no current developers play the game. It will be extremely difficult to add support for a game you don't play. So, if you want to see support for a game that's currently not supported, you would need to find someone with Kotlin/Java programming skills who also plays the game.

### Will this server work with newer version of supported games?
Not likely but it doesn't hurt to try. If it works, please report it in the [issue tracker](https://github.com/MewoLab/AquaDX/issues).


## Self Hosting

### Can I host a public instance?
Yes. But you should only consider this if you have strong programming or homelab experience or have self-hosted other services before, as you will not receive support for basic questions.

If you're new to self-hosting, please just use our public server at https://aquadx.net.

> [!CAUTION]  
> By the CC By-NC-SA License, your public instance CANNOT be commercial in any way, this includes paid access, donations, or any other form of monetization.

### Can I use other port for endpoints?
No. It's hardcoded inside a game and server can do nothing about it.

### What ports does AquaDX use?
* 80: ALL.Net, Game endpoints
* 8443: Billing
* 22345: Aime

### How can I host this behind a reverse proxy?
Here are some tips:

* ALL.Net and game endpoints can be proxied
* Billing endpoint can be proxied but with extra steps: enable deprecated `TLS_RSA_*` cipher and use self-signed `ib.naominet.jp` TLS certificate - or simply disable it
* **DO NOT** proxy Aime endpoint: it's TCP traffic, not HTTP
* **DO NOT** compress traffic: proxy as-is if you can
* **DO NOT** use CDN proxy: e.g. Cloudflare
* Set `allnet.server.host` in `application.properties` with your public IP or hostname
* You may change endpoint ports for internally (aqua <-> proxy), but external ports that are exposed needs to be the same as default (proxy <-> game)


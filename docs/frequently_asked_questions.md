# Frequently asked questions
For best viewing experience, please use a markdown viewer that supports Github or Gitlab Flavored Markdown syntax.

## Server
### Can I host a public instance?
You're permitted to. However, you should only consider this if you have strong programming or homelab experience or have self-hosted other services before, as we will not be answering basic questions such as how to set up things like port forwarding or domain records.

If you're new to self-hosting, it's easier to just use our public server at https://aquadx.net.

### Can I use other port for endpoints?
No. It's hardcoded inside a game and server can do nothing about it.

### Can I disable billing endpoint?
Yes. There will be no major consequences even without it.

### What ports does Aqua use?
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

### `java.lang.ClassNotFoundException` occurs when I try to start a server!
Delete exclamation mark character(`!`) in your directory name.

### I want to add custom content data in Aqua database
You can add database entry by hand or your handmade tools. Currently Aqua doesn't have a way to do this automatically. I don't have timeframe for this either.

### How can I update to a newer version?
Read the [changelog](/CHANGELOG.md) to check breaking changes before updating. Then follow **one** of these options:
* Take jar file (`aqua.jar`) from newer release and replace it
* Copy your current DB file (`data/db.sqlite`) and config file (`application.properties`) to newer release folder

### `Port 80 was already in use` occurs when I try to start a server!
Identity which process is using 80 port then terminate it. Game won't connect to Aqua server if port is different then 80 port, so it is necessary.

## Game
### Can I use unmodified cabinets or games with this server?
No. This is due to hardened security measures which SEGA made.

### Will you add [your wanted game name] support?
It'll be case by case basis. Open an issue if you want to suggest something.

### Is the server update is mandatory with every new game content updates?
No, games will still work. However, new content *probably* not appear in game without so-called "force unlock" and Web UI will not work as intended when displaying new content.

### Will this server work with newer version of supported games?
Probably not without update, but who knows?

### Game passes connection test but networking is not working
Some game have kill switch for prevent early run before release date (a.k.a "Flying Get"). In this case, **BOTH** client and server need to handle networking enable flag for avoid this problem. For client part, consult with your source. For server, wait for future Aqua update with new version support.

### Team or/and place name showed as garbled characters in game when using non-latin text
Convert `application.properties` text encoding to UTF-8 without BOM.

## Misc
### Can I use latest version of Java instead of 17?
Yes.

### Can I use OpenJ9 JVM?
While it *may* work, I can't give any support with it.

### Will you share game or update files?
No.

### Where I can find game patches or get one?
I won't give any help on this repository.

### Why the file size of compiled jar is so huge?
It is because Aqua is using Spring Boot as a base. It's a upstream issue, not something that can be fixed on this project side.

### I have a problem with the *online* aqua server
I, the fork maintainer, am not affiliated with any public hosted instance. Contact to your server maintainer instead.

### Can I request developer access to this repository?
Please don't. I'm not hiding anything in the repository and currently no plan to give direct write access to anyone. However, merge request is always welcome.
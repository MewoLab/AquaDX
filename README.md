# AquaDX

Multipurpose game server for ALL.Net games.

### Related Projects

* [AquaMai](https://github.com/MewoLab/AquaMai): A maimai DX mod that adds many features to the game.
* [AquaNet](./AquaNet): A new web frontend for the modern age.

### Supported Games

Below is a list of games supported by this server. 

| Game                   | Ver  | Codename            | Thanks to                                  |
|------------------------|------|---------------------|--------------------------------------------|
| SDHD: CHUNITHM         | 2.27 | LUMINOUS PLUS       | [@rinsama](https://github.com/mxihan)      |
| SDEZ: MaiMai DX        | 1.45 | BUDDiES PLUS        | [@肥宅虾哥](https://github.com/FeiZhaixiage)   |
| SDGA: MaiMai DX (Intl) | 1.45 | BUDDiES PLUS        | [@Clansty](https://github.com/clansty)     |
| SDED: Card Maker       | 1.39 |                     | [@Becods](https://github.com/Becods)       |
| SDDT: O.N.G.E.K.I.     | 1.45 | bright MEMORY Act.3 | [@Gamer2097](https://github.com/Gamer2097) |
| SBZV: Project DIVA     | 7.10 | Future Tone         |                                            |
| SDFE: Wacca (*ALPHA)   | 3.07 | Reverse             |                                            |

> **News**: AquaDX just added Wacca support on Mar 29, 2024! Feel free to test it out, but expect bugs and issues.

Check out these docs for more information.
* [Game specific notes](docs/game_specific_notes.md)
* [Frequently asked questions](docs/frequently_asked_questions.md)

> [!TIP]  
> Some games may require additional patches and these will not be provided in this project and repository. You already found this, so you know where to find related resources too.

## Usage
If you own a cab or controller and just want to play the game, follow the instructions below:

1. Make sure you have a working game (read [game specific notes](docs/game_specific_notes.md) for more information).
2. Go to [aquadx.net](https://aquadx.net) and make an account.
3. Click on "Setup Connection" in the home page, and follow the instructions.
4. Play a coin with your card.  
   (Either a physical card or the `aime.txt` / `felica.txt` in your segatools)
5. Pet your cat 🐱
6. Link your card on the website. 

If you encounter any issue, please report in the [issue tracker](https://MewoLab/AquaDX/issues).

> [!TIP]  
> If you don't know your card ID, there's always a button on the login screen of the game that can read a card's access code.

## Self Hosting (Advanced)

> [!CAUTION]  
> This guide assumes you have basic programming & networking knowledge.  
> We will not be answering basic questions like how to set up port forwarding or domain records.  
> If you're new to self-hosting, please just use our public server in the Usage section above.

1. Install [Docker](https://www.docker.com/get-started/) and [Git](https://git-scm.com/downloads)
2. Run `git clone https://github.com/MewoLab/AquaDX` to clone this repo.
3. Run `docker compose up` in the AquaDX folder.

If you're getting BAD on title server checks after the docker server is up, please edit `config/application.properties` 
and change `allnet.server.host` to your LAN IP address (e.g. 192.168.0.?). You can find your LAN address using the `ipconfig` command on Windows or `ifconfig` on Linux.

> [!NOTE]  
> The guide above will create a new MariaDB database.  
> If you were using SQLite Aqua before, it is not supported in AquaDX. Please export your data and import it to MariaDB.  
> If you were using MySQL Aqua before, you can migrate to MariaDB using [this guide here](docs/mysql_to_mariadb.md).

### Configuration
Configuration is saved in `config/application.properties`, spring loads this file automatically.

* The host and port of game title servers can be overwritten in `allnet.server.host` and `allnet.server.port`. By default it will send the same host and port the client used the request this information.
This will be sent to the game at booting and being used by the following request.
* You can switch to the MariaDB database by commenting the Sqlite part.
* For some games, you might need to change some game-specific config entries.

### Updating Self-Hosted Instance

Please run the commands below in the AquaDX folder to update:

```
# Backup your database
docker run --rm -it mariadb:latest mariadb-dump -h host.docker.internal --port 3369 --user=cat --password=meow main > backup.sql

# Pull the new repository
docker compose pull

# Run the updated version
docker compose up
```

### Building
You need to install JDK 21 on your system, then run `./gradlew clean build`. The jar file will be built into the `build/libs` folder.

## License: [CC By-NC-SA](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en)

* **Attribution** — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
* **NonCommercial** — You may not use the material for commercial purposes.
* **ShareAlike** — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.

### Credit
* **samnyan**: The creator and developer of the original Aqua server
* **Akasaka Ryuunosuke**: providing all the DIVA protocol information
* **Dom Eori**: Developer of forked Aqua server, from v0.0.17 and up
* All devs who contribute to the [MiniMe server](https://dev.s-ul.net/djhackers/minime)
* All contributors by merge requests, issues and other channels

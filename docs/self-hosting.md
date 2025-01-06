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
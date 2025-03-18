#!/bin/bash
echo "Building AquaDX New Version..."
./tools/build.sh
mv build/aqua-nightly.zip .
unzip aqua-nightly.zip -d aquaUnzipped
mv aquaUnzipped/aqua.jar aquaUnzipped/AquaDX-1.0.0.jar
echo "Moving binary to Docker Container"
docker cp aquaUnzipped/AquaDX-1.0.0.jar aquadx-app-1:/app
rm -f aqua-nightly.zip
rm -rf aquaUnzipped
echo "Running meta update scripts"
./src/main/resources/meta/update.sh
docker cp src/main/resources/meta aquadx-app-1:/app
docker stop aquadx-app-1
docker start aquadx-app-1

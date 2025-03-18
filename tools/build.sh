#!/usr/bin/env bash

# Stop on error
set -e
set -x

# Build jar
./gradlew build --parallel -x test
rm -rf build/libs/aqua-nightly.jar
rm -rf build/libs/*-plain.jar
cp build/libs/*.jar "build/libs/aqua-nightly.jar"

echo "Creating release package"
PACK="build/release"
rm -rf "$PACK"
mkdir -p "$PACK"
cp build/libs/aqua-nightly.jar "$PACK/aqua.jar"
cp -r config "$PACK/config"
mkdir -p "$PACK/data"

NOTES="$PACK/ReleaseNotes.md"
echo '### AquaDX Nightly Release' >> "$NOTES"
echo "This nightly release is automatically built by github actions on $(date)." >> "$NOTES"
echo "https://github.com/hykilpikonna/AquaDX" >> "$NOTES"
echo '### The latest five updates are:' >> "$NOTES"
git log -"5" --format="- %H %s" | sed '/^$/d' >> "$NOTES"

echo "Compressing"
cd build/release
zip -r ../aqua-nightly.zip .
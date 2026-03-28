#!/usr/bin/env bash

# Current script dir
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

mkdir -p "$DIR/ongeki" "$DIR/mai2" "$DIR/chu3" "$DIR/wacca"

curl "https://aquadx.net/d/ongeki/00/all-music.json" -o "$DIR/ongeki/music.json"
curl "https://aquadx.net/d/mai2/00/all-music.json" -o "$DIR/mai2/music.json"
curl "https://aquadx.net/d/mai2/00/all-items.json" -o "$DIR/mai2/items.json"
curl "https://aquadx.net/d/chu3/00/all-music.json" -o "$DIR/chu3/music.json"
curl "https://aquadx.net/d/wacca/00/all-music.json" -o "$DIR/wacca/music.json"

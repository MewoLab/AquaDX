# Encryption Support

AquaDX supports encryption for Chunithm, Maimai and Ongeki.<br>
It is not required to connect and keys are NOT provided by default.

Card Maker has encryption features in the client, but they are not supported due to it's complexity.

## Adding new keys to AquaDX

> [!IMPORTANT]
> Ensure you're using AquaDX from source. Docker will work, as long as you use `docker-compose up --build`

1. Create a new file at `src/main/resources/data/game/<game name>/game_encryption.json`
2. Fill it out using the following schema:
```json
{
    "code": "SDHD",
    "versions": [215, 216],
    "key": "DECAFBADDECAFBADDECAFBADDECAFBADDECAFBADDECAFBADDECAFBADDECAFBAD",
    "iv": "DECAFBADDECAFBADDECAFBADDECAFBAD",
    "salt": "DECAFBADDECAFBAD",
    "iterations": 36
},
```

**`key`, `salt`, and `iv` MUST be hex strings. Do NOT insert raw ASCII strings.**<br>

Replace `code` with the correct code for your game (`SDHD` for Chunithm JP, `SDEZ` for Maimai JP, etc.).<br>
Set `versions` to be an array of game versions without the decimal (215 for 2.15, 150 for 1.50, etc.).<br> 
You MUST include rom patch versions in this array or it will not encrypt correctly.

- Maimai DX does NOT use iterations, you do not need to include it
- Ongeki always uses 64 iterations (you must include it)
- If using a specific Python script to rip Chunithm keys, focus on the `=>` entries.
- `key` is 32 bytes long. `iv` is 16 bytes long. `salt` is 8 bytes long. (hex strings should be twice as long as they are in bytes)
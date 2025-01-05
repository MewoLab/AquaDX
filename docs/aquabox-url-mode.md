# AquaBox URL Mode Setup Guide

## For users

1. Go to your Chuni game settings
2. Go down to "Enable AquaBox" or "Upgrade AquaBox"
3. Click on "Switch to URL mode"
4. Enter the base URL for your AquaBox

## For server owners / asset hosters

> :warning: Assets are already not hosted on AquaDX for legal reasons.<br>
> Hosting SEGA's assets may put you at higher risk of DMCA.

1. Extract your Chunithm Luminous game files.

    It is recommended you have the latest version of the game and all of the options your users may use.

    The script to generate the proper paths can be found in [tools/chusan-extractor.js](tools/chusan-extractor.js). Node.js or Bun is required.<br>
    Please read the comments at the top of the script for usage instructions.

2. Copy the new `chu3` folder where you need it to be (read #3 if you're hosting AquaNet and want to host on the same endpoints).
3. (Optional) Update `src/lib/config.ts`.
```ts
// Change this to the base url of where your assets are stored.
// If you are hosting on AquaNet, you can put the files @ /public/chu3 & use '/chu3' for your base url.
// This will work the same way as setting it on the UI does. TEST IT ON THE UI BEFORE YOU APPLY THIS CONFIG!!!
export const USERBOX_DEFAULT_URL = "/chu3"; 
```
4. Enjoy!
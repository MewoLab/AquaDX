# Game specific notes

## Chunithm (Chusan)

### Required patches
* No encryption & TLS

### Additional notes
* Class/Dan and National Matching modes will work after playing the first game  
    (both when you first set up the game and when you update the game's rom or options).
* National Matching requires [additional setup](chu3-national-matching.md).
* For user box customization, use the AquaNet website.
* Many aspects of the game may not work in freeplay mode, this is not a server-side restriction.

## Maimai DX

### Required patches
* No TLS
* No certificate pinning
* No URI obfuscation
* No encryption

### Non-working features
* KOP related
* Tournament mode
* Chart recommendation (Festival)

## O.N.G.E.K.I

### Required patches
* No TLS
* No certificate pinning
* No URI obfuscation
* No encryption

### Non-working features
* KOP related
* Physical cards

## Card Maker

### Required patches
* No TLS
* No encryption

### Support status
* Chunithm New: Yes, New (2.00)
* Maimai DX: Yes, Universe (1.20)
* O.N.G.E.K.I: No

### Additional notes
* Only stated version above are supported.
* Server does not consider gacha rarity and probability weight during card draw.
* Server returns same hard-coded serial for each cards. This is intentional behavior.
* Due to its high correlation with every game endpoints, this may cease to work after major game version up.

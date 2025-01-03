import type { ChusanMatchingOption } from "./generalTypes"

export const AQUA_HOST = 'https://aquadx.net/aqua'
export const DATA_HOST = 'https://aquadx.net'

// This will be displayed for users to connect from the client
export const AQUA_CONNECTION = 'aquadx.hydev.org'

export const TURNSTILE_SITE_KEY = '0x4AAAAAAASGA2KQEIelo9P9'
export const DISCORD_INVITE = 'https://discord.gg/FNgveqFF7s'
export const TELEGRAM_INVITE = 'https://t.me/+zBL4RZdyfvUzZGU1'
export const QQ_INVITE = 'https://qm.qq.com/q/wvNXbXbHbO'

// UI
export const FADE_OUT = { duration: 200 }
export const FADE_IN = { delay: 400 }
export const DEFAULT_PFP = '/assets/imgs/no_profile.png'

// USERBOX_ASSETS
export const HAS_USERBOX_ASSETS = true

// Meow meow meow

// Matching servers
export const CHU3_MATCHINGS: ChusanMatchingOption[] = [
  {
    name: "林国对战",
    ui: "https://chu3-match.sega.ink/rooms",
    guide: "https://performai.evilleaker.com/manual/games/chunithm/national_battle/",
    matching: "https://chu3-match.sega.ink/",
    reflector: "http://reflector.naominet.live:18080/",
    coop: ["RinNET", "MysteriaNET"],
  },
  {
    name: "Yukiotoko",
    ui: "https://yukiotoko.metatable.sh/",
    guide: "https://github.com/MewoLab/AquaDX/blob/v1-dev/docs/chu3-national-matching.md",
    matching: "http://yukiotoko.chara.lol:9004/",
    reflector: "http://yukiotoko.chara.lol:50201/",
    coop: ["Missless"]
  }
]

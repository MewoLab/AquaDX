import type { ChusanMatchingOption } from "./generalTypes"

export const AQUA_HOST = import.meta.env.VITE_AQUA_HOST
export const DATA_HOST = import.meta.env.VITE_DATA_HOST

// This will be displayed for users to connect from the client
export const AQUA_CONNECTION = import.meta.env.VITE_AQUA_CONNECTION

export const TURNSTILE_SITE_KEY = import.meta.env.VITE_TURNSTILE_SITE_KEY
export const DISCORD_INVITE = import.meta.env.VITE_DISCORD_INVITE
export const TELEGRAM_INVITE = import.meta.env.VITE_TELEGRAM_INVITE
export const QQ_INVITE = import.meta.env.VITE_QQ_INVITE

// UI
export const FADE_OUT = { duration: 200 }
export const FADE_IN = { delay: 400 }
export const DEFAULT_PFP = '/assets/imgs/no_profile.png'

export const ANNOUNCEMENT = '' // If set, will add an announcement to the top bar. Keep it short.

// Documentation for Userbox mode can be found in `docs/aquabox-url-mode.md`
// Please note that if this is set, it must be manually unset by users in Chuni Settings -> Update Userbox -> Switch to URL mode -> (empty value) -> Enter key
export const USERBOX_DEFAULT_URL = ""

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
    coop: ["Missless", "CozyNet", "GMG"]
  }
]

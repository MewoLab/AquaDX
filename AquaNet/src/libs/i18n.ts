import { EN_REF, type LocalizedMessages } from "./i18n/en_ref";
import { ZH } from "./i18n/zh";
import type { GameName } from "./scoring";

import zhCountires from "./i18n/zh_countries.json"
import enCountires from "./i18n/en_countries.json"

type Lang = 'en' | 'zh'

const msgs: Record<Lang, LocalizedMessages> = {
  en: EN_REF,
  zh: ZH
}

const countries: Record<Lang, typeof enCountires> = {
  en: enCountires,
  zh: zhCountires
}

let lang: Lang = 'en'

// Infer language from browser
if (navigator.language.startsWith('zh')) {
  lang = 'zh'
}

export function ts(key: string, variables?: { [index: string]: any }) {
  return t(key as keyof LocalizedMessages, variables)
}

/**
 * Load the translation for the given key
 *
 * TODO: Check for translation completion on build
 *
 * @param key
 * @param variables
 */
export function t(key: keyof LocalizedMessages, variables?: { [index: string]: any }) {
  // Check if the key exists
  let msg = msgs[lang][key]
  if (!msg) {
    // Check if the key exists in English
    if (!(msg = msgs.en[key])) {
      msg = key
      console.error(`ERROR!! Missing translation reference entry (English) for ${key}`)
    }
    else console.warn(`Missing translation for ${key} in ${lang}`)
  }
  // Replace variables
  if (variables) {
    return msg.replace(/\${(.*?)}/g, (_: string, v: string | number) => variables[v] + "")
  }
  return msg
}
Object.assign(window, { t })

export function getCountryName(code: keyof typeof enCountires) {
  return countries[lang][code]
}

export const GAME_TITLE: { [key in GameName]: string } =
  {chu3: t("game.chu3"), mai2: t("game.mai2"), ongeki: t("game.ongeki"), wacca: t("game.wacca")}

/**
 * Converts a two-letter country code to its corresponding flag emoji.
 *
 * The Unicode flag emoji is represented by two Regional Indicator Symbols.
 * Each letter in the country code is transformed into a Regional Indicator Symbol
 * by adding its alphabetical position (A = 0, B = 1, etc.) to the base code point U+1F1E6.
 *
 * @param countryCode - A two-letter ISO country code (e.g., "US", "GB").
 * @returns The corresponding flag emoji if the country code is valid; otherwise, an empty string.
 */
export function countryCodeToEmoji(countryCode: string): string {
  if (!countryCode) return ""
  if (countryCode.length !== 2) return ""

  // Convert the country code to uppercase to standardize it
  const code = countryCode.toUpperCase();

  // The base code point for Regional Indicator Symbol Letter A is 0x1F1E6.
  const OFFSET = 0x1F1E6;
  const firstCharCode = code.charCodeAt(0);
  const secondCharCode = code.charCodeAt(1);

  // 'A' has a char code of 65.
  const firstIndicator = OFFSET + (firstCharCode - 65);
  const secondIndicator = OFFSET + (secondCharCode - 65);

  // Create and return the flag emoji string
  return String.fromCodePoint(firstIndicator, secondIndicator);
}

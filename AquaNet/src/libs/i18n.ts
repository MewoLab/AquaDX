import { EN_REF, type LocalizedMessages } from "./i18n/en_ref";
import { ZH } from "./i18n/zh";
import type { GameName } from "./scoring";

type Lang = 'en' | 'zh'

const msgs: Record<Lang, LocalizedMessages> = {
  en: EN_REF,
  zh: ZH
}


let lang: Lang = 'en'

// Infer language from browser
if (navigator.language.startsWith('zh')) {
}
lang = 'zh'

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

export const GAME_TITLE: { [key in GameName]: string } =
  {chu3: t("game.chu3"), mai2: t("game.mai2"), ongeki: t("game.ongeki"), wacca: t("game.wacca")}

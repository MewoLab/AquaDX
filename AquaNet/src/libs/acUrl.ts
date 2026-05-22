// card handler. for dealing user QR scan.
// this will assumes that the link of the generated QR code in [access code]
// page has been altered by modder to link to custom 

import { ACCESSCODE_QKEY } from "./config";

const query = new URLSearchParams(location.hash.slice(1));
const lsKey = "acurl"

export const url_has = (): boolean => query.has(ACCESSCODE_QKEY);
export const get = (): string => localStorage.getItem(lsKey) || "";
export const has = (): boolean => !!localStorage.getItem(lsKey);
export const clear = (): void => localStorage.removeItem(lsKey);

export function check() {
    if (!url_has()) return

    console.log("Found access code in URL. Saving.")
    localStorage.setItem(lsKey, query.get(ACCESSCODE_QKEY) || "")
}

export function preview() {
  const card = get()
  const masked =
    card.slice(0, 4) +
    "x".repeat(card.length - 4);

  return masked.match(/.{1,4}/g)?.join(" ");
}

export function previewR() {
  const card = get()

  return card.match(/.{1,4}/g)?.join(" ");
}
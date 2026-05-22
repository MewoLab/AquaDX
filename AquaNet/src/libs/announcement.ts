import { writable } from "svelte/store"

export const ANNOUNCEMENT = writable('')

// This task must not bother the existing one. Keep it async in the background instead
void (async () => {
  let r = await fetch("/announcement.txt")
  if (!r.ok) return;

  ANNOUNCEMENT.set(await r.text())
})()
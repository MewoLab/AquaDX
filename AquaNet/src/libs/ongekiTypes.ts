import type { GenericGamePlaylog } from "./generalTypes";

export interface OngekiUserSummaryEntry {
    name: string
    iconId: number
    serverRank: number
    accuracy: number
    rating: number
    ratingHighest: number
    ranks: { name: string, count: number }[]
    maxCombo: number
    fullCombo: number
    allPerfect: number
    totalDxScore: number
    plays: number
    totalPlayTime: number
    joined: string
    lastSeen: string
    lastVersion: string
    best30: string
    best15: string
    recent10: string
    recent: GenericGamePlaylog[]
}

import { DATA_HOST } from "./config"
import type { MusicMeta } from "./generalTypes"

export type GameName = 'mai2' | 'chu3' | 'ongeki' | 'wacca'

const multTable = {
  'mai2': [
    [ 100.5, 22.4, 'SSSp' ],
    [ 100.0, 21.6, 'SSS' ],
    [ 99.5, 21.1, 'SSp' ],
    [ 99, 20.8, 'SS' ],
    [ 98, 20.3, 'Sp' ],
    [ 97, 20, 'S' ],
    [ 94, 16.8, 'AAA' ],
    [ 90, 15.2, 'AA' ],
    [ 80, 13.6, 'A' ],
    [ 75, 12, 'BBB' ],
    [ 70, 11.2, 'BB' ],
    [ 60, 9.6, 'B' ],
    [ 50, 8, 'C' ],
    [ 40, 6.4, 'D' ],
    [ 30, 4.8, 'D' ],
    [ 20, 3.2, 'D' ],
    [ 10, 1.6, 'D' ],
    [ 0, 0, 'D' ]
  ],

  // TODO: Fill in multipliers for Chunithm and Ongeki
  'chu3': [
    [ 100.9, 215, 'SSS+' ],
    [ 100.75, 200, 'SSS' ],
    [ 100.0, 0, 'SS' ],
    [ 97.5, 0, 'S' ],
    [ 95.0, 0, 'AAA' ],
    [ 92.5, 0, 'AA' ],
    [ 90.0, 0, 'A' ],
    [ 80.0, 0, 'BBB' ],
    [ 70.0, 0, 'BB' ],
    [ 60.0, 0, 'B' ],
    [ 50.0, 0, 'C' ],
    [ 0.0, 0, 'D' ]
  ],

  'ongeki': [
    [ 100.75, 0, 'SSS+' ],
    [ 100.0, 0, 'SSS' ],
    [ 99.0, 0, 'SS' ],
    [ 97.0, 0, 'S' ],
    [ 94.0, 0, 'AAA' ],
    [ 90.0, 0, 'AA' ],
    [ 85.0, 0, 'A' ],
    [ 80.0, 0, 'BBB' ],
    [ 75.0, 0, 'BB' ],
    [ 70.0, 0, 'B' ],
    [ 50.0, 0, 'C' ],
    [ 0.0, 0, 'D' ]
  ],

  'wacca': [
    [ 100.0, 0, 'AP' ],
    [ 98.0, 0, 'SSS' ],
    [ 95.0, 0, 'SS' ],
    [ 90.0, 0, 'S' ],
    [ 85.0, 0, 'AAA' ],
    [ 80.0, 0, 'AA' ],
    [ 70.0, 0, 'A' ],
    [ 60.0, 0, 'B' ],
    [ 1.0, 0, 'C' ],
    [ 0.0, 0, 'D' ]
  ],
}

export function getMult(achievement: number, game: GameName) {
  achievement /= 10000
  const mt = multTable[game]
  for (let i = 0; i < mt.length; i++) {
    if (achievement >= (mt[i][0] as number)) return mt[i]
  }
  return [ 0, 0, 0 ]
}

export function roundFloor(achievement: number, game: GameName, digits = 2) {
  // Round, but if the rounded number reaches the next rank, use floor instead
  const mult = getMult(achievement, game);
  achievement /= 10000
  const rounded = achievement.toFixed(digits);
  if (getMult(+rounded * 10000, game)[2] === mult[2] && rounded !== '101.0') return rounded;
  return (+rounded - Math.pow(10, -digits)).toFixed(digits);
}

export function chusanRating(lv: number, score: number) {
  lv = lv * 100
  if (score >= 1009000) return lv + 215; // SSS+
  if (score >= 1007500) return lv + 200 + (score - 1007500) / 100; // SSS
  if (score >= 1005000) return lv + 150 + (score - 1005000) / 50; // SS+
  if (score >= 1000000) return lv + 100 + (score - 1000000) / 100; // SS
  if (score >= 975000) return lv + (score - 975000) / 250; // S+, S
  if (score >= 925000) return lv - 300 + (score - 925000) * 3 / 500; // AA
  if (score >= 900000) return lv - 500 + (score - 900000) * 4 / 500; // A
  if (score >= 800000) return ((lv - 500) / 2 + (score - 800000) * ((lv - 500) / 2) / (100000)); // BBB
  return 0; // C
}

export interface ParsedComposition {
  name?: string
  musicId: number
  diffId: number // ID of the difficulty
  score: number
  cutoff: number
  mult: number
  rank: string // e.g. 'SSS+'
  difficulty?: number // Actual difficulty of the map
  img: string
  ratingChange?: string // Rating change after playing this map
}


export function parseComposition(item: string, allMusics: Record<string, MusicMeta>, game: GameName): ParsedComposition {
  // Chuni & ongeki: musicId, difficultId, score
  // Mai: musicId, level (difficultyId), romVersion, achievement (score)
  const mapData = item.split(':').map(Number)
  if (game === 'mai2') mapData.splice(2, 1)
  const [ musicId, diffId, score ] = mapData
  const meta = allMusics[musicId]

  // Get score multiplier
  const tup = getMult(score, game)
  const [ cutoff, mult ] = [ +tup[0], +tup[1] ]
  const rank = "" + tup[2]

  let diff = meta?.notes?.[diffId === 10 ? 0 : diffId]?.lv

  function calcDxChange() {
    if (!diff) return
    if (game === 'mai2')
      return Math.floor(diff * mult * (Math.min(100.5, score / 10000) / 100)).toFixed(0)
    if (game === 'chu3' || game === 'ongeki')
      return (Math.floor(chusanRating(diff, score)) / 100).toFixed(2)
  }

  return {
    name: meta?.name,
    musicId,
    diffId,
    score,
    cutoff,
    mult,
    rank,
    difficulty: diff,
    img: `${DATA_HOST}/d/${game}/music/00${mapData[0].toString().padStart(6, '0').substring(2)}.png`,
    ratingChange: calcDxChange()
  }
}

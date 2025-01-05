export interface TrendEntry {
  date: string
  rating: number
  plays: number
}

export interface Card {
  luid: string
  registerTime: string
  accessTime: string
  linked: boolean
  isGhost: boolean
}

export interface AquaNetUser {
  username: string
  email: string
  displayName: string
  country: string
  lastLogin: number
  regTime: number
  profileLocation: string
  profileBio: string
  profilePicture: string
  emailConfirmed: boolean
  ghostCard: Card
  cards: Card[]
  computedName: string,
  password: string,
  optOutOfLeaderboard: boolean,
}

export interface CardSummaryGame {
  name: string
  rating: number
  lastLogin: string
}

export interface CardSummary {
  mai2: CardSummaryGame | null
  chu3: CardSummaryGame | null
  ongeki: CardSummaryGame | null
  diva: CardSummaryGame | null
  wacca: CardSummaryGame | null
}


export interface ConfirmProps {
  title: string
  message: string
  confirm: () => void
  cancel?: () => void
  dangerous?: boolean
}

export interface GenericGamePlaylog {
  musicId: number
  level: number
  playDate: string
  achievement: number
  maxCombo: number
  totalCombo: number
  afterRating: number
  beforeRating: number
  isFullCombo?: boolean
  isAllPerfect?: boolean
  isAllJustice?: boolean
}

export interface GenericRanking {
  name: string
  username: string
  rank: number
  accuracy: number
  rating: number
  fullCombo: number
  allPerfect: number
  lastSeen: string
}

export interface RankCount {
  name: string
  count: number
}

export interface GenericGameSummary {
  name: string
  iconId: number
  aquaUser?: AquaNetUser
  serverRank: number
  accuracy: number
  rating: number
  ratingHighest: number
  ranks: RankCount[]
  detailedRanks: { [key: number]: { [key: string]: number } }
  maxCombo: number
  fullCombo: number
  allPerfect: number
  totalScore: number
  plays: number
  totalPlayTime: number
  joined: string
  lastSeen: string
  lastVersion: string
  ratingComposition: { [key: string]: any }
  recent: GenericGamePlaylog[]
  rival?: boolean
}

export interface MusicMeta {
  name: string,
  composer: string,
  bpm: number,
  ver: number,
  notes: {
    lv: number
    designer: string
    lv_id: number
    notes: number
  }[],
  worldsEndTag?: string
  worldsEndStars?: number
}

export type AllMusic = { [key: string]: MusicMeta }

export interface GameOption {
  key: string
  value: any
  type: 'Boolean' | 'String'
  game: string

  changed?: boolean
}

export interface UserItem { itemKind: number, itemId: number, stock: number }
export interface UserBox {
  userName: string,
  nameplateId: number,
  frameId: number,
  characterId: number,
  trophyId: number,
  mapIconId: number,
  voiceId: number,
  avatarWear: number,
  avatarHead: number,
  avatarFace: number,
  avatarSkin: number,
  avatarItem: number,
  avatarFront: number,
  avatarBack: number,

  level: number
  playerRating: number
}

export interface ChusanMatchingOption {
  name: string
  ui: string
  guide: string
  matching: string
  reflector: string
  coop: string[]
}
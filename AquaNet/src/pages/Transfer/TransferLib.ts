
interface AllNetSrc {
  card: string
  dns: string
  keychip: string
}

interface AllNetGame {
  game: string
  version: string
}

interface AllNetClient extends AllNetSrc, AllNetGame {}

interface TrCheckGood {
  gameUrl: string
  userId: number
}

type TrStreamMessage = { message: string } | { error: string } | { data: string }


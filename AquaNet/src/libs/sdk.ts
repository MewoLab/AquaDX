import { AQUA_HOST, DATA_HOST } from './config'
import type {
  AllMusic,
  Card,
  CardSummary,
  GenericGameSummary,
  GenericRanking,
  TrendEntry,
  AquaNetUser, GameOption,
  UserBox,
  UserItem,
  Dict
} from './generalTypes'
import type { GameName } from './scoring'

interface ExtReqInit extends RequestInit {
  params?: { [index: string]: string }
  json?: any
}

/**
 * Modify a fetch url
 *
 * @param input Fetch url input
 * @param callback Callback for modification
 */
export function reconstructUrl(input: URL | RequestInfo, callback: (url: URL) => URL | void): RequestInfo | URL {
  let u = new URL((input instanceof Request) ? input.url : input)
  const result = callback(u)
  if (result) u = result
  if (input instanceof Request) {
    // @ts-ignore
    return { url: u, ...input }
  }
  return u
}

/**
 * Fetch with url parameters
 */
export function fetchWithParams(input: URL | RequestInfo, init?: ExtReqInit): Promise<Response> {
  return fetch(reconstructUrl(input, u => {
    u.search = new URLSearchParams(init?.params ?? {}).toString()
  }), init)
}

/**
 * Do something with the response when it's not ok
 *
 * @param res Response object
 */
async function ensureOk(res: Response) {
  if (!res.ok) {
    const text = await res.text()
    console.error(`${res.status}: ${text}`)

    // If 400 invalid token is caught, should invalidate the token and redirect to signin
    if (text === 'Invalid token') {
      localStorage.removeItem('token')
      window.location.href = '/'
    }

    // Try to parse as json
    let json
    try {
      json = JSON.parse(text)
    } catch (e) {
      throw new Error(text)
    }
    if (json.error) throw new Error(json.error)
  }
}

/**
 * Post to an endpoint and return the response in JSON while doing error checks
 * and handling token (and token expiry) automatically.
 *
 * @param endpoint The endpoint to post to (e.g., '/pull')
 * @param params An object containing the request body or any necessary parameters
 * @param init Additional fetch/init configuration
 * @returns The JSON response from the server
 */
export async function post(endpoint: string, params: Dict = {}, init?: ExtReqInit): Promise<any> {
  return postHelper(endpoint, params, init).then(it => it.json())
}

/**
 * Actual impl of post(). This does not return JSON but returns response object.
 */
async function postHelper(endpoint: string, params: Dict = {}, init?: ExtReqInit): Promise<any> {
  // Add token if exists
  const token = localStorage.getItem('token')
  if (token && !('token' in params)) params = { ...(params ?? {}), token }

  if (init?.json) {
    init.body = JSON.stringify(init.json)
    init.headers = { 'Content-Type': 'application/json', ...init.headers }
    init.json = undefined
  }

  const res = await fetchWithParams(AQUA_HOST + endpoint, { method: 'POST', params, ...init })
    .catch(e => { console.error(e); throw new Error("Network error") })
  await ensureOk(res)

  return res
}

const decoder = new TextDecoder()

/**
 * Post with a stream response. Similar to post(), but the response will stream messages to onChunk.
 */
export async function postStream(endpoint: string, params: Dict = {}, onChunk: (data: any) => void, init?: ExtReqInit): Promise<void> {
  const res = await postHelper(endpoint, params, init)
  if (!res.body) {
    console.error('Response body is not a stream')
    return
  }

  // The response body is a ReadableStream. We'll read chunks as they arrive.
  const reader = res.body?.getReader()
  if (!reader) return
  let buffer = ''

  try {
    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      // Decode any new data, parse full lines, keep the rest in buffer
      buffer += decoder.decode(value, { stream: true })
      let fullLines = buffer.split('\n')
      buffer = fullLines.pop() ?? ''

      for (const line of fullLines) {
        if (!line.trim()) continue // skip empty lines
        onChunk(JSON.parse(line))
      }
    }

    // If there's leftover data in 'buffer' after stream ends, parse
    if (buffer.trim())
      onChunk(JSON.parse(buffer.trim()))

  } finally {
    reader.releaseLock()
  }
}

/**
 * aqua.net.UserRegistrar
 *
 * @param user
 */
async function register(user: { username: string, email: string, password: string, turnstile: string }) {
  return await post('/api/v2/user/register', user)
}

async function login(user: { email: string, password: string, turnstile: string }) {
  const data = await post('/api/v2/user/login', user)

  // Put token into local storage
  localStorage.setItem('token', data.token)
}

async function resetPassword(user: { email: string, turnstile: string }) {
  return await post('api/v2/user/reset-password', user)
}

async function changePassword(user: { token: string, password: string }) {
  return await post('/api/v2/user/change-password', user)
}

const isLoggedIn = () => !!localStorage.getItem('token')
const ensureLoggedIn = () => !isLoggedIn() && (window.location.href = '/')

export const USER = {
  register,
  login,
  resetPassword,
  changePassword,
  confirmEmail: (token: string) =>
    post('/api/v2/user/confirm-email', { token }),
  me: (): Promise<AquaNetUser> => {
    ensureLoggedIn()
    return post('/api/v2/user/me', {})
  },
  keychip: (): Promise<string> =>
    post('/api/v2/user/keychip', {}).then(it => it.keychip),
  setting: (key: string, value: string) =>
    post('/api/v2/user/setting', { key: key === 'password' ? 'pwHash' : key, value }),
  uploadPfp: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return post('/api/v2/user/upload-pfp', { }, { method: 'POST', body: formData })
  },
  isLoggedIn,
  ensureLoggedIn,
}

export const USERBOX = {
  getProfile: (): Promise<{ user: UserBox, items: UserItem[] }> =>
    post('/api/v2/game/chu3/user-box', {}),
  setUserBox: (d: { field: string, value: number | string }) =>
    post(`/api/v2/game/chu3/user-detail-set`, d),
  getUserProfile: (username: string): Promise<UserBox> =>
    post(`/api/v2/game/chu3/user-detail`, {username})
}

export const CARD = {
  summary: (cardId: string): Promise<{card: Card, summary: CardSummary}> =>
    post('/api/v2/card/summary', { cardId }),
  link: (props: { cardId: string, migrate: string }) =>
    post('/api/v2/card/link', props),
  unlink: (cardId: string) =>
    post('/api/v2/card/unlink', { cardId }),
  userGames: (username: string): Promise<CardSummary> =>
    post('/api/v2/card/user-games', { username }),
}

export const GAME = {
  trend: (username: string, game: GameName): Promise<TrendEntry[]> =>
    post(`/api/v2/game/${game}/trend`, { username }),
  photos: (): Promise<string[]> =>
    post(`/api/v2/game/mai2/my-photo`, { }),
  userSummary: (username: string, game: GameName): Promise<GenericGameSummary> =>
    post(`/api/v2/game/${game}/user-summary`, { username }),
  ranking: (game: GameName): Promise<GenericRanking[]> =>
    post(`/api/v2/game/${game}/ranking`, { }),
  changeName: (game: GameName, newName: string): Promise<{ newName: string }> =>
    post(`/api/v2/game/${game}/change-name`, { newName }),
  export: (game: GameName): Promise<Record<string, any>> =>
    post(`/api/v2/game/${game}/export`),
  import: (game: GameName, data: any): Promise<Record<string, any>> =>
    post(`/api/v2/game/${game}/import`, {}, { json: data }),
  importMusicDetail: (game: GameName, data: any): Promise<Record<string, any>> =>
    post(`/api/v2/game/${game}/import-music-detail`, {}, { json: data }),
  setRival: (game: GameName, rivalUserName: string, isAdd: boolean) =>
    post(`/api/v2/game/${game}/set-rival`, { rivalUserName, isAdd }),
}

export const DATA = {
  allMusic: (game: GameName): Promise<AllMusic> =>
    fetch(`${DATA_HOST}/d/${game}/00/all-music.json`).then(it => it.json()),
  allItems: (game: GameName): Promise<Record<string, Record<string, any>>> =>
    fetch(`${DATA_HOST}/d/${game}/00/all-items.json`).then(it => it.json()),
}

export const SETTING = {
  get: (): Promise<GameOption[]> =>
    post('/api/v2/settings/get', {}),
  set: (key: string, value: any) =>
    post('/api/v2/settings/set', { key, value: `${value}` }),
  detailSet: (game: string, field: string, value: any) =>
    post(`/api/v2/game/${game}/user-detail-set`, { field, value }),
}

export const TRANSFER = {
  check: (d: AllNetClient): Promise<TrCheckGood> =>
    post('/api/v2/transfer/check', {}, { json: d }),
  pull: (d: AllNetClient, callback: (data: TrStreamMessage) => void) =>
    postStream('/api/v2/transfer/pull', {}, callback, { json: d }),
  push: (d: AllNetClient, data: string) =>
    post('/api/v2/transfer/push', {}, { json: { client: d, data } }),
}

export const FEDY = {
  status: (): Promise<{ linkedAt: number }> =>
    post('/api/v2/fedy/status'),
  link: (nonce: string): Promise<{ linkedAt: number }> =>
    post('/api/v2/fedy/link', { nonce }),
  unlink: () =>
    post('/api/v2/fedy/unlink'),
}

// @ts-ignore
window.sdk = { USER, USERBOX, CARD, GAME, DATA, SETTING, TRANSFER, FEDY }

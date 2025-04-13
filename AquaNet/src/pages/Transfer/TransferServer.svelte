<script lang="ts">
  import StatusOverlays from "../../components/StatusOverlays.svelte";
  import { t } from "../../libs/i18n";
  import { TRANSFER } from "../../libs/sdk";
  import { download, selectJsonFile } from "../../libs/ui";
  import InputTextShort from "./InputTextShort.svelte";
  import { createEventDispatcher } from 'svelte';

  const dispatch = createEventDispatcher()

  export let src: AllNetSrc
  export let gameInfo: AllNetGame
  export let isSrc: boolean = true

  export let tested: boolean = false
  let [loading, error] = [false, ""]

  const blacklist = ['amime.missless.net']

  function testConnection() {
    if (loading || isBlacklist) return

    // Preliminiary checks
    if (!src.dns || !src.keychip || !src.card || !gameInfo.game || !gameInfo.version) {
      error = t('trans.error.empty')
      return
    }

    loading = true
    error = ""
    console.log("Testing connection...")
    return TRANSFER.check({...src, ...gameInfo}).then(res => {
      console.log("Connection test result:", res)
      tested = true
    }).catch(err => error = err.message).finally(() => loading = false)
  }

  let messages: string[] = []
  export let exportedData: string = ""

  export function pull(): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      if (loading || !tested) return reject(t('trans.error.untested'))
      if (exportedData) return resolve(exportedData)
      console.log("Exporting data...")
      error = ""

      TRANSFER.pull({...src, ...gameInfo}, (msg: TrStreamMessage) => {
        console.log("Export progress: ", JSON.stringify(msg))

        if ('message' in msg) messages = [...messages, msg.message]

        if ('error' in msg) {
          error = msg.error
          reject(msg.error)
        }

        if ('data' in msg) {
          // file name: Export YYYY-MM-DD {server host} {game} {card last 6}.json
          let date = new Date().toISOString().split('T')[0]
          let host = new URL(src.dns).hostname
          download(msg.data, `Export ${date} ${host} ${gameInfo.game} ${src.card.slice(-6)}.json`)
          exportedData = msg.data
          resolve(msg.data)
        }
      }).catch(err => { error = err; reject(err) })
    })
  }

  function pushBtn() {
    if (loading || !tested) return
    selectJsonFile().then(obj => push(JSON.stringify(obj)))
  }

  export function push(data: string) {
    if (loading || !tested) return
    console.log("Import data...")
    loading = true
    error = ""

    return TRANSFER.push({...src, ...gameInfo}, data).then(() => {
      console.log("Data imported successfully")
      messages = [t('trans.success.import')]
    }).catch(err => error = err.message).finally(() => loading = false)
  }

  $: isBlacklist = !!blacklist.filter(x => src.dns.includes(x))
</script>

<StatusOverlays {loading} />

<div class="server source" class:src={isSrc} class:hasError={error} class:tested={tested}>
  <h3>{t(`trans.${isSrc ? "source" : "target"}.title`)}</h3>

  {#if !isSrc && isBlacklist}
    <blockquote class="error-msg">{t('trans.blacklist')}</blockquote>
  {/if}

  {#if error}
    <blockquote class="error-msg">{error}</blockquote>
  {/if}

  <!-- First input line -->
  <div class="inputs">
    <InputTextShort desc={t('trans.field.addr')} placeholder="e.g. http://aquadx.hydev.org"
      bind:value={src.dns} on:change disabled={tested}
      validate={v => /^https?:\/\/[a-z0-9.-]+(:\d+)?$/i.test(v)} />
    <InputTextShort desc={t('trans.field.keychip')} placeholder="e.g. A0299792458"
      bind:value={src.keychip} on:change disabled={tested}
      validate={v => /^([A-Z0-9]{11}|[A-Z0-9]{4}-[A-Z0-9]{11})$/.test(v)} />
  </div>

  <!-- Second input line -->
  <div class="inputs">
    <div class="game-version">
      <InputTextShort desc={t('trans.field.game')} placeholder="e.g. SDHD"
        bind:value={gameInfo.game} on:change disabled={tested} />
      <InputTextShort desc={t('trans.field.version')} placeholder="e.g. 2.30"
        bind:value={gameInfo.version} on:change disabled={tested} />
    </div>
    <InputTextShort desc={t('trans.field.card')} placeholder="e.g. 27182818284590452353"
      bind:value={src.card} disabled={tested} on:change={value => {
        src.card = src.card.replaceAll(' ', '')
        dispatch('change', { value });
      }} />
  </div>

  <!-- Streaming messages -->
  {#if messages.length > 0}
    <div class="stream-messages">
      {#each messages.slice(Math.max(messages.length - 5, 0), undefined) as msg}
        <p>{msg}</p>
      {/each}
    </div>
  {/if}

  <!-- Buttons -->
  <div class="inputs buttons">
    {#if !tested}
      <button class="flex-1" on:click={testConnection} disabled={loading}>{t('trans.btn.test')}</button>
    {:else}
      <button class="flex-1" on:click={pull}>{t('trans.btn.export')}</button>
      <button class="flex-1" on:click={pushBtn}>{t('trans.btn.import')}</button>
    {/if}
  </div>
</div>

<style lang="sass">
  @use "../../vars"
  @use "sass:color"

  .error-msg
    white-space: pre-wrap
    margin: 0

  .server
    display: flex
    flex-direction: column
    gap: 1rem

    // --c-src: 202, 168, 252
    --c-src: 179, 198, 255
    // animation: hue-rotate 10s infinite linear
    // &.src
      // --c-src: 173, 192, 247
      // animation: hue-rotate 10s infinite linear reverse

    &.tested
      --c-src: 169, 255, 186

    &.hasError
      --c-src: 255, 174, 174
      animation: none

    padding: 1rem
    border-radius: vars.$border-radius
    // background-color: vars.$ov-light
    background: #252525

    // Pink outline
    border: 1px solid rgba(var(--c-src), 0.5)
    box-shadow: 0 0 1rem 0 rgba(var(--c-src), 0.25)

    h3
      margin: 0
      font-size: 1.5rem
      text-align: center


  // @keyframes hue-rotate
  //   0%
  //     filter: hue-rotate(0deg)
  //   100%
  //     filter: hue-rotate(360deg)

  .inputs
    display: flex
    flex-wrap: wrap
    gap: 1rem

    .game-version
      flex: 60
      display: flex
      gap: 1rem

      :global(> *)
        width: 100px

    &.buttons
      margin-top: 0.5rem

  .stream-messages
    font-size: 0.8rem
    opacity: 0.8

    margin-top: 0.5rem
    padding: 0 0.5rem
</style>

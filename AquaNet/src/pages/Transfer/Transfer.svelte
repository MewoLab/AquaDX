<script lang="ts">
  import { slide } from "svelte/transition";
  import { t, ts } from "../../libs/i18n";
  import TransferServer from "./TransferServer.svelte";
  import { DATA_HOST } from "../../libs/config";
  import type { ConfirmProps } from "../../libs/generalTypes";
  import StatusOverlays from "../../components/StatusOverlays.svelte";


  let tabs = ['chu3', 'mai2', 'ongeki']
  let game: Record<string, { game: string, version: string }> = {
    'chu3': { game: "SDHD", version: "2.30" },
    'mai2': { game: "SDGA", version: "1.50" },
    'ongeki': { game: "SDDT", version: "1.45" }
  }
  let tab = 0

  let src = JSON.parse(localStorage.getItem('src') ?? `{"dns": "", "card": "", "keychip": ""}`)
  let dst = JSON.parse(localStorage.getItem('dst') ?? `{"dns": "", "card": "", "keychip": ""}`)
  let [srcTested, dstTested] = [false, false]
  let gameInfo = JSON.parse(localStorage.getItem('gameInfo') ?? `{"game": "", "version": ""}`)

  let srcEl: TransferServer, dstEl: TransferServer
  let srcExportedData: string
  let [error, loading] = ["", false]
  let confirm: ConfirmProps | null = null

  function defaultGame() {
    gameInfo.game = game[tabs[tab]].game
    gameInfo.version = game[tabs[tab]].version
  }

  function onChange() {
    localStorage.setItem('src', JSON.stringify(src))
    localStorage.setItem('dst', JSON.stringify(dst))
    localStorage.setItem('gameInfo', JSON.stringify(gameInfo))
  }

  function actuallyStartTransfer() {
    srcEl.pull()
      .then(() => dstEl.push(srcExportedData))
      .then(() => confirm = {
        title: t('trans.confirm.done.title'),
        message: t('trans.confirm.done.msg', { src: src.dns, dst: dst.dns })
      })
      .catch(e => error = e)
      .finally(() => loading = false)
  }

  function startTransfer() {
    if (!(srcTested && dstTested)) return confirm = {
      title: t('trans.confirm.untested.title'),
      message: t('trans.confirm.untested.msg')
    }
    if (loading) return alert(t('trans.alert.in-progress'))
    console.log("Starting transfer...")
    loading = true

    if (dstEl.exportedData) return actuallyStartTransfer()

    // Ask user to make sure to backup their data
    confirm = {
      title: t('trans.confirm.unbackuped.title'),
      message: t('trans.confirm.unbackuped.msg'),
      dangerous: true,
      confirm: actuallyStartTransfer,
      cancel: () => { loading = false }
    }
  }

  defaultGame()
</script>

<StatusOverlays bind:confirm={confirm} {error} />

<main class="content">
  <div class="outer-title-options">
    <h2>{t('trans.title')}</h2>
    <nav>
      {#each tabs as tabName, i}
        <div transition:slide={{axis: 'x'}} class:active={tab === i}
             on:click={() => tab = i} on:keydown={e => e.key === 'Enter' && (tab = i)}
             role="button" tabindex="0">
          {ts(`settings.tabs.${tabName}`)}
        </div>
      {/each}
    </nav>
  </div>

  <div class="prompt">
    {@html t("trans.prompt-html")}
  </div>

  <TransferServer bind:src={src} bind:gameInfo={gameInfo} on:change={onChange}
    bind:tested={srcTested} bind:this={srcEl} bind:exportedData={srcExportedData} />

  <div class="arrow" class:disabled={!(srcTested && dstTested)}>
    <img src="{DATA_HOST}/d/DownArrow.png" alt="arrow" on:click={startTransfer}>
  </div>

  <TransferServer bind:src={dst} bind:gameInfo={gameInfo} on:change={onChange}
    bind:tested={dstTested} bind:this={dstEl} isSrc={false} />
</main>


<style lang="sass">
  .arrow
    width: 100%
    display: flex
    justify-content: center
    margin-top: -40px
    margin-bottom: -40px
    z-index: 1

    &.disabled
      filter: grayscale(1)

    // CSS animation to let the image opacity breathe
    img
      animation: breathe 1s infinite alternate

  @keyframes breathe
    0%
      opacity: 0.5
    100%
      opacity: 1
</style>



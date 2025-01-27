<!-- Svelte 4.2.11 -->

<script lang="ts">
  import { fade, slide } from "svelte/transition"
  import type { Card, CardSummary, CardSummaryGame, ConfirmProps, AquaNetUser } from "../../libs/generalTypes"
  import { CARD, USER } from "../../libs/sdk"
  import moment from "moment"
  import Icon from "@iconify/svelte"
  import StatusOverlays from "../../components/StatusOverlays.svelte"
  import { t } from "../../libs/i18n"

  // State
  let state: 'ready' | 'linking-AC' | 'linking-SN' | 'loading' = "loading"
  let showConfirm: ConfirmProps | null = null

  let error: string = ""
  let me: AquaNetUser | null = null
  let accountCardSummary: CardSummary | null = null

  // Fetch data for current user
  const updateMe = () => USER.me().then(m => {
    me = m
    m.cards.sort((a, b) => a.registerTime < b.registerTime ? 1 : -1)
    CARD.summary(m.ghostCard.luid).then(s => accountCardSummary = s.summary)

    // Always put the ghost card at the top
    m.cards.sort((a, b) => a.isGhost ? -1 : 1)
    state = "ready"
  }).catch(e => error = e.message)
  updateMe()

  // Data conflict overlay
  let conflictCardID: string = ""
  let conflictSummary: CardSummary | null = null
  let conflictGame: string = ""
  let conflictNew: CardSummaryGame | null = null
  let conflictOld: CardSummaryGame | null = null
  let conflictToMigrate: string[] = []

  function setError(msg: string, type: 'AC' | 'SN') {
    type === 'AC' ? errorAC = msg : errorSN = msg
  }

  async function doLink(id: string, migrate: string) {
    try {
      await CARD.link({cardId: id, migrate})
      await updateMe()
      if (linkingType === 'AC') inputAC = ""
      if (linkingType === 'SN') inputSN = ""
    } catch (e) {
      setError(e.message, linkingType)
    }
    state = "ready"
  }

  let linkingType: 'AC' | 'SN' = null
  async function link(type: 'AC' | 'SN') {
    if (state !== 'ready' || accountCardSummary === null) return
    state = "linking-" + type
    linkingType = type
    const id = type === 'AC' ? inputAC : inputSN

    console.log("linking card", id)

    // Check if this card is already linked in the account
    if (me?.cards?.some(c => formatLUID(c.luid, c.isGhost).toLowerCase() === id.toLowerCase())) {
      setError(t('home.linkcard.linked-own'), type)
      state = "ready"
      return
    }

    // First, lookup the card summary
    const card = (await CARD.summary(id).catch(e => {
      // If card is not found, create a card and link it
      if (e.message === 'Card not found') {
        doLink(id, "")
        return
      }

      setError(e.message, type)
      state = "ready"
      return
    }))!
    const summary = card.summary

    // Check if it's already linked
    if (card.card.linked) {
      setError(t('home.linkcard.linked-another'), type)
      state = "ready"
      return
    }

    // If all games in summary are null or doesn't conflict with the ghost card,
    // we can link the card directly
    if (Object.keys(summary).every(k => summary[k as keyof CardSummary] === null
        || accountCardSummary!![k as keyof CardSummary] === null)) {
      console.log("linking card directly")
      await doLink(id, Object.keys(summary).filter(k => summary[k as keyof CardSummary] !== null).join(","))
    }

    // For each conflicting game, ask the user if they want to migrate the data
    else {
      conflictSummary = summary
      conflictCardID = id
      await linkConflictContinue(null)
    }
  }

  async function linkConflictContinue(choose: "old" | "new" | null) {
    if (accountCardSummary === null || conflictSummary === null) return
    console.log("linking card with migration")

    if (choose) {
      // If old is chosen, nothing needs to be migrated
      // If new is chosen, we need to migrate the data
      if (choose === "new") {
        conflictToMigrate.push(conflictGame)
      }
      // Continue to the next card
      conflictSummary[conflictGame as keyof CardSummary] = null
    }

    let isConflict = false
    for (const k in conflictSummary) {
      conflictNew = conflictSummary[k as keyof CardSummary]
      conflictOld = accountCardSummary[k as keyof CardSummary]
      conflictGame = k
      if (!conflictNew || !conflictOld) continue

      isConflict = true
      break
    }

    // If there are no longer conflicts, we can link the card
    if (!isConflict) {
      await doLink(conflictCardID, conflictToMigrate.join(","))

      // Reset the conflict state
      linkConflictCancel()
    }
  }

  function linkConflictCancel() {
    state = "ready"
    conflictSummary = null
    conflictCardID = ""
    conflictGame = ""
    conflictNew = null
    conflictOld = null
    conflictToMigrate = []
  }

  async function unlink(card: Card) {
    showConfirm = {
      title: t('home.linkcard.unlink'),
      message: t('home.linkcard.unlink-notice'),
      confirm: async () => {
        await CARD.unlink(card.luid)
        await updateMe()
        showConfirm = null
      },
      cancel: () => showConfirm = null,
      dangerous: true
    }
  }

  function cursorPositionToCursorIndex(text: string, cursorPosition: number, effectiveCharsRegex: RegExp) {
    const textBeforeCursor = text.slice(0, cursorPosition)
    const ignoredChars = textBeforeCursor.replace(new RegExp(effectiveCharsRegex, "g"), "")
    return textBeforeCursor.length - ignoredChars.length
  }

  function cursorIndexToCursorPosition(text: string, cursorIndex: number, effectiveCharsRegex: RegExp) {
    let i = 0
    while (i < text.length) {
      while (i < text.length && !effectiveCharsRegex.test(text[i])) i++
      if (cursorIndex === 0) break
      cursorIndex--
      i++
    }
    return i
  }

  // Access code input
  const inputACRegex = /^(\d{4} ){0,4}\d{0,4}$/
  let elemInputAC: HTMLInputElement
  let inputOldAC = ""
  let inputAC = ""
  let errorAC = ""
  let warningAC = ""

  function inputACChange() {
    // Add spaces to the input
    const cursorIndex = cursorPositionToCursorIndex(inputAC, elemInputAC.selectionStart, /\d/)
    inputAC = inputAC.replace(/\D/g, '').replace(/(.{4})/g, '$1 ').replace(/ $/, '')
    const cursorPosition = cursorIndexToCursorPosition(inputAC, cursorIndex, /\d/)
    setTimeout(() => elemInputAC.selectionStart = elemInputAC.selectionEnd = cursorPosition, 0)
    if (inputAC !== inputOldAC) errorAC = ""
    warningAC = inputAC[0] === "5" ? t('home.linkcard.felica-ac-warning') : ""

    inputOldAC = inputAC
  }

  // Serial number input
  const inputSNRegex = /^([0-9A-Fa-f]{0,2}:){0,7}[0-9A-Fa-f]{0,2}$/
  let inputElemSN: HTMLInputElement
  let inputOldSN = ""
  let inputSN = ""
  let errorSN = ""

  function inputSNChange() {
    // Add colons to the input
    inputSN = inputSN.toUpperCase()
    const cursorIndex = cursorPositionToCursorIndex(inputSN, inputElemSN.selectionStart, /[0-9A-F]/)
    inputSN = inputSN.replace(/[^0-9A-F]/g, '').replace(/(.{2})/g, '$1:').replace(/:$/, '')
    const cursorPosition = cursorIndexToCursorPosition(inputSN, cursorIndex, /[0-9A-F]/)
    setTimeout(() => inputElemSN.selectionStart = inputElemSN.selectionEnd = cursorPosition, 0)
    if (inputSN !== inputOldSN) errorSN = ""

    inputOldSN = inputSN
  }

  function formatLUID(luid: string, ghost: boolean = false) {
    if (ghost) return luid.slice(0, 6) + " " + (luid.slice(6).match(/.{4}/g)?.join(" ") ?? "")
    switch (cardType(luid)) {
      case "FeliCa SN":
        return BigInt(luid).toString(16).toUpperCase().padStart(16, "0").match(/.{1,2}/g)!.join(":")
      case "Access Code":
        return luid.match(/.{4}/g)!.join(" ")
      default:
        return luid
    }
  }

  function cardType(luid: string) {
    if (luid.startsWith("00")) return "FeliCa SN"
    if (luid.length === 20) return "Access Code"
    if (luid.includes(":")) return "FeliCa SN"
    if (luid.includes(" ")) return "Access Code"
    return "Unknown"
  }

  function isInput(e: KeyboardEvent) {
    return e.key.length === 1 && !e.altKey && !e.ctrlKey && !e.metaKey && !e.shiftKey
  }
  
  async function dropFile(e: DragEvent) {
    e.preventDefault()
    e.stopPropagation()
    const file = e.dataTransfer?.files[0]
    if (!file) return
    switch (file.name.toLowerCase()) {
      case "aime.txt":
        inputSN = ""
        inputAC = await file.text()
        inputACChange()
        break
      case "felica.txt":
        inputAC = ""
        inputSN = await file.text()
        inputSNChange()
        break
    }
  }
</script>

<!-- svelte-ignore a11y-no-static-element-interactions -->
<div class="link-card" on:drop={dropFile} on:dragover={(e) => e.preventDefault()}>
  <h2>{t('home.linkcard.cards')}</h2>
  <p>{t('home.linkcard.description')}:</p>

  {#if me}
    <div class="existing-cards" transition:slide>
      {#each me.cards as card (card.luid)}
        <div class:ghost={card.isGhost} class='existing card' transition:fade|global>
          <span class="type">{card.isGhost ? t('home.linkcard.account-card') : cardType(card.luid)}</span>
          <span class="register">{t('home.linkcard.registered')}: {moment(card.registerTime).format("YYYY MMM DD")}</span>
          <span class="last">{t('home.linkcard.lastused')}: {moment(card.accessTime).format("YYYY MMM DD")}</span>
          <div></div>
          <span class="id">{formatLUID(card.luid, card.isGhost)}</span>
          {#if !card.isGhost}
            <button class="icon error" on:click={() => unlink(card)}><Icon icon="tabler:trash-x-filled"/></button>
          {/if}
        </div>
      {/each}
    </div>
  {/if}

  <h2>{t('home.link-card')}</h2>
  <p>{t('home.linkcard.enter-info')}:</p>
  {#if !inputSN}
    <div out:slide={{ duration: 250 }}>
  <p>{t('home.linkcard.access-code')}</p>
  <label>
    <!-- DO NOT change the order of bind:value and on:input. Their order determines the order of reactivity -->
    <input bind:this={elemInputAC}
           placeholder="e.g. 2408 1234 5678 9012 3456 / 0008 1234 5678 8765 4321"
           on:keydown={(e) => {
             e.key === "Enter" && link('AC')
             // Ensure key is numeric
             if (isInput(e) && !/[\d ]/.test(e.key)) e.preventDefault()
           }}
           bind:value={inputAC}
           on:input={inputACChange}
           class:error={inputAC && (!inputACRegex.test(inputAC) || errorAC)}
           class:warning={inputAC && warningAC}>
    {#if inputAC.length > 0}
      <button transition:slide={{axis: 'x'}} on:click={() => link('AC')}>{t('home.linkcard.link')}</button>
    {/if}
  </label>
  {#if errorAC}
    <p class="error" style={warningAC ? "margin-bottom: 0" : ""} transition:slide>{errorAC}</p>
  {/if}
  {#if warningAC}
    <!-- Transition temporarily adds `overflow: hidden` which leads to BFC issue, breaking margin collapse -->
    <div style="overflow: hidden" transition:slide>
      {#each warningAC.trim().split("\n") as paragraph}
        <p class="warning">{paragraph}</p>
      {/each}
    </div>
  {/if}
    </div>
    {/if}

  {#if !inputAC}
    <div out:slide={{ duration: 250 }}>
  <p>{t('home.linkcard.enter-sn1')}
    (<a href="https://play.google.com/store/apps/details?id=com.wakdev.wdnfc">Android</a> /
    <a href="https://apps.apple.com/us/app/nfc-tools/id1252962749">Apple</a>)
    {t('home.linkcard.enter-sn2')}
  </p>
  <label>
    <input bind:this={inputElemSN}
           placeholder="e.g. 01:2E:1A:2B:3C:4D:5E:6F"
           on:keydown={(e) => {
             e.key === "Enter" && link('SN')
             // Ensure key is hex or colon
             if (isInput(e) && !/[0-9A-Fa-f:]/.test(e.key)) e.preventDefault()
           }}
           bind:value={inputSN}
           on:input={inputSNChange}
           class:error={inputSN && (!inputSNRegex.test(inputSN) || errorSN)}>
    {#if inputSN.length > 0}
      <button transition:slide={{axis: 'x'}} on:click={() => link('SN')}>{t('home.linkcard.link')}</button>
    {/if}
  </label>
  {#if errorSN}
    <p class="error" transition:slide>{errorSN}</p>
  {/if}
    </div>
    {/if}

  {#if conflictOld && conflictNew && me}
    <div class="overlay" transition:fade>
      <div>
        <h2>{t('home.linkcard.data-conflict')}</h2>
        <p></p>
        <div class="conflict-cards">
          <div class="old card clickable" on:click={() => linkConflictContinue('old')}
               role="button" tabindex="0" on:keydown={e => e.key === "Enter" && linkConflictContinue('old')}>
            <span class="type">{t('home.linkcard.account-card')}</span>
            <span>{t('home.linkcard.name')}: {conflictOld.name}</span>
            <span>{t('home.linkcard.rating')}: {conflictOld.rating}</span>
            <span>{t('home.linkcard.last-login')}: {moment(conflictOld.lastLogin).format("YYYY MMM DD")}</span>
            <span class="id">{formatLUID(me.ghostCard.luid, true)}</span>
          </div>
          <div class="new card clickable" on:click={() => linkConflictContinue('new')}
               role="button" tabindex="0" on:keydown={e => e.key === "Enter" && linkConflictContinue('new')}>
            <span class="type">{cardType(conflictCardID)}</span>
            <span>{t('home.linkcard.name')}: {conflictNew.name}</span>
            <span>{t('home.linkcard.rating')}: {conflictNew.rating}</span>
            <span>{t('home.linkcard.last-login')}: {moment(conflictNew.lastLogin).format("YYYY MMM DD")}</span>
            <span class="id">{conflictCardID}</span>
          </div>
        </div>
        <button class="error" on:click={linkConflictCancel}>{t('action.cancel')}</button>
      </div>
    </div>
  {/if}

  <StatusOverlays bind:confirm={showConfirm} bind:error={error} loading={!me} />
</div>

<style lang="sass">
  @use "../../vars"

  .link-card
    input
      width: 100%

    label
      display: flex

      button
        margin-left: 1rem

    .existing-cards, .conflict-cards
      display: grid
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr))
      gap: 1rem

    .existing-cards .existing.card
      min-height: 90px
      position: relative
      overflow: hidden

      *
        white-space: nowrap

      &.ghost
        background: rgba(vars.$c-darker, 0.8)

      .register, .last
        opacity: 0.7

      span:not(.type)
        font-size: 0.8rem

      > div
        flex: 1

      button
        position: absolute
        right: 10px
        bottom: 10px

    .conflict-cards
      .card
        transition: vars.$transition

      .card:hover
        background: vars.$c-darker

      span:not(.type)
        font-size: 0.8rem

      .id
        opacity: 0.7

</style>

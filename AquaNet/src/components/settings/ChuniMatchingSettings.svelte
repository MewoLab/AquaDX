<script lang="ts">
  import { fade, slide } from "svelte/transition";
  import { CHU3_MATCHINGS } from "../../libs/config.js";
  import type { ChusanMatchingOption, GameOption } from "../../libs/generalTypes.js";
  import { t, ts } from "../../libs/i18n.js";
  import { DATA, SETTING } from "../../libs/sdk.js";
  import StatusOverlays from "../StatusOverlays.svelte";
  import GameSettingFields from "./GameSettingFields.svelte";

  let custom = false
  let overlay = false
  let loading = false
  let error = ""

  let changed: string[] = [];
  let symbols: Record<number, number> = {};
  let allItems: Record<string, Record<string, { name: string }>> = {}
  let submitting: string | undefined | null;  

  let existingUrl = "";
  SETTING.get().then(s => {
    existingUrl = s.filter(it => it.key === 'chusanMatchingServer')[0]?.value

    if (existingUrl && !CHU3_MATCHINGS.some(it => it.matching === existingUrl)) {
      custom = true
    }

    const symbolKey = "chusanSymbolChat"
    s.forEach(opt => {
      if (opt.key.substring(0, symbolKey.length) == symbolKey && opt.value)
        symbols[parseInt(opt.key.substring(symbolKey.length))] = opt.value;
    })
  })

  async function fetchSymbolData() {
    allItems = await DATA.allItems('chu3').catch(_ => {
      loading = false
      error = t("userbox.error.nodata")
    }) as typeof allItems
  }

  async function submitSymbol(id: number) {
    if (submitting) return false
    const field = `chusanSymbolChat${id + 1}`;
    submitting = field

    await SETTING.set(field, symbols[id + 1]).catch(e => error = e.message).finally(() => submitting = null);
    changed = changed.filter(v => v != `symbolChat${id}`)
    return true
  }

  // Click on "Custom" option"
  function clickCustom() {
    custom = true
    overlay = false
  }

  // Click on a matching option, set the reflector and matching server
  function clickOption(opt: ChusanMatchingOption) {
    Promise.all([
      SETTING.set('chusanMatchingReflector', opt.reflector),
      SETTING.set('chusanMatchingServer', opt.matching),
    ]).then(() => {
      overlay = false
      custom = false
      existingUrl = opt.matching
    }).catch(e => error = e.message)
  }
</script>

<StatusOverlays {error} {loading}/>

<div class="matching">
  <h2>{t("userbox.header.matching")}</h2>
  <p class="notice">{t("settings.cabNotice")}</p>

  <div class="matching-selector">
    <button on:click={_ => overlay = true}>{t('userbox.matching.select')}</button>
  </div>

  {#if custom}
    <GameSettingFields game="chu3-matching"/>
  {/if}

  <h2>{t("userbox.header.matching.symbolChat")}</h2>
  {#await fetchSymbolData() then}
    {#each {length: 4}, i}
      <div class="field">
        <label for={`symbolChat${i}`}>{ts(`userbox.matching.symbolChat`) + ` #${i + 1}`}</label>
        <div>
          <select bind:value={symbols[i + 1]} id={`symbolChat${i}`} on:change={() => {changed = [...changed, `symbolChat${i}`];}}>
            <option value={null}>{ts(`userbox.matching.symbolChat.default`)}</option>
            {#each Object.entries(allItems.symbolChat).filter((f) => parseInt(f[0]) !== 0) as [id, option]}
              <option value={parseInt(id)}>{option?.name || `(unknown ${id})`}</option>
            {/each}
          </select>
          {#if changed.includes(`symbolChat${i}`)}
            <button transition:slide={{axis: "x"}} disabled={!!submitting} on:click={() => submitSymbol(i)}>
              {t("settings.profile.save")}
            </button>
          {/if}
        </div>
      </div>
    {/each}
  {/await}
</div>

{#if overlay}
<div class="overlay" transition:fade>
  <div>
    <div>
      <h2>{t('userbox.header.matching')}</h2>
      <p>{t('userbox.matching.select.sub')}</p>
    </div>
    <div class="options">
      <!-- Selectable options -->
      {#each CHU3_MATCHINGS as option}
      <div class="clickable option" on:click={() => clickOption(option)}
        role="button" tabindex="0" on:keypress={e => e.key === 'Enter' && clickOption(option)}
        class:selected={!custom && existingUrl === option.matching}>

        <span class="name">{option.name}</span>
        <div class="links">
          <a href={option.ui} target="_blank" rel="noopener">{t('userbox.matching.option.ui')}</a> /
          <a href={option.guide} target="_blank" rel="noopener">{t('userbox.matching.option.guide')}</a>
        </div>

        <div class="divider"></div>

        <div class="coop">
          <span>{t('userbox.matching.option.collab')}</span>
          <div>
            {#each option.coop as coop}
              <span>{coop}</span>
            {/each}
          </div>
        </div>
      </div>
      {/each}

      <!-- Placeholder option for "Custom" -->
      <div class="clickable option" on:click={clickCustom}
        role="button" tabindex="0" on:keypress={e => e.key === 'Enter' && clickCustom()}
        class:selected={custom}>

        <span class="name">{t('userbox.matching.custom.name')}</span>
        <p class="notice custom">{t('userbox.matching.custom.sub')}</p>
      </div>
    </div>
  </div>
</div>
{/if}

<style lang="sass">
@use "../../vars"

.matching
  display: flex
  flex-direction: column
  gap: 12px

  h2
    margin-bottom: 0

p.notice
  opacity: 0.6
  margin: 0

  &.custom
    font-size: 0.9rem

.options
  display: flex
  flex-wrap: wrap
  gap: 1rem

.option
  flex: 1
  display: flex
  flex-direction: column
  align-items: center

  border-radius: vars.$border-radius
  background: vars.$ov-light
  padding: 1rem
  min-width: 150px

  &.selected
    border: 1px solid vars.$c-main

  .divider
    width: 100%
    height: 0.5px
    background: white
    opacity: 0.2
    margin: 0.8rem 0

  .name
    font-size: 1.1rem
    font-weight: bold

  .coop
    text-align: center

    div
      display: flex
      flex-direction: column
      font-size: 0.9rem
      opacity: 0.6

</style>

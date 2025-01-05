<!-- Svelte 4.2.11 -->

<script lang="ts">
  import {
    type AquaNetUser,
    type UserBox,
    type UserItem,
  } from "../../libs/generalTypes";
  import { DATA, USER, USERBOX } from "../../libs/sdk";
  import { t, ts } from "../../libs/i18n";
  import { DATA_HOST, FADE_IN, FADE_OUT, USERBOX_DEFAULT_URL } from "../../libs/config";
  import { fade, slide } from "svelte/transition";
  import StatusOverlays from "../StatusOverlays.svelte";
  import Icon from "@iconify/svelte";
  import GameSettingFields from "./GameSettingFields.svelte";
  import { filter } from "d3";
  import { coverNotFound } from "../../libs/ui";

  import { userboxFileProcess, ddsDB, initializeDb } from "../../libs/userbox/userbox"

  import ChuniPenguinComponent from "./userbox/ChuniPenguin.svelte"
  import ChuniUserplateComponent from "./userbox/ChuniUserplate.svelte";

  import useLocalStorage from "../../libs/hooks/useLocalStorage.svelte";
  import { DDS } from "../../libs/userbox/dds";
  import ChuniMatchingSettings from "./ChuniMatchingSettings.svelte";

  let user: AquaNetUser
  let [loading, error, submitting, preview] = [true, "", "", ""]
  let changed: string[] = [];

  // Available (unlocked) options for each kind of item
  // In allItems: 'namePlate', 'frame', 'trophy', 'mapIcon', 'systemVoice', 'avatarAccessory'
  let allItems: Record<string, Record<string, { name: string }>> = {}
  let iKinds = { namePlate: 1, frame: 2, trophy: 3, mapIcon: 8, systemVoice: 9, avatarAccessory: 11 }
  // In userbox: 'nameplateId', 'frameId', 'trophyId', 'mapIconId', 'voiceId', 'avatar{Wear/Head/Face/Skin/Item/Front/Back}'
  let userbox: UserBox
  let avatarKinds = ['Wear', 'Head', 'Face', 'Skin', 'Item', 'Front', 'Back'] as const
  // iKey should match allItems keys, and ubKey should match userbox keys
  let userItems: { iKey: string, ubKey: keyof UserBox, items: UserItem[] }[] = []

  // Submit changes
  function submit(field: keyof UserBox) {
    let obj = { field, value: userbox[field] }
    if (submitting) return
    submitting = obj.field

    USERBOX.setUserBox(obj)
      .then(() => changed = changed.filter((c) => c !== obj.field))
      .catch(e => error = e.message)
      .finally(() => submitting = "")
  }

  // Fetch data from the server
  async function fetchData() {
    const profile = await USERBOX.getProfile().catch(_ => {
      loading = false
      error = t("userbox.error.nodata")
    })
    if (!profile) return
    userbox = profile.user
    userItems = Object.entries(iKinds).flatMap(([iKey, iKind]) => {
      if (iKey != 'avatarAccessory') {
        let ubKey = `${iKey}Id`
        if (ubKey == 'namePlateId') ubKey = 'nameplateId'
        if (ubKey == 'systemVoiceId') ubKey = 'voiceId'
        return [{ iKey, ubKey: ubKey as keyof UserBox,
          items: profile.items.filter(x => x.itemKind === iKind)
        }]
      }

      return avatarKinds.map((aKind, i) => {
        let items = profile.items.filter(x => x.itemKind === iKind && Math.floor(x.itemId / 100000) % 10 === i + 1)
        return { iKey, ubKey: `avatar${aKind}` as keyof UserBox, items }
      })
    })

    allItems = await DATA.allItems('chu3').catch(_ => {
      loading = false
      error = t("userbox.error.nodata")
    }) as typeof allItems

    console.log("User Items", userItems)
    console.log("All items", allItems)
    console.log("Userbox", userbox)

    loading = false
  }

  USER.me().then(u => {
    if (!u) throw new Error(t("userbox.error.nodata"))
    user = u
    return fetchData()
  }).catch((e) => { loading = false; error = e.message });
  
  let DDSreader: DDS | undefined;

  let USERBOX_PROGRESS = 0;
  let USERBOX_SETUP_RUN = false;
  let USERBOX_SETUP_MODE = false;
  let USERBOX_SETUP_TEXT = t("userbox.new.setup");

  let USERBOX_ENABLED = useLocalStorage("userboxNew", false);
  let USERBOX_PROFILE_ENABLED = useLocalStorage("userboxNewProfile", false);
  let USERBOX_INSTALLED = false;
  let USERBOX_SUPPORT = "webkitGetAsEntry" in DataTransferItem.prototype;

  type OnlyNumberPropsOf<T extends Record<string, any>> = {[Prop in keyof T as (T[Prop] extends number ? Prop : never)]: T[Prop]}
  let userboxSelected: keyof OnlyNumberPropsOf<UserBox> = "avatarWear";
  const userboxNewOptions = ["systemVoice", "frame", "trophy", "mapIcon"]

  async function userboxSafeDrop(event: Event & { currentTarget: EventTarget & HTMLInputElement; }) {
    if (!event.target) return null;
    let input = event.target as HTMLInputElement;
    let folder = input.webkitEntries[0];
    error = await userboxFileProcess(folder, (progress: number, progressString: string) => {
      USERBOX_SETUP_TEXT = progressString;
      USERBOX_PROGRESS = progress;
    }) ?? "";
  }

  let USERBOX_URL_STATE = useLocalStorage("userboxURL", USERBOX_DEFAULT_URL);
  function userboxHandleInput(baseURL: string, isSetByServer: boolean = false) {
    if (baseURL != "")
      try {
        // validate url
        new URL(baseURL, location.href);
      } catch(err) {
        if (isSetByServer)
          return;
        return error = t("userbox.new.error.invalidUrl")
      }
    USERBOX_URL_STATE.value = baseURL;
    USERBOX_ENABLED.value = true;
    USERBOX_PROFILE_ENABLED.value = true;
    location.reload();
  }

  if (USERBOX_DEFAULT_URL && !USERBOX_URL_STATE.value)
    userboxHandleInput(USERBOX_DEFAULT_URL, true);

  indexedDB.databases().then(async (dbi) => {
    let databaseExists = dbi.some(db => db.name == "userboxChusanDDS");
    if (USERBOX_URL_STATE.value && databaseExists) {
      indexedDB.deleteDatabase("userboxChusanDDS")
    }
    if (databaseExists) {
      await initializeDb();
    }
    if (databaseExists || USERBOX_URL_STATE.value) {
      DDSreader = new DDS(ddsDB);
      USERBOX_INSTALLED = databaseExists || USERBOX_URL_STATE.value != "";
    } 
  })

</script>

<StatusOverlays {error} loading={loading || !!submitting} />
{#if !loading && !error}
<div out:fade={FADE_OUT} in:fade={FADE_IN}>
  <h2>{t("userbox.header.general")}</h2>
  <GameSettingFields game="chu3"/>
  <h2>{t("userbox.header.userbox")}</h2>
  {#if !USERBOX_ENABLED.value || !USERBOX_INSTALLED}
    <div class="fields">
      {#each userItems as { iKey, ubKey, items }, i}
        <div class="field">
          <label for={ubKey}>{ts(`userbox.${ubKey}`)}</label>
          <div>
            <select bind:value={userbox[ubKey]} id={ubKey} on:change={() => changed = [...changed, ubKey]}>
              {#each items as option}
                <option value={option.itemId}>{allItems[iKey][option.itemId]?.name || `(unknown ${option.itemId})`}</option>
              {/each}
            </select>
            {#if changed.includes(ubKey)}
              <button transition:slide={{axis: "x"}} on:click={() => submit(ubKey)} disabled={!!submitting}>
                {t("settings.profile.save")}
              </button>
            {/if}
          </div>
        </div>
      {/each}
    </div>
  {:else}
    <div class="chuni-userbox-container">
      <ChuniUserplateComponent chuniIsUserbox={true} on:click={() => userboxSelected = "nameplateId"} chuniCharacter={userbox.characterId} chuniLevel={userbox.level.toString()} chuniRating={userbox.playerRating / 100} 
        chuniNameplate={userbox.nameplateId} chuniName={userbox.userName} chuniTrophyName={allItems.trophy[userbox.trophyId].name}></ChuniUserplateComponent>
      <ChuniPenguinComponent chuniWear={userbox.avatarWear} chuniHead={userbox.avatarHead} chuniBack={userbox.avatarBack} 
        chuniFront={userbox.avatarFront} chuniFace={userbox.avatarFace} chuniItem={userbox.avatarItem} 
        chuniSkin={userbox.avatarSkin}></ChuniPenguinComponent>
    </div>
    <div class="chuni-userbox-row">
      {#each avatarKinds as avatarKind}
        {#await DDSreader?.getFile(`avatarAccessoryThumbnail:${userbox[`avatar${avatarKind}`].toString().padStart(8, "0")}`) then imageURL}
          <button on:click={() => userboxSelected = `avatar${avatarKind}`}>
            <img src={imageURL} class={userboxSelected == `avatar${avatarKind}` ? "focused" : ""} alt={allItems.avatarAccessory[userbox[`avatar${avatarKind}`]].name} title={allItems.avatarAccessory[userbox[`avatar${avatarKind}`]].name}>
          </button>
        {/await}
      {/each}
    </div>
    <div class="chuni-userbox">
      {#if userboxSelected == "nameplateId"}
        {#each userItems.find(f => f.ubKey == "nameplateId")?.items ?? [] as item}
          {#await DDSreader?.getFile(`nameplate:${item.itemId.toString().padStart(8, "0")}`) then imageURL}
            <button class="nameplate" on:click={() => {userbox[userboxSelected] = item.itemId; submit(userboxSelected)}}>
              <img src={imageURL} alt={allItems.namePlate[item.itemId].name} title={allItems.namePlate[item.itemId].name}>
            </button>
          {/await}
        {/each}
      {:else}
        {#each userItems.find(f => f.ubKey == userboxSelected)?.items ?? [] as item}
          {#await DDSreader?.getFile(`avatarAccessoryThumbnail:${item.itemId.toString().padStart(8, "0")}`) then imageURL}
            <button on:click={() => {userbox[userboxSelected] = item.itemId; submit(userboxSelected)}}>
              <img src={imageURL} alt={allItems.avatarAccessory[item.itemId].name} title={allItems.avatarAccessory[item.itemId].name}>
            </button>
          {/await}
        {/each}
      {/if}
    </div>
    <div class="fields">
      {#each userItems.filter(i => userboxNewOptions.includes(i.iKey)) as { iKey, ubKey, items }, i}
        <div class="field">
          <label for={ubKey}>{ts(`userbox.${ubKey}`)}</label>
          <div>
            <select bind:value={userbox[ubKey]} id={ubKey} on:change={() => changed = [...changed, ubKey]}>
              {#each items as option}
                <option value={option.itemId}>{allItems[iKey][option.itemId]?.name || `(unknown ${option.itemId})`}</option>
              {/each}
            </select>
            {#if changed.includes(ubKey)}
              <button transition:slide={{axis: "x"}} on:click={() => submit(ubKey)} disabled={!!submitting}>
                {t("settings.profile.save")}
              </button>
            {/if}
          </div>
        </div>
      {/each}
    </div>
  {/if}
  {#if USERBOX_INSTALLED}
    <!-- god this is a mess but idgaf atp -->
    <div class="field boolean" style:margin-top="1em">
      <input type="checkbox" bind:checked={USERBOX_ENABLED.value} id="newUserbox">
      <label for="newUserbox">
        <span class="name">{t("userbox.new.activate")}</span>
        <span class="desc">{t(`userbox.new.activate_desc`)}</span>
      </label>
    </div>
    <div class="field boolean" style:margin-top="1em">
      <input type="checkbox" bind:checked={USERBOX_PROFILE_ENABLED.value} id="newUserboxProfile">
      <label for="newUserboxProfile">
        <span class="name">{t("userbox.new.activate_profile")}</span>
        <span class="desc">{t(`userbox.new.activate_profile_desc`)}</span>
      </label>
    </div>
  {/if}
  {#if USERBOX_SUPPORT && !USERBOX_DEFAULT_URL}
    <p>
      <button on:click={() => USERBOX_SETUP_RUN = !USERBOX_SETUP_RUN}>{t(!USERBOX_INSTALLED ? `userbox.new.activate_first` : `userbox.new.activate_update`)}</button>
    </p>
  {/if} 
  <ChuniMatchingSettings/>
  <!--{#if !USERBOX_SUPPORT || !USERBOX_INSTALLED || !USERBOX_ENABLED.value}
    <h2>{t("userbox.header.preview")}</h2>
    <p class="notice">{t("userbox.preview.notice")}</p>
    <input bind:value={preview} placeholder={t("userbox.preview.url")}/>
    {#if preview}
      <div class="preview">
        {#each userItems.filter(v => v.iKey != 'trophy' && v.iKey != 'systemVoice') as { iKey, ubKey, items }, i}
          <div>
            <span>{ts(`userbox.${ubKey}`)}</span>
            <img src={`${preview}/${iKey}/${userbox[ubKey].toString().padStart(8, '0')}.png`} alt="" on:error={coverNotFound} />
          </div>
        {/each}
      </div>
    {/if}
  {/if}-->
</div>
{/if}
 
{#if USERBOX_SETUP_RUN && !error}
  <div class="overlay" transition:fade>
    <div>
      <h2>{t('userbox.new.name')}</h2>
      <span>{USERBOX_SETUP_MODE ? t('userbox.new.url_warning') : USERBOX_SETUP_TEXT}</span>
      <div class="actions">
        {#if USERBOX_SETUP_MODE}
          <input type="text" on:keyup={e => {if (e.key == "Enter") userboxHandleInput((e.target as HTMLInputElement).value)}} class="add-margin" placeholder="Base URL">
        {:else}
          {#if USERBOX_PROGRESS != 0}
            <div class="progress">
              <div class="progress-bar" style="width: {USERBOX_PROGRESS}%"></div>
            </div>
          {:else}
          <p class="notice add-margin">
            {t('userbox.new.setup.notice')}
          </p>
          <button class="drop-btn">
            <input type="file" on:input={userboxSafeDrop} on:click={e => e.preventDefault()}>
            {t('userbox.new.drop')}
          </button>
          {/if}
        {/if}
        {#if USERBOX_PROGRESS == 0}
          <button on:click={() => USERBOX_SETUP_RUN = false}>
            {t('back')}
          </button>
          <button on:click={() => USERBOX_SETUP_MODE = !USERBOX_SETUP_MODE}>
            {t(USERBOX_SETUP_MODE ? 'userbox.new.switch.to_drop' : 'userbox.new.switch.to_url')}
          </button>
        {/if}
      </div>
    </div>
  </div>
{/if}

<style lang="sass">
@use "../../vars"

input
  width: 100%


h2
  margin-bottom: 0.5rem

p.notice
  opacity: 0.6
  margin-top: 0

.progress 
  width: 100%
  height: 10px
  box-shadow: 0 0 1px 1px vars.$ov-lighter
  border-radius: 25px
  margin-bottom: 15px
  overflow: hidden

  .progress-bar
    background: #b3c6ff
    height: 100%
    border-radius: 25px


.add-margin, .drop-btn
  margin-bottom: 1em

.drop-btn
  position: relative
  width: 100%
  aspect-ratio: 3
  background: transparent
  box-shadow: 0 0 1px 1px vars.$ov-lighter

  > input
    position: absolute
    top: 0
    left: 0
    width: 100%
    height: 100%
    opacity: 0

.preview
  margin-top: 32px
  display: flex
  flex-wrap: wrap
  justify-content: space-between
  gap: 32px

  > div
    position: relative
    width: 100px
    height: 100px
    overflow: hidden
    background: vars.$ov-lighter
    border-radius: vars.$border-radius

    span
      position: absolute
      bottom: 0
      width: 100%
      text-align: center
      z-index: 10
      background: rgba(0, 0, 0, 0.2)
      backdrop-filter: blur(2px)

    img
      position: absolute
      inset: 0
      width: 100%
      height: 100%
      object-fit: contain

.fields
  display: flex
  flex-direction: column
  gap: 12px
  width: 100%
  flex-grow: 0

  label
    display: flex
    flex-direction: column

  select
    width: 100%

.field
  display: flex
  flex-direction: column
  width: 100%

  label
    max-width: max-content

  > div:not(.bool)
    display: flex
    align-items: center
    gap: 1rem
    margin-top: 0.5rem

    > select
      flex: 1


.field.boolean
  display: flex
  flex-direction: row
  align-items: center
  gap: 1rem
  width: auto

  input
    width: auto
    aspect-ratio: 1 / 1

  label
    display: flex
    flex-direction: column
    max-width: max-content

    .desc
      opacity: 0.6

/* AquaBox */

.chuni-userbox-row
  width: 100%
  display: flex

  button
    padding: 0
    margin: 0
    width: 100%
    flex: 0 1 100%
    background: none
    aspect-ratio: 1

    img
      width: 100%
      filter: brightness(50%)

      &.focused
        filter: brightness(75%)

.chuni-userbox 
  width: calc(100% - 20px)
  height: 350px
  
  display: flex
  flex-direction: row
  flex-wrap: wrap
  padding: 10px
  background: vars.$c-bg
  border-radius: 16px
  overflow-y: auto
  margin-bottom: 15px
  justify-content: center

  button
    padding: 0
    margin: 0
    width: 20%
    align-self: flex-start
    background: none
    aspect-ratio: 1

    img
      width: 100%

    &.nameplate
      width: 50%
      aspect-ratio: unset
      border: none

.chuni-userbox-container
  display: flex
  align-items: center
  justify-content: center

@media (max-width: 1000px)
  .chuni-userbox-container
    flex-wrap: wrap
</style>

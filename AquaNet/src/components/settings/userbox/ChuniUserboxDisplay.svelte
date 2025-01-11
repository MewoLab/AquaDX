<script lang="ts">
  import { initializeDb } from "../../../libs/userbox/userbox"
  import ChuniPenguinComponent from "./ChuniPenguin.svelte"
  import ChuniUserplateComponent from "./ChuniUserplate.svelte"
  import { type UserBox } from "../../../libs/generalTypes"
  import { DATA, USERBOX } from "../../../libs/sdk"
  import useLocalStorage from "../../../libs/hooks/useLocalStorage.svelte"
  import { t } from "../../../libs/i18n"

  /**
   * This is a UserBox viewer on the Profile page (UserHome), added by raymond
   * to view other user's penguins on their profile.
   */

  export let game: string
  export let username: string
  export let error: string = ""

  let USERBOX_ACTIVE = useLocalStorage("userboxNewProfile", false)
  let USERBOX_INSTALLED = false

  let userbox: UserBox
  let allItems: Record<string, Record<string, { name: string }>> = {}

  if (game == "chu3" && USERBOX_ACTIVE.value) {
    indexedDB.databases().then(async (dbi) => {
      let databaseExists = dbi.some(db => db.name == "userboxChusanDDS")
      if (databaseExists) {
        await initializeDb()
        const profile = await USERBOX.getUserProfile(username).catch(_ => null)
        if (!profile) return
        userbox = profile
        console.log(userbox)

        allItems = await DATA.allItems('chu3').catch(_ => {
          error = t("userbox.error.nodata")
        }) as typeof allItems
        USERBOX_INSTALLED = databaseExists
      }
    })
  }
</script>

{#if USERBOX_ACTIVE.value && USERBOX_INSTALLED && game == "chu3"}
  <div class="chuni-userbox-container">
    <ChuniUserplateComponent chuniCharacter={userbox.characterId} chuniRating={userbox.playerRating / 100} chuniLevel={userbox.level.toString()}
      chuniNameplate={userbox.nameplateId} chuniName={userbox.userName} chuniTrophyName={allItems.trophy[userbox.trophyId].name}></ChuniUserplateComponent>
    <div class="chuni-penguin-container">
      <ChuniPenguinComponent classPassthrough="chuni-penguin-float" chuniWear={userbox.avatarWear} chuniHead={userbox.avatarHead} chuniBack={userbox.avatarBack}
      chuniFront={userbox.avatarFront} chuniFace={userbox.avatarFace} chuniItem={userbox.avatarItem}
      chuniSkin={userbox.avatarSkin}></ChuniPenguinComponent>
    </div>
  </div>
{/if}

<style lang="sass">
.chuni-userbox-container
  display: flex
  align-items: center
  justify-content: center

  .chuni-penguin-container
    height: 256px
    aspect-ratio: 1
    position: relative

@media (max-width: 1000px)
  .chuni-userbox-container
    flex-wrap: wrap
</style>

<script lang="ts">
  import {GAME} from "../libs/sdk";
  import {AQUA_HOST} from "../libs/config";
  import Loading from "../components/ui/Loading.svelte";
  import Error from "../components/ui/Error.svelte";
  import { t } from "../libs/i18n";

  const token = localStorage.getItem("token")
</script>

<main class="content">
  <div class="outer-title-options">
    <h2>{t("maiphoto.title")}</h2>
  </div>

  {#await GAME.photos()}
    <Loading/>
  {:then photos}
    {#if photos.length === 0}
      <blockquote>{t('maiphoto.none')}</blockquote>
    {:else}
      <blockquote>{t('maiphoto.url_warning')}</blockquote>
    {/if}
    <div class="pictures">
      {#each photos as photo}
        <div class="photo-container">
          <img class="rounded-2xl" src="{AQUA_HOST}/api/v2/game/mai2/my-photo/{photo}?token={token}" alt="Memorial" />
        </div>
      {/each}
    </div>
  {:catch error}
    <Error {error}/>
  {/await}
</main>

<style lang="sass">
  @use "../vars"

  .pictures
    display: flex
    flex-wrap: wrap
    justify-content: center
    row-gap: 1rem
    gap: 1rem

  .photo-container
    flex: 1 1 300px
    min-width: 280px
    max-width: 100%
    display: flex
    justify-content: center

  .photo-container img
    width: 100%
    height: auto
    object-fit: contain
</style>

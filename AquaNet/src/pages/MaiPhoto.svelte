<script lang="ts">
  import {GAME} from "../libs/sdk";
  import {AQUA_HOST} from "../libs/config";

  let photos: string[] = []
  GAME.photos().then(result => {
    console.log(result)
    photos = result
  }).catch(e => {
    console.error(e)
  })

  const token = localStorage.getItem("token")
</script>

<main>
  <div class="content-main">
    <p>Here you can see the pictures you have uploaded from mai.</p>
  <div class="pictures">
  {#each photos as photo}
    <img class="rounded-2xl max-w-sm" src="{AQUA_HOST}/api/v2/game/mai2/my-photo/{photo}?token={token}" alt="Memorial" />
  {/each}
  {#if photos.length === 0}
    <p>There are no photos available. Try uploading some!</p>
  {/if}
  </div>
  </div>
</main>
<style lang="sass">
  @use "../vars"

  .pictures
    display: flex
    flex-wrap: wrap
    justify-content: center
    row-gap: 1rem
    gap: 1rem
</style>

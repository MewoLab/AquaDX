<!-- Svelte 4.2.11 -->

<script lang="ts">
  import RatingCompSong from "./RatingCompSong.svelte";
  import { parseComposition, type GameName } from "../libs/scoring";
  import { type MusicMeta } from "../libs/generalTypes";

  export let title: string;
  export let comp: string | undefined;
  export let allMusics: Record<string, MusicMeta>;
  export let game: GameName;
  export let top: number | undefined = undefined;

  let split = comp?.split(",")?.filter(it => it.split(":")[0] !== '0')
    ?.map(it => parseComposition(it, allMusics, game))

  if (top) split = split?.toSorted((a, b) => b.score - a.score).slice(0, top)
  if (split) console.log("Split", split)

  let mustResize = split && split.length > 12
</script>

{#if split && split.length > 0 && comp}
  <div class="rating-composition-container" class:must-resize={mustResize}>
    {#if mustResize}
      <input type="checkbox">
    {/if}

    <h2>{title}</h2>
    <div class="rating-composition">
      {#each split as p}
        <div>
          <RatingCompSong {p} {game}/>
        </div>
      {/each}
    </div>
  </div>
{/if}

<style lang="sass">
  @use "../vars"

  .rating-composition
    display: grid
    // 3 columns
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr))
    gap: vars.$gap

    overflow: hidden
    transition: max-height 250ms

  .rating-composition-container
    position: relative

    input
      width: 100%
      height: 100%
      position: absolute
      z-index: 10

      border: none
      border-radius: 0
      opacity: 0

      transition: opacity 250ms

  .rating-composition-container.must-resize
    input
      & ~ .rating-composition
        max-height: 250px

      & ~ h2::after
        content: "▾"
        margin-left: 0.5em
      &:checked ~ h2::after
        content: "▴"

      &:checked
        opacity: 0
      &:checked ~ .rating-composition
        max-height: 3000px

      opacity: 1
      background: linear-gradient(#0000 250px, rgb(28.35, 28.35, 28.35) 100%) /* for some reason the color.adjust doesn't work here so whatever */
        
    
</style>

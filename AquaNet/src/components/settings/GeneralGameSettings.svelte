<script>
  import { fade } from "svelte/transition";
  import { FADE_IN, FADE_OUT } from "../../libs/config";
  import { t, ts } from "../../libs/i18n";
  import useLocalStorage from "../../libs/hooks/useLocalStorage.svelte";
  import RegionSelector from "./RegionSelector.svelte";

  const rounding = useLocalStorage("rounding", true);
</script>

<div out:fade={FADE_OUT} in:fade={FADE_IN} class="fields">
  <blockquote>
    {ts("settings.gameNotice")}
  </blockquote>
  <div class="field">
    <div class="bool">
      <input id="rounding" type="checkbox" bind:checked={rounding.value}/>
      <label for="rounding">
        <span class="name">{ts(`settings.fields.rounding.name`)}</span>
        <span class="desc">{ts(`settings.fields.rounding.desc`)}</span>
      </label>
    </div>
  </div>
  <div class="divider"></div>
  <blockquote>
    {ts("settings.regionNotice")}
  </blockquote>
  <RegionSelector/>
</div>

<style lang="sass">
  @use "../../vars"

  .fields
    display: flex
    flex-direction: column
    gap: 12px

  .bool
    display: flex
    align-items: center
    gap: 1rem

    label
      display: flex
      flex-direction: column

      .desc
        opacity: 0.6

  .divider
    width: 100%
    height: 0.5px
    background: white
    opacity: 0.2
    margin: 0.4rem 0
</style>

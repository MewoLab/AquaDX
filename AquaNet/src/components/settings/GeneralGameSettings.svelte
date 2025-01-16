<script>
  import { fade } from "svelte/transition";
  import { FADE_IN, FADE_OUT } from "../../libs/config";
  import GameSettingFields from "./GameSettingFields.svelte";
  import { ts } from "../../libs/i18n";
  import useLocalStorage from "../../libs/hooks/useLocalStorage.svelte";

  const rounding = useLocalStorage("rounding", true);
</script>

<div out:fade={FADE_OUT} in:fade={FADE_IN} class="fields">
  <p class="warning">
    These settings affect Mai and Wacca.
  </p>
  <GameSettingFields game="general"/>
  <div class="field">
    <div class="bool">
      <input id="rounding" type="checkbox" bind:checked={rounding.value}/>
      <label for="rounding">
        <span class="name">{ts(`settings.fields.rounding.name`)}</span>
        <span class="desc">{ts(`settings.fields.rounding.desc`)}</span>
      </label>
    </div>
  </div>
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

  .field
    display: flex
    flex-direction: column

    label
      max-width: max-content

    > div:not(.bool)
      display: flex
      align-items: center
      gap: 1rem
      margin-top: 0.5rem

      > input
        flex: 1

  .warning
    background: #aa555510
    padding: 10px
    border-left: solid 2px vars.$c-error

    &::before
      color: vars.$c-error
      font-weight: bold
      content: "！"
</style>

<script lang="ts">
  import { slide } from "svelte/transition";
  import { ts } from "../../libs/i18n";
  import InputWithButton from "./InputWithButton.svelte";

  export let field: {key: string, value: any, type: string, changed?: boolean};
  export let callback: () => Promise<boolean>;
</script>

<div class="field {field.type.toLowerCase()}">
  {#if field.type.toLowerCase() === "boolean"}
    <input id={field.key} type="checkbox" bind:checked={field.value} on:change={callback}/>
    <label for={field.key}>
      <span class="name">{ts(`settings.fields.${field.key}.name`)}</span>
      <span class="desc">{ts(`settings.fields.${field.key}.desc`)}</span>
    </label>
  {/if}
  {#if field.type.toLowerCase() === "string"}
    <label for={field.key}>
      <span class="name">{ts(`settings.fields.${field.key}.name`)}</span>
      <span class="desc">{ts(`settings.fields.${field.key}.desc`)}</span>
    </label>
    <InputWithButton bind:field={field} callback={callback}/>
  {/if}
</div>

<style lang="sass">
  .field.string
    flex-direction: column
    align-items: flex-start
    gap: 0.5rem

  .field.boolean
    align-items: center
    gap: 1rem

  .field
    display: flex

    label
      display: flex
      flex-direction: column
      max-width: max-content

      .desc
        opacity: 0.6
</style>

<script lang="ts">
  import { slide, fade } from "svelte/transition";
  import { FADE_IN, FADE_OUT } from "../../libs/config";
  import { t } from "../../libs/i18n.js";
  import Icon from "@iconify/svelte";
  import StatusOverlays from "../StatusOverlays.svelte";
  import { GAME } from "../../libs/sdk";
  import GameSettingFields from "./GameSettingFields.svelte";

  const profileFields = [
    ['name', t('settings.mai2.name')],
  ]

  export let username: string;
  let error: string
  let submitting = ""
  let values = Array(profileFields.length).fill('')
  let changed: string[] = []

  GAME.userSummary(username, 'mai2').then(({name}) => {
    values = [name]
  }).catch(e => error = e.message)

  function submit(field: string, value: string) {
    if (submitting) return
    submitting = field

    switch (field) {
      case 'name':
        GAME.changeName('mai2', value).then(({newName}) => {
          changed = changed.filter(c => c !== field)
          values = [newName]
        }).catch(e => error = e.message).finally(() => submitting = "")
        break
    }
  }

  function exportData() {
    submitting = "export"
    GAME.export('mai2')
      .then(data => download(JSON.stringify(data), `AquaDX_maimai2_export_${values[0]}.json`))
      .catch(e => error = e.message)
      .finally(() => submitting = "")
  }

  function download(data: string, filename: string) {
    const blob = new Blob([data]);
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
  }
</script>

<div class="fields" out:fade={FADE_OUT} in:fade={FADE_IN}>
  {#each profileFields as [field, name], i (field)}
    <div class="field">
      <label for={field}>{name}</label>
      <div>
        <input id={field} type="text"
               bind:value={values[i]} on:input={() => changed = [...changed, field]}
               placeholder={field === 'password' ? t('settings.profile.unchanged') : t('settings.profile.unset')}/>
        {#if changed.includes(field) && values[i]}
          <button transition:slide={{axis: 'x'}} on:click={() => submit(field, values[i])}>
            {#if submitting === field}
              <Icon icon="line-md:loading-twotone-loop"/>
            {:else}
              {t('settings.profile.save')}
            {/if}
          </button>
        {/if}
      </div>
    </div>
  {/each}
  <GameSettingFields game="mai2"/>
  <button class="exportButton" on:click={exportData}>
    <Icon icon="bxs:file-export"/>
    {t('settings.export')}
  </button>
</div>

<StatusOverlays {error} loading={!values[0] || !!submitting}/>

<style lang="sass">
  .fields
    display: flex
    flex-direction: column
    gap: 12px

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

  .exportButton
    display: flex
    justify-content: center
    align-items: center
    gap: 5px
</style>

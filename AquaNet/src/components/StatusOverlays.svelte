<!-- Svelte 4.2.11 -->

<script lang="ts">
  import { fade } from 'svelte/transition'
  import type { ConfirmProps } from "../libs/generalTypes";
  import { t } from "../libs/i18n"
  import Loading from './ui/Loading.svelte';
  import Error from './ui/Error.svelte';

  // Props
  export let confirm: ConfirmProps | null = null
  export let error: string | null = null
  export let loading: boolean = false

  function doConfirm(fn?: () => void) {
    confirm = null
    fn && fn()
  }
</script>

{#if confirm}
  <div class="overlay" transition:fade>
    <div>
      <h2>{confirm.title}</h2>
      <span>{confirm.message}</span>

      <div class="actions">
        {#if confirm.cancel}
          <button on:click={() => doConfirm(confirm?.cancel)}>{t('action.cancel')}</button>
        {/if}
        <button on:click={() => doConfirm(confirm?.confirm)} class:error={confirm.dangerous}>{t('action.confirm')}</button>
      </div>
    </div>
  </div>
{/if}

{#if error}
  <Error {error}/>
{/if}

{#if loading && !error}
  <Loading/>
{/if}

<style lang="sass">
  .actions
    display: flex
    gap: 16px

    button
      width: 100%
</style>

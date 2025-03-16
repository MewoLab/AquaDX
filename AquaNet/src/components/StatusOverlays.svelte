<!-- Svelte 4.2.11 -->

<script lang="ts">
  import { fade } from 'svelte/transition'
  import type { ConfirmProps } from "../libs/generalTypes";
  import { DISCORD_INVITE } from "../libs/config";
  import { t } from "../libs/i18n"
  import Loading from './ui/Loading.svelte';
  import Error from './ui/Error.svelte';

  // Props
  export let confirm: ConfirmProps | null = null
  export let error: string | null
  export let loading: boolean = false
</script>

{#if confirm}
  <div class="overlay" transition:fade>
    <div>
      <h2>{confirm.title}</h2>
      <span>{confirm.message}</span>

      <div class="actions">
        {#if confirm.cancel}
          <!-- Svelte LSP is very annoying here -->
          <button on:click={() => {
            confirm && confirm.cancel && confirm.cancel()

            // Set to null
            confirm = null
          }}>{t('action.cancel')}</button>
        {/if}
        <button on:click={() => confirm && confirm.confirm()} class:error={confirm.dangerous}>{t('action.confirm')}</button>
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

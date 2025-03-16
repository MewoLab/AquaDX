<script lang="ts">
  import { fade } from "svelte/transition";
  import { t } from "../../libs/i18n";
  import { DISCORD_INVITE } from "../../libs/config";

  export let error: string;
</script>

<div class="overlay" transition:fade>
  <div>
    <h2 class="error">{t('status.error')}</h2>
    <span>{t('status.error.hint')}<a href={DISCORD_INVITE}>{t('status.error.hint.link')}</a></span>
    <span>{t('status.detail', { detail: error })}</span>

    <div class="actions">
      <button on:click={() => location.reload()} class="error">
        {t('action.refresh')}
      </button>
    </div>
  </div>
</div>

<style lang="sass">
  .actions
    display: flex
    gap: 16px

    button
      width: 100%
</style>

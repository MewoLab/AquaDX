<!-- Svelte 4.2.11 -->

<script lang="ts">
  import { fade, slide } from "svelte/transition";
  import { USER } from "../../libs/sdk";
  import type { AquaNetUser } from "../../libs/generalTypes";
  import { codeToHtml } from 'shiki'
  import { AQUA_CONNECTION, DISCORD_INVITE, FADE_IN, FADE_OUT } from "../../libs/config";
  import { t } from "../../libs/i18n";
  import DashboardTabs from "../../components/DashboardTabs.svelte";
  import { patchUserSegatools } from "../../libs/setup";

  let user: AquaNetUser
  let keychips: string[] = [];
  let selectedKeychip: string = "";
  let keychipCode: string;

  let exposeKeychip = false;
  let automaticSetupStatus: "none" | "success" | "failure" = "none";
  let isLoading = true;
  let isAdding = false;
  let newKeychip = "";
  let addKeychipError = "";

  function formatKeychipDisplay(k: string): string {
    return `${k.slice(0, 4)}-${k.slice(4)}`;
  }

  function buildManualKeychipLines(): string {
    return `id=${formatKeychipDisplay(selectedKeychip)}`;
  }

  async function buildKeychipCode() {
    exposeKeychip = false;
    const keychipLines = buildManualKeychipLines();
    keychipCode = await codeToHtml(`
[dns]
default=${AQUA_CONNECTION}

[keychip]
enable=1
${keychipLines}`.trim(), {
      lang: 'ini',
      theme: 'rose-pine',
      transformers: []
    });
  }

  async function loadKeychips() {
    isLoading = true;
    keychips = await USER.keychips();
    if (keychips.length > 0) {
      selectedKeychip = keychips[0];
      await buildKeychipCode();
    } else {
      selectedKeychip = "";
      await buildKeychipCode();
    }
    isLoading = false;
  }

  USER.me().then((u) => {
    user = u;
    loadKeychips();
  });

  async function selectKeychip(k: string) {
    selectedKeychip = k;
    await buildKeychipCode();
  }

  async function addKeychip() {
    const rawKeychipId = newKeychip.trim().toUpperCase();
    const validRawFormat = /^A\d{14}$/.test(rawKeychipId) || /^A\d{3}-\d{11}$/.test(rawKeychipId);
    if (!validRawFormat) {
      addKeychipError = "Invalid keychip format; use A12345678901234 or A123-12345678901.";
      return;
    }

    const keychipId = rawKeychipId.replace("-", "");

    addKeychipError = "";
    isAdding = true;
    try {
      const newId = await USER.addKeychip(keychipId);
      keychips = [...keychips, newId];
      selectedKeychip = newId;
      newKeychip = "";
      await buildKeychipCode();
    } catch (error) {
      addKeychipError = error instanceof Error ? error.message : "Failed to add keychip.";
    } finally {
      isAdding = false;
    }
  }

  async function deleteKeychip(k: string) {
    await USER.deleteKeychip(k);
    keychips = keychips.filter(id => id !== k);
    if (selectedKeychip === k) {
      selectedKeychip = keychips[0] ?? "";
      await buildKeychipCode();
    }
  }

  async function patchSegatools() {
    automaticSetupStatus = await patchUserSegatools({ keychip: formatKeychipDisplay(selectedKeychip), dns: AQUA_CONNECTION }) ? "success" : "failure";
  }
</script>

<main class="content">
  <DashboardTabs />
  <div class="setup-instructions">
    <h2>{t('home.setup')}</h2>

    {#if isLoading}
      <p>{t('loading')}</p>
    {:else}
      <div class="setup-step">
        1. <div>{@html t('setup.steps.one')}</div>
      </div>

      <blockquote class="info">
        {t('setup.keychip-warning')}
      </blockquote>

      {#if user.canModifyKeychips}
        <details>
          <summary>{t('setup.keychip')}</summary>
          <p>
            {t('setup.keychip.warning')}
          </p>
          <div class="keychip-list">
            {#each keychips as k}
              <div class="keychip-item" class:selected={k === selectedKeychip}>
                <button class="keychip-select" on:click={() => selectKeychip(k)}>
                  {formatKeychipDisplay(k)}
                </button>
                <button class="keychip-delete danger" on:click={() => deleteKeychip(k)}>
                  {t('setup.keychip.delete')}
                </button>
              </div>
            {/each}

            <form class="add-keychip-form" on:submit|preventDefault={addKeychip}>
              <input
                type="text"
                placeholder={t('setup.keychip.placeholder')}
                maxlength="16"
                bind:value={newKeychip}
                required
              />
              <button class="add-keychip" type="submit" disabled={isAdding}>
                {isAdding ? t('loading') : t('setup.keychip.add')}
              </button>
            </form>
            {#if addKeychipError}
              <p class="danger">{addKeychipError}</p>
            {/if}
          </div>
        </details>
        <div class="divider"></div>
      {/if}

      {#if selectedKeychip}
        {#if !!window.showOpenFilePicker}
          <details>
            <summary>{t('setup.type.automatic')}</summary>
            {@html t('setup.automatic')}
            {#if automaticSetupStatus != "none"}
              <blockquote class={`keychip-status ${automaticSetupStatus}`}>
                {t(`setup.automatic.${automaticSetupStatus}`)}
              </blockquote>
            {/if}
            <div class="setup-btn">
              <button on:click={patchSegatools}>{t('setup.automatic.select')}</button>
            </div>
          </details>
        {/if}

        <details>
          <summary>{t('setup.type.manual')}</summary>
          {@html t('setup.manual')}
          <div class="code-container">
            <div class="code" class:revealed={exposeKeychip}>
              {@html keychipCode}
            </div>
            {#if !exposeKeychip}
              <button class="reveal-btn" on:click={() => exposeKeychip = true}>
                {t('setup.reveal-keychip')}
              </button>
            {/if}
          </div>
        </details>
        <br>
      {/if}

      <div class="setup-step">
        2. <div>{@html t('setup.steps.two')}</div>
      </div>
      <div class="setup-step">
        3. <div>{@html t('setup.steps.three')}</div>
      </div>
      <h2>{t('setup.troubleshooting.header')}</h2>
      <ul>
        <li>
          {@html t('setup.troubleshooting.items.one')}
        </li>
        <li>
          {@html t('setup.troubleshooting.items.two')}
        </li>
        <li>
          {@html t('setup.troubleshooting.items.three')}
        </li>
      </ul>
      <p>
        {@html t('setup.support-info')}
      </p>
    {/if}
  </div>
</main>

<style lang="sass">
  @use "../../vars"
  .code
    overflow-x: auto

  ul
    li
      margin: 0.75em 0
  .divider
    width: 90%
    height: 1px

    background: #fff3

    margin: 1em 0
    
    position: relative
    left: 50%
    transform: translate(-50%, 0)

  :global(pre.shiki)
    background-color: transparent !important

    :global(code)
      counter-reset: step
      counter-increment: step 0

    :global(code .line::before)
      content: counter(step)
      counter-increment: step
      width: 1rem
      margin-right: 1.5rem
      display: inline-block
      text-align: right
      color: rgba(115,138,148,.4)

  .setup-step
    display: flex
    div
      margin-left: 1em
  
  .setup-btn
    margin: 0.5em

  details
    summary
        cursor: pointer
        font-weight: bold
        padding: 0.25em 0

    &:open
        summary
            margin: 0 0 1em 0

  .code-container
    padding: 10px
    position: relative
    margin: 1em
    overflow: hidden
    background: vars.$c-shadow

    .code
      filter: blur(4px)
      transition: 250ms filter
      &.revealed
        filter: none
      :global(.copy)
        position: absolute
        right: 2em
        top: 2em

    .reveal-btn
      position: absolute
      top: 50%
      left: 50%
      transform: translate(-50%, -50%)

  .keychip-list
    display: flex
    flex-direction: column
    gap: 0.5em
    margin: 1em 0

  .keychip-item
    display: flex
    align-items: center
    gap: 0.5em
    padding: 0.25em 0.5em
    border-radius: 4px
    &.selected
      background: vars.$c-shadow

  .keychip-select
    font-family: monospace
    flex: 1
    text-align: left

  .add-keychip
    align-self: flex-start
    margin-top: 0.25em

  .add-keychip-form
    display: flex
    flex-wrap: wrap
    gap: 0.5em
    align-items: center
    margin-top: 0.25em
    
    input
      flex: 1 1 16rem
      max-width: 16rem

    button
      flex: 1 1 10rem
      max-width: 8rem

  .danger
    color: vars.$c-error
    
</style>


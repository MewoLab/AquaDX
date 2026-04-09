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
  let keychip: string;
  let keychipCode: string;

  let exposeKeychip = false;
  let automaticSetupStatus: "none" | "success" | "failure" = "none";

  USER.me().then((u) => {
    user = u;
    USER.keychip().then(k => {
      keychip = `${k.slice(0, 4)}-${k.slice(4)}1337`;
      codeToHtml(`
[dns]
default=${AQUA_CONNECTION}

[keychip]
enable=1
id=${keychip}`.trim(), {
        lang: 'ini',
        theme: 'rose-pine',
        transformers: []
      }).then((html) => {
        keychipCode = html;
      });
    });
  });

  async function patchSegatools() {
    automaticSetupStatus = await patchUserSegatools({ keychip, dns: AQUA_CONNECTION }) ? "success" : "failure";
  }
</script>

<main class="content">
  <DashboardTabs />
  <div class="setup-instructions">
    <h2>{t('home.setup')}</h2>

    {#if keychip}
      <div class="setup-step">
        1. <div>{@html t('setup.steps.one')}</div>
      </div>
      
      <blockquote class="info">
        {t('setup.keychip-warning')}
      </blockquote>
      
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
    {:else}
      <p>{t('loading')}</p>
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
      
</style>

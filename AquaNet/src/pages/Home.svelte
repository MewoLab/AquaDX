<script lang="ts">
  import { USER } from "../libs/sdk.js";
  import type { AquaNetUser } from "../libs/generalTypes";
  import StatusOverlays from "../components/StatusOverlays.svelte";
  import ActionCard from "../components/ActionCard.svelte";
  import { t } from "../libs/i18n";
  import ImportDataAction from "./Home/ImportDataAction.svelte";
  import DashboardTabs from "../components/DashboardTabs.svelte";
  import * as acUrl from "../libs/acUrl";

  USER.ensureLoggedIn();

  let me: AquaNetUser;
  let error = "";

  USER.me().then((m) => me = m).catch(e => error = e.message)

  if (acUrl.has()) {
    location.href = "/cards"
  }
</script>

<main class="content">
  <DashboardTabs />
  {#if me}
    <div class="action-cards">
      <div class="quick-action-cards">
        <ActionCard isSmall={true} color="201, 135, 174" icon="fluent:games-16-filled" href={`/u/${me.username}`}>
          <h3>{t('home.user-profile')}</h3>
        </ActionCard>
        <ActionCard isSmall={true} color="136, 99, 150" icon="fluent:text-bullet-list-square-16-filled" href={`/ranking`}>
          <h3>{t('home.rankings')}</h3>
        </ActionCard>
        <ActionCard isSmall={true} color="133, 199, 201" icon="fluent:settings-16-filled" href={`/settings`}>
          <h3>{t('home.settings')}</h3>
        </ActionCard>
      </div>

      <div class="separator"></div>

      <ActionCard color="255, 192, 203" icon="solar:card-bold-duotone" href="/cards">
        {#if me && me.cards.length > 1}
          <h3>{t('home.manage-cards')}</h3>
          <span>{t('home.manage-cards-description')}</span>
        {:else if me}
          <h3>{t('home.link-card')}</h3>
          <span>{t('home.link-cards-description')}</span>
        {/if}
      </ActionCard>

      <ImportDataAction/>

      <ActionCard icon="uil:link-alt" href="/setup">
        <h3>{t('home.setup')}</h3>
        <span>{t('home.setup-description')}</span>
      </ActionCard>

      <ActionCard color="82, 93, 233" icon="fluent:chat-12-filled" href="/support">
        <h3>{t('home.join-community')}</h3>
        <span>{t('home.join-community-description')}</span>
      </ActionCard>
    </div>
  {/if}
</main>

<StatusOverlays {error} loading={!me}/>

<style lang="sass">
  @use "../vars"


  h3
    font-size: 1.3rem
    margin: 0

  .action-cards
    display: flex
    flex-direction: column
    gap: 1rem

  .quick-action-cards
    display: flex
    flex-direction: row
    gap: 1rem

    :global(.action-card)
      flex: 1
      height: 2rem
      display: flex
      align-content: center
      flex-wrap: wrap

  .separator 
    position: relative
    left: 50%
    transform: translate(-50%, 0)
    width: 75%
    height: 1px
    background: vars.$ov-light
</style>

<script lang="ts">
  import { onMount } from "svelte";
  import { title } from "../libs/ui";
  import { GAME } from "../libs/sdk";
  import type { GenericRanking } from "../libs/generalTypes";
  import StatusOverlays from "../components/StatusOverlays.svelte";
  import type { GameName } from "../libs/scoring";
  import { GAME_TITLE } from "../libs/i18n";
  import { t } from "../libs/i18n";
  import UserCard from "../components/UserCard.svelte";
  import Tooltip from "../components/Tooltip.svelte";
  import Pagination from "../components/Pagination.svelte";

  export let game: GameName = 'mai2';

  title(`Ranking`);

  let d: { users: GenericRanking[] };
  let error: string | null;

  let page = 1
  const perPage = 50
  let totalPages = 1

  function handleUpdatePage(event: CustomEvent<number>) {
    page = event.detail;
    const url = new URL(window.location.toString())
    url.searchParams.set('page', page.toString())
    history.pushState({}, '', url.toString())
    window.scrollTo(0, 0)
  }

  onMount(() => {
    const url = new URL(window.location.toString())
    const pageParam = url.searchParams.get('page')
    if (pageParam) {
      page = parseInt(pageParam, 10) || 1
    }

    window.addEventListener('popstate', () => {
      const url = new URL(window.location.toString())
      const pageParam = url.searchParams.get('page')
      page = parseInt(pageParam, 10) || 1
      window.scrollTo(0, 0)
    })
  })

  Promise.all([GAME.ranking(game)])
    .then(([users]) => {
      d = { users }
      totalPages = Math.ceil(users.length / perPage)
    })
    .catch((e) => error = e.message);

  let hoveringUser = "";
  let hoverLoading = false;

  $: paginatedUsers = d ? d.users.slice((page - 1) * perPage, page * perPage) : []
</script>

<main class="content leaderboard">
  <div class="outer-title-options">
    <h2>{t("Leaderboard.Title")}</h2>
    <nav>
      {#each Object.entries(GAME_TITLE) as [k, v]}
        <a href="/ranking/{k}" class:active={k === game}>{v}</a>
      {/each}
    </nav>
  </div>

  {#if d}
    {#if page > 1}
      <Pagination {page} {totalPages} on:updatePage={handleUpdatePage} />
    {/if}

    <div class="leaderboard-container">
      <div class="lb-user" on:mouseenter={() => hoveringUser = paginatedUsers[0]?.username} role="heading" aria-level="2">
        <span class="rank">{t("Leaderboard.Rank")}</span>
        <span class="name"></span>
        <span class="rating">{t("Leaderboard.Rating")}</span>
        <span class="accuracy">{t("Leaderboard.Accuracy")}</span>
        <span class="fc">{t("Leaderboard.FC")}</span>
        <span class="ap">{t("Leaderboard.AP")}</span>
      </div>
      {#each paginatedUsers as user, i (user.rank)}
        <div class="lb-user" class:alternate={i % 2 === 1} role="listitem"
          on:mouseover={() => hoveringUser = user.username} on:focus={() => {}}>

          <span class="rank">#{user.rank}</span>
          <span class="name">
            {#if user.username !== ""}
              <a href="/u/{user.username}/{game}" class:registered={!(/user\d+/.test(user.username))}>{user.name}</a>
            {:else}
              <span>{user.name}</span>
            {/if}
          </span>
          <span class="rating">{
            game === 'chu3' || game === 'ongeki' ?
              (user.rating / 100).toFixed(2) :
              user.rating.toLocaleString()
          }</span>
          <span class="accuracy">{(+user.accuracy).toFixed(2)}%</span>
          <span class="fc">{user.fullCombo}</span>
          <span class="ap">{user.allPerfect}</span>
        </div>
      {/each}
    </div>

    <Pagination {page} {totalPages} on:updatePage={handleUpdatePage} />

    <Tooltip triggeredBy=".name" loading={hoverLoading}>
      <UserCard username={hoveringUser} {game} setLoading={l => hoverLoading = l} />
    </Tooltip>
  {/if}

  <StatusOverlays error={error} loading={!d} />
</main>

<style lang="sass">
  @use "../vars"

  .leaderboard-container
    display: flex
    flex-direction: column

  .lb-user
    display: flex
    align-items: center
    justify-content: space-between
    width: 100%
    gap: 12px
    border-radius: vars.$border-radius
    padding: 6px 12px
    box-sizing: border-box

    > *:not(.name)
      text-align: center

    .name
      min-width: 100px
      flex: 1

      > a
        color: unset

      .registered
        background: vars.$grad-special
        color: transparent
        -webkit-background-clip: text
        background-clip: text

    .accuracy, .rating
      width: 15%
      min-width: 45px

    .rating
      font-weight: bold
      color: white

    .fc, .ap
      width: 5%
      min-width: 20px

    @media (max-width: vars.$w-mobile)
      font-size: 0.9rem

      .accuracy
        display: none

    &.alternate
      background-color: vars.$ov-light

</style>

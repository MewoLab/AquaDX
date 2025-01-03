<script lang="ts">
  import { CHARTJS_OPT, coverNotFound, pfpNotFound, registerChart, renderCal, title, tooltip, pfp } from "../libs/ui";
  import type {
    GenericGamePlaylog,
    GenericGameSummary,
    MusicMeta,
    TrendEntry,
    AquaNetUser,

    AllMusic

  } from "../libs/generalTypes";
  import { DATA_HOST } from "../libs/config";
  import 'cal-heatmap/cal-heatmap.css';
  import moment from "moment";
  import 'chartjs-adapter-moment';
  import { CARD, DATA, GAME, USER } from "../libs/sdk";
  import { type GameName, getMult, roundFloor } from "../libs/scoring";
  import StatusOverlays from "../components/StatusOverlays.svelte";
  import Icon from "@iconify/svelte";
  import { GAME_TITLE, t } from "../libs/i18n";
  import RankDetails from "../components/RankDetails.svelte";
  import RatingComposition from "../components/RatingComposition.svelte";
  import useLocalStorage from "../libs/hooks/useLocalStorage.svelte";
  import Line from "../components/chart/Line.svelte";

  const TREND_DAYS = 60

  registerChart()

  export let username: string;
  export let game: GameName = "mai2"
  let calElement: HTMLElement
  let error: string;
  let me: AquaNetUser
  title(`User ${username}`)
  const rounding = useLocalStorage("rounding", true);

  const titleText = GAME_TITLE[game]

  interface MusicAndPlay extends MusicMeta, GenericGamePlaylog {}

  let d: {
    user: GenericGameSummary,
    trend: TrendEntry[]
    recent: MusicAndPlay[],
    validGames: [ string, string ][],
  } | null

  let allMusics: AllMusic
  let showDetailRank = false
  let isLoading = false

  function init() {
    USER.isLoggedIn() && USER.me().then(u => me = u)

    CARD.userGames(username).then(games => {
      if (!games[game]) {
        // Find a valid game
        const valid = Object.entries(games).filter(([g, valid]) => valid)
        if (!valid || !valid[0]) return error = t("UserHome.NoValidGame")
        window.location.href = `/u/${username}/${valid[0][0]}`
      }

      Promise.all([
        GAME.userSummary(username, game),
        GAME.trend(username, game),
        DATA.allMusic(game),
      ]).then(([user, trend, music]) => {
        console.log(user)
        console.log(trend)
        console.log(games)

        // If game is wacca, divide all ratings by 10
        if (game === 'wacca') {
          user.rating /= 10
          trend.forEach(it => it.rating /= 10)
          user.recent.forEach(it => {
            it.beforeRating /= 10
            it.afterRating /= 10
          })
        }

        const minDate = moment().subtract(TREND_DAYS, 'days').format("YYYY-MM-DD")
        d = {user,
          trend: trend.filter(it => it.date >= minDate && it.plays != 0),
          recent: user.recent.map(it => {return {...music[it.musicId], ...it}}),
          validGames: Object.entries(GAME_TITLE).filter(g => games[g[0] as GameName])
        }
        allMusics = music
        renderCal(calElement, trend.map(it => {return {date: it.date, value: it.plays}})).then(() => {
          // Scroll to the rightmost
          calElement.scrollLeft = calElement.scrollWidth - calElement.clientWidth
        })
      }).catch((e) => error = e.message);
    }).catch((e) => { error = e.message; console.error(e) } );
  }

  if (Object.keys(GAME_TITLE).includes(game)) init()
  else error = t("UserHome.InvalidGame", {game})

  const setRival = (isAdd: boolean) => {
    isLoading = true
    GAME.setRival(game, username, isAdd).then(() => {
      d!.user.rival = isAdd
    }).catch(e => error = e.message).finally(() => isLoading = false)
  }
</script>

<main id="user-home" class="content">
  {#if d}
    <div class="user-pfp">
      <img use:pfp={d.user.aquaUser} alt="" class="pfp" on:error={pfpNotFound}>
      <div class="name-box">
        <h2>{d.user.name}</h2>
        {#if typeof d.user.rival === 'boolean' && game === 'mai2'}
          <span class="clickable" on:click={() => setRival(!d?.user.rival)} role="button" tabindex="0"
             on:keydown={e => e.key === "Enter" && setRival(!d?.user.rival)}>
            {d.user.rival ? t("UserHome.RemoveRival") : t("UserHome.AddRival")}
          </span>
        {/if}
        {#if me && me.username === username}
          <a class="setting-icon clickable" use:tooltip={t("UserHome.Settings")} href="/settings">
            <Icon icon="eos-icons:rotating-gear"/>
          </a>
        {/if}
      </div>
      <nav>
        {#each d.validGames as [g, name]}
          <a href={`/u/${username}/${g}`} class:active={game === g}>{name}</a>
        {/each}
      </nav>
    </div>

    <div>
      <h2>{titleText} {t('UserHome.Statistics')}</h2>
      <div class="scoring-info">
        <div class="chart">
          <div class="info-top">
            <div class="rating">
              <span>{game === 'mai2' ? t("UserHome.DXRating"): t("UserHome.Rating")}</span>
              <span>{
                game === 'chu3' ?
                  (d.user.rating / 100).toFixed(2) :
                  d.user.rating.toLocaleString()
              }</span>
            </div>

            <div class="rank">
              <span>{t('UserHome.ServerRank')}</span>
              <span>#{(d.user.serverRank + 1).toLocaleString()}</span>
            </div>
          </div>

          <div class="trend">
            <!-- ChartJS cannot be fully responsive unless there is a parent div that's independent from its size and helps it determine its size -->
            <div class="chartjs-box-reference">
              {#if d.trend.length <= 1}
                <div class="no-data">{t("UserHome.NoData", { days: TREND_DAYS })}</div>
              {:else}
                <Line data={{
                  datasets: [
                    {
                      label: 'Rating',
                      data: d.trend.map(it => {return {x: Date.parse(it.date), y: it.rating}}),
                      borderColor: '#646cff',
                      tension: 0.1,

                      // TODO: Set X axis span to 3 months
                    }
                  ]
                }} options={CHARTJS_OPT} />
              {/if}
            </div>
          </div>

          {#if Object.entries(d.user.detailedRanks).length > 0}
            <div class="info-bottom clickable" use:tooltip={t("UserHome.ShowRanksDetails")}
                 on:click={() => showDetailRank = !showDetailRank} role="button" tabindex="0"
                 on:keydown={e => e.key === "Enter" && (showDetailRank = !showDetailRank)}>
              {#each d.user.ranks as r}
                <div><span>{r.name}</span><span>{r.count}</span></div>
              {/each}
            </div>
          {:else}
            <div class="info-bottom">
              {#each d.user.ranks as r}
                <div><span>{r.name}</span><span>{r.count}</span></div>
              {/each}
            </div>
          {/if}
        </div>

        <div class="other-info">
          <div class="accuracy">
            <span>{t('UserHome.Accuracy')}</span>
            <span>{(d.user.accuracy).toFixed(2)}%</span>
          </div>

          <div class="max-combo">
            <span>{t("UserHome.MaxCombo")}</span>
            <span>{d.user.maxCombo}</span>
          </div>

          <div class="full-combo">
            <span>{t("UserHome.FullCombo")}</span>
            <span>{d.user.fullCombo}</span>
          </div>

          <div class="all-perfect">
            <span>{t("UserHome.AllPerfect")}</span>
            <span>{d.user.allPerfect}</span>
          </div>

          <div class="total-dx-score">
            <span>{game === 'mai2' ? t('UserHome.DXScore') : t("UserHome.Score")}</span>
            <span>{d.user.totalScore.toLocaleString()}</span>
          </div>
        </div>
      </div>
    </div>

    {#if showDetailRank}<RankDetails g={d.user}/>{/if}

    <div>
      <h2>{t('UserHome.PlayActivity')}</h2>
      <div class="activity-info">
        <div class="hide-scrollbar" id="cal-heatmap" bind:this={calElement}></div>

        <div class="info-bottom">
          <div class="plays">
            <span>{t("UserHome.Plays")}</span>
            <span>{d.user.plays}</span>
          </div>

          <div class="time">
            <span>{t('UserHome.PlayTime')}</span>
            <span>{(d.user.totalPlayTime / 60).toFixed(1)} hr</span>
          </div>

          <div class="first-play">
            <span>{t('UserHome.FirstSeen')}</span>
            <span>{moment(d.user.joined).format("YYYY-MM-DD")}</span>
          </div>

          <div class="last-play">
            <span>{t('UserHome.LastSeen')}</span>
            <span>{moment(d.user.lastSeen).format("YYYY-MM-DD")}</span>
          </div>

          <div class="last-version">
            <span>{t('UserHome.Version')}</span>
            <span>{d.user.lastVersion}</span>
          </div>
        </div>
      </div>
    </div>

    <RatingComposition title="B30" comp={d.user.ratingComposition.best30} {allMusics} {game}/>
    <RatingComposition title="B35" comp={d.user.ratingComposition.best35} {allMusics} {game}/>
    <RatingComposition title="B15" comp={d.user.ratingComposition.best15} {allMusics} {game}/>
    <!-- <RatingComposition title="Hot 10" comp={d.user.ratingComposition.hot10} {allMusics} {game}/> -->
    <!-- <RatingComposition title="N10" comp={d.user.ratingComposition.next10} {allMusics} {game}/> -->
    <RatingComposition title="Recent 10" comp={d.user.ratingComposition.recent10} {allMusics} {game} top={10}/>

    <div class="recent">
      <h2>{t('UserHome.RecentScores')}</h2>
      <div class="scores">
        {#each d.recent as r, i}
          <div class:alt={i % 2 === 0}>
            <img src={`${DATA_HOST}/d/${game}/music/00${r.musicId.toString().padStart(6, '0').substring(2)}.png`} alt="" on:error={coverNotFound} />
            <div class="info">
              <div>{r.name ?? t("UserHome.UnknownSong")}</div>
              <div>
                {#if r.isAllPerfect || r.isAllJustice}
                  <img src="/assets/imgs/All Perfect.png" alt="All Perfect" />
                {:else if r.isFullCombo}
                  <img src="/assets/imgs/Full Combo.png" alt="Full Combo" />
                {/if}
                <span class={`lv level-${r.level === 10 ? 5 : r.level}`}>
                  <span>
                    {r.notes?.[r.level === 10 ? 0 : r.level]?.lv?.toFixed(1) ?? r.worldsEndTag ?? '-'}
                  </span>
                </span>
                <span class={`rank-${getMult(r.achievement, game)[2].toString()[0]}`}>
                  <span class="rank-text">{("" + getMult(r.achievement, game)[2]).replace("p", "+")}</span>
                  <span class="rank-num" use:tooltip={(r.achievement / 10000).toFixed(4)}>
                    {
                      rounding.value ?
                        roundFloor(r.achievement, game, 1) :
                        (r.achievement / 10000).toFixed(4)
                    }%
                  </span>
                </span>
                {#if game === 'mai2' || game === 'wacca'}
                  <span class:increased={r.afterRating - r.beforeRating > 0} class="dx-change">
                    {r.afterRating === r.beforeRating ? '-' : (r.afterRating - r.beforeRating).toFixed(0)}
                  </span>
                {/if}
              </div>
            </div>
          </div>
        {/each}
      </div>
    </div>
  {/if}

  <StatusOverlays {error} loading={!d || isLoading} />
</main>

<style lang="sass">
@use "../vars"

#user-home
  .user-pfp
    display: flex
    align-items: flex-end
    gap: vars.$gap
    margin-top: -72px
    position: relative

    h2
      font-size: 2rem
      margin: 0
      white-space: nowrap

    nav
      position: absolute
      display: flex
      flex-direction: row
      gap: 10px
      top: 4px
      right: 0

    .setting-icon
      font-size: 1.5rem
      color: vars.$c-main
      cursor: pointer
      display: flex
      align-items: center

    .name-box
      flex: 1
      display: flex
      align-items: center
      justify-content: space-between
      gap: 10px

  .pfp
    width: 100px
    height: 100px
    border-radius: vars.$border-radius
    object-fit: cover

  @media (max-width: vars.$w-mobile)
    .user-pfp
      margin-top: -68px
      h2
        font-size: 1.5rem

    .pfp
      width: 80px
      height: 80px

  .info-bottom, .info-top, .other-info
    display: flex
    gap: vars.$gap

    > div
      display: flex
      flex-direction: column

      > span:first-child
        font-weight: bold
        font-size: 0.8rem

        // character spacing
        letter-spacing: 0.1em
        color: vars.$c-main

  .info-bottom
    width: max-content

  .info-top > div > span:last-child
    font-size: 1.5rem

  .info-bottom
    max-width: 100%
    flex-wrap: wrap
    row-gap: 0.5rem

  .scoring-info
    display: flex
    gap: vars.$gap
    max-height: 250px

    .chart
      flex: 0 1 790px
      display: flex
      flex-direction: column

    .other-info
      flex: 1 0 100px
      flex-direction: column
      gap: 0
      justify-content: space-between

    .trend
      height: 300px
      width: 100%
      max-width: 790px

      position: relative

      > .chartjs-box-reference
        position: absolute
        inset: 0
        display: flex
        align-items: center
        justify-content: center

        .no-data
          opacity: 0.5
          user-select: none

    @media (max-width: vars.$w-mobile)
      flex-direction: column
      max-height: unset

      .chart
        flex: 0

        .trend
          max-height: 130px

      .other-info
        > div
          flex-direction: row
          justify-content: space-between

      .info-bottom
        justify-content: space-between

  .activity-info
    display: flex
    flex-direction: column
    gap: vars.$gap

    #cal-heatmap
      overflow-x: auto

    @media (max-width: vars.$w-mobile)
      #cal-heatmap
        width: 100%

      .info-bottom
        flex-direction: column
        gap: 0
        width: 100%

        > div
          flex-direction: row
          justify-content: space-between

  // Recent Scores section
  .recent
    .scores
      display: flex
      flex-direction: column
      flex-wrap: wrap
      gap: vars.$gap

      > div.alt
        background-color: rgba(white, 0.03)
        border-radius: vars.$border-radius

      // Image and song info
      > div
        display: flex
        align-items: center
        gap: vars.$gap
        padding-right: 16px
        max-width: 100%
        box-sizing: border-box

        img
          width: 50px
          height: 50px
          border-radius: vars.$border-radius
          object-fit: cover

        // Song info and score
        > div.info
          flex: 1
          display: flex
          justify-content: space-between
          align-items: center
          overflow: hidden

          // Limit song name to one line
          > div:first-child
            flex: 1
            min-width: 0
            overflow: hidden
            text-overflow: ellipsis
            white-space: nowrap

          // Make song score and rank not wrap
          > div:last-child
            white-space: nowrap
            display: flex
            align-items: center
            gap: 10px

            img
              height: 1.5em
              width: 1.5em

          @media (max-width: vars.$w-mobile)
            flex-direction: column
            gap: 0

            .rank-text
              text-align: left

            // Score and rank should be space-between on mobile
            > div:last-child
              display: flex
              justify-content: space-between
              gap: 10px

              .lv
                margin-right: auto

        .rank-S
          // Gold green gradient on text
          background: vars.$grad-special
          -webkit-background-clip: text
          color: transparent

        .rank-A
          color: #ff8a8a

        .rank-B
          color: #6ba6ff

        .lv
          min-width: 30px
          text-align: center
          background: rgba(var(--lv-color), 0.6)
          padding: 0 6px
          border-radius: vars.$border-radius

        .lv.level-5 > span
          color: transparent
          background: var(--lv-text-clip)
          background-clip: text
          -webkit-background-clip: text
          font-weight: bold
          font-size: 1em
          font-family: 'Arial Black', sans-serif

        span
          display: inline-block
          text-align: right

        // Vertical table-like alignment
        span.rank-text
          min-width: 38px
        span.rank-num
          min-width: 60px
        span.dx-change
          min-width: 30px

      span.increased
        &:before
          content: "+"
        color: vars.$c-good
</style>

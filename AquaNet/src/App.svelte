<script lang="ts">
  import { Route, Router } from "svelte5-router";
  import Welcome from "./pages/Welcome.svelte";
  import UserHome from "./pages/UserHome.svelte";
  import Home from "./pages/Home.svelte";
  import Ranking from "./pages/Ranking.svelte";
  import { CARD, USER } from "./libs/sdk";
  import type { AquaNetUser } from "./libs/generalTypes";
  import Settings from "./pages/User/Settings.svelte";
  import MaiPhoto from "./pages/MaiPhoto.svelte";
  import { pfp, tooltip } from "./libs/ui"
  import { ANNOUNCEMENT } from "./libs/config";
  import { t } from "./libs/i18n";
  import Transfer from "./pages/Transfer/Transfer.svelte";

  console.log(`%c
┏━┓         ┳━┓━┓┏━
┣━┫┏━┓┓ ┏┏━┓┃ ┃ ┣┫
┛ ┗┗━┫┗━┻┗━┻┻━┛━┛┗━
     ┗       v${APP_VERSION}`, `
     background: linear-gradient(-45deg, rgba(18,194,233,1) 0%, rgba(196,113,237,1) 50%, rgba(246,79,89,1) 100%);
     font-size: 2em;
     font-family: Monospace;
     unicode-bidi: isolate;
     -webkit-background-clip: text;
     -webkit-text-fill-color: transparent;`)

  export let url = "";
  let me: AquaNetUser
  let playedMai = false

  if (USER.isLoggedIn())
  {
    USER.me().then(m => {
      me = m
      CARD.userGames(me.username).then(game => {
        playedMai = !!game.mai2
      })
    }).catch(e => console.error(e))

  }
  let path = window.location.pathname;
</script>

<nav>
  {#if path !== "/"}
    <a class="logo" href={USER.isLoggedIn() ? "/home" : "/"}>
      <img src="/assets/icons/android-chrome-192x192.png" alt="AquaDX"/>
      <span>AquaNet</span>
    </a>
  {/if}
  {#if ANNOUNCEMENT}
    <div class="announcement">
      <strong>{t('navigation.notice')}</strong>: {ANNOUNCEMENT}
    </div>
  {/if}
  <a href="/home">{t('navigation.home').toLowerCase()}</a>
  <!-- <div on:click={() => alert("Coming soon™")} on:keydown={e => e.key === "Enter" && alert("Coming soon™")}
       role="button" tabindex="0">{t('navigation.maps').toLowerCase()}</div> -->
  <a href="/ranking">{t('navigation.rankings').toLowerCase()}</a>
  {#if playedMai}
    <a href="/pictures">photo</a>
  {/if}
  {#if me}
    <a href="/u/{me.username}" use:tooltip={t('navigation.profile')}>
      <img alt="profile" class="pfp" use:pfp={me}/>
    </a>
  {/if}
</nav>

<Router {url}>
  <Route path="/" component={Welcome} />
  <Route path="/home" component={Home} />
  <Route path="/ranking" component={Ranking} />
  <Route path="/ranking/:game" component={Ranking} />
  <Route path="/u/:username" component={UserHome} />
  <Route path="/u/:username/:game" component={UserHome} />
  <Route path="/settings" component={Settings} />
  <Route path="/pictures" component={MaiPhoto} />
  <Route path="/transfer" component={Transfer} />
</Router>

<style lang="sass">
  @use "vars"

  nav
    display: flex
    justify-content: flex-end
    align-items: center
    gap: 32px
    height: vars.$nav-height

    padding: 0 48px

    z-index: 10
    position: relative

    img
      width: 1.5rem
      height: 1.5rem
      border-radius: vars.$border-radius
      object-fit: cover

    .announcement
      position: absolute
      left: 50%
      transform: translate(-50%, 0)
      top: 0
      width: 50%
      height: 100%
      display: flex
      justify-content: center
      align-content: center
      z-index: -1
      background: linear-gradient(90deg, #6f0f0f00 0%, vars.$c-shadow 50%, #6f0f0f00 100%)
      font-size: 1.125em
      text-decoration: none !important
      color: inherit !important

    .pfp
      width: 2rem
      height: 2rem

    .logo
      display: flex
      align-items: center
      gap: 8px
      font-weight: bold
      color: vars.$c-main
      letter-spacing: 0.2em
      flex: 1

      @media (max-width: vars.$w-mobile)
        > span
          display: none

    @media (max-width: vars.$w-mobile)
      justify-content: center

</style>

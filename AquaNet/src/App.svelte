<script lang="ts">
  import { Route, Router } from "svelte5-router";
  import Welcome from "./pages/Welcome.svelte";
  import UserHome from "./pages/UserHome.svelte";
  import Home from "./pages/Home.svelte";
  import Ranking from "./pages/Ranking.svelte";
  import { USER } from "./libs/sdk";
  import type { AquaNetUser } from "./libs/generalTypes";
  import Settings from "./pages/User/Settings.svelte";
  import { pfp } from "./libs/ui"

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

  if (USER.isLoggedIn()) USER.me().then(m => me = m).catch(e => console.error(e))

  let path = window.location.pathname;
</script>

<nav>
  {#if path !== "/"}
    <a class="logo" href={USER.isLoggedIn() ? "/home" : "/"}>
      <img src="/assets/icons/android-chrome-192x192.png" alt="AquaDX"/>
      <span>AquaNet</span>
    </a>
  {/if}
  <a href="/home">home</a>
  <div on:click={() => alert("Coming soon™")} on:keydown={e => e.key === "Enter" && alert("Coming soon™")}
       role="button" tabindex="0">maps</div>
  <a href="/ranking">rankings</a>
  {#if me}
    <a href="/u/{me.username}">
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

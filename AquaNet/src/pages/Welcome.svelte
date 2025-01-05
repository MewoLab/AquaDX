<script lang="ts">
  import { Turnstile } from "svelte-turnstile";
  import { slide } from 'svelte/transition';
  import { TURNSTILE_SITE_KEY } from "../libs/config";
  import Icon from "@iconify/svelte";
  import { USER } from "../libs/sdk";
  import { t } from "../libs/i18n"

  let params = new URLSearchParams(window.location.search)

  let state = "home"
  $: isSignup = state === "signup"
  let submitting = false

  let email = ""
  let password = ""
  let username = ""
  let turnstile = ""
  let turnstileReset: () => void | undefined;

  let error = ""
  let verifyMsg = ""

  if (USER.isLoggedIn()) {
    window.location.href = "/home"
  }

  if (params.get('confirm-email')) {
    state = 'verify'
    verifyMsg = t("welcome.verifying")
    submitting = true

    // Send request to server
    USER.confirmEmail(params.get('confirm-email')!)
      .then(() => {
        verifyMsg = t('welcome.verified')
        submitting = false

        // Clear the query param
        window.history.replaceState({}, document.title, window.location.pathname)
      })
      .catch(e => verifyMsg = t('welcome.verification-failed', { message: e.message }))
  }

  async function submit(): Promise<any> {
    submitting = true

    // Check if username and password are valid
    if (email === "" || password === "") {
      error = t("welcome.email-password-missing")
      return submitting = false
    }

    if (turnstile === "") {
      // Sleep for 100ms to allow Turnstile to finish
      error = t("welcome.waiting-turnstile")
      return setTimeout(submit, 100)
    }

    // Signup
    if (isSignup) {
      if (username === "") {
        error = t("welcome.username-missing")
        return submitting = false
      }

      // Send request to server
      await USER.register({ username, email, password, turnstile })
        .then(() => {
          // Show verify email message
          state = 'verify'
          verifyMsg = t("welcome.verification-sent", { email })
        })
        .catch(e => {
          error = e.message
          submitting = false
          turnstileReset()
        })
    }
    else {
      // Send request to server
      await USER.login({ email, password, turnstile })
        .then(() => window.location.href = "/home")
        .catch(e => {
          if (e.message === 'Email not verified - STATE_0') {
            state = 'verify'
            verifyMsg = t("welcome.verify-state-0")
          }
          else if (e.message === 'Email not verified - STATE_1') {
            state = 'verify'
            verifyMsg = t("welcome.verify-state-1")
          }
          else if (e.message === 'Email not verified - STATE_2') {
            state = 'verify'
            verifyMsg = t("welcome.verify-state-2")
          }
          else {
            error = e.message
            submitting = false
            turnstileReset()
          }
        })
    }

    submitting = false
  }

</script>

<main id="home" class="no-margin">
  <div>
    <h1 id="title">AquaNet</h1>
    {#if state === "home"}
      <div class="btn-group" transition:slide>
        <button on:click={() => state = 'login'}>{t('welcome.btn-login')}</button>
        <button on:click={() => state = 'signup'}>{t('welcome.btn-signup')}</button>
      </div>
    {:else if state === "login" || state === "signup"}
      <div class="login-form" transition:slide>
        {#if error}
          <span class="error">{error}</span>
        {/if}
        <div on:click={() => state = 'home'} on:keypress={() => state = 'home'}
             role="button" tabindex="0" class="clickable">
          <Icon icon="line-md:chevron-small-left" />
          <span>{t('back')}</span>
        </div>
        {#if isSignup}
          <input type="text" placeholder={t('username')} bind:value={username}>
        {/if}
        <input type="email" placeholder={t('email')} bind:value={email}>
        <input type="password" placeholder={t('password')} bind:value={password}>
        <button on:click={submit}>
          {#if submitting}
            <Icon icon="line-md:loading-twotone-loop"/>
          {:else}
            {isSignup ? t('welcome.btn-signup') : t('welcome.btn-login')}
          {/if}
        </button>
        <Turnstile siteKey={TURNSTILE_SITE_KEY} bind:reset={turnstileReset}
                   on:turnstile-callback={e => console.log(turnstile = e.detail.token)}
                   on:turnstile-error={_ => console.log(error = t("welcome.turnstile-error"))}
                   on:turnstile-expired={_ => window.location.reload()}
                   on:turnstile-timeout={_ => console.log(error = t('welcome.turnstile-timeout'))} />
      </div>
    {:else if state === "verify"}
      <div class="login-form" transition:slide>
        <span>{verifyMsg}</span>
        {#if !submitting}
          <button on:click={() => state = 'home'} transition:slide>{t('back')}</button>
        {/if}
      </div>
    {/if}
  </div>

  <div class="light-pollution">
    <div class="l1"></div>
    <div class="l2"></div>
    <div class="l3"></div>
  </div>
</main>

<style lang="sass">
  @use "../vars"

  .login-form
    display: flex
    flex-direction: column
    gap: 8px
    width: calc(100% - 12px)
    max-width: 300px

    div.clickable
      display: flex
      align-items: center

  #home
    color: vars.$c-main
    position: relative
    width: 100%
    height: 100%
    padding-left: 100px
    overflow: hidden
    background-color: black

    box-sizing: border-box

    display: flex
    flex-direction: column
    justify-content: center

    margin-top: -(vars.$nav-height)

    // Content container
    > div
      display: flex
      flex-direction: column
      align-items: flex-start
      width: max-content

      // Switching state container
      > div
        transition: vars.$transition

    #title
      font-family: Quicksand, vars.$font
      user-select: none

      // Gap between text characters
      letter-spacing: 0.2em
      margin-top: 0
      margin-bottom: 32px
      opacity: 0.9

    .btn-group
      display: flex
      gap: 8px

    .light-pollution
      pointer-events: none
      opacity: 0.8

      > div
        position: absolute
        z-index: 1

      .l1
        left: -560px
        top: 90px
        height: 1130px
        width: 1500px
        $color: rgb(158, 110, 230)
        background: radial-gradient(50% 50% at 50% 50%, rgba($color, 0.28) 0%, rgba(0,0,0,0) 100%)

      .l2
        left: -200px
        top: 560px
        height: 1200px
        width: 1500px
        $color: rgb(92, 195, 250)
        background: radial-gradient(50% 50% at 50% 50%, rgba($color, 0.28) 0%, rgba(0,0,0,0) 100%)

      .l3
        left: -600px
        opacity: 0.7
        top: -630px
        width: 1500px
        height: 1000px
        $color: rgb(230, 110, 156)
        background: radial-gradient(50% 50% at 50% 50%, rgba($color, 0.28) 0%, rgba(0,0,0,0) 100%)

    @media (max-width: 500px)
      align-items: center
      padding-left: 0
</style>

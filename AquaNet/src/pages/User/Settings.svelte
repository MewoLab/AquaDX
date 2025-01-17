<!-- Svelte 4.2.11 -->

<script lang="ts">
  import { slide, fade } from "svelte/transition";
  import type { AquaNetUser } from "../../libs/generalTypes";
  import { CARD, USER } from "../../libs/sdk";
  import StatusOverlays from "../../components/StatusOverlays.svelte";
  import Icon from "@iconify/svelte";
  import { pfp } from "../../libs/ui";
  import { t, ts } from "../../libs/i18n";
  import { FADE_IN, FADE_OUT } from "../../libs/config";
  import Cropper from "svelte-easy-crop";
  import UserBox from "../../components/settings/ChuniSettings.svelte";
  import Mai2Settings from "../../components/settings/Mai2Settings.svelte";
  import WaccaSettings from "../../components/settings/WaccaSettings.svelte";
  import GeneralGameSettings from "../../components/settings/GeneralGameSettings.svelte";

  USER.ensureLoggedIn()

  let me: AquaNetUser;
  let error: string;
  let submitting = ""
  let tab = 0
  let tabs = [ 'profile', 'game' ]

  const profileFields = [
    [ 'displayName', t('settings.profile.name') ],
    [ 'username', t('settings.profile.username') ],
    [ 'password', t('settings.profile.password') ],
    [ 'profileLocation', t('settings.profile.location') ],
    [ 'profileBio', t('settings.profile.bio') ],
  ] as const

  // Fetch user data
  const getMe = () => USER.me().then((m) => {
    if (pfpCropURL != null) {
      URL.revokeObjectURL(pfpCropURL);
      pfpField.value = "";
      pfpCropURL = null;
    }
    me = m

    CARD.userGames(m.username).then(games => {
      if (games.chu3 && !tabs.includes('chu3')) {
        tabs = [...tabs, 'chu3']
      }
      if (games.mai2 && !tabs.includes('mai2')) {
        tabs = [...tabs, 'mai2']
      }
      if (games.wacca && !tabs.includes('wacca')) {
        tabs = [...tabs, 'wacca']
      }
    })
  }).catch(e => error = e.message)
  getMe()

  let changed: string[] = []
  let pfpField: HTMLInputElement
  let pfpCropURL: string | null = null;
  let pfpCrop = { width: 0, height: 0, x: 0, y: 0 };

  function submit(field: string, value: string) {
    if (submitting) return
    submitting = field

    USER.setting(field, value).then(() => {
      changed = changed.filter(c => c !== field)
    }).catch(e => error = e.message).finally(() => submitting = "")
  }

  function uploadPfp() {
    if (submitting) return
    // Don't know why this isn't just a part of the cropper module. Have to do this myself.. What a shame
    let canvas = document.createElement("canvas");
    let ctx = canvas.getContext("2d");
    canvas.width = 256;
    canvas.height = 256;
    let img = document.createElement("img");
    img.onload = () => {
      ctx?.drawImage(img, pfpCrop.x, pfpCrop.y, pfpCrop.width, pfpCrop.height, 0, 0, 256, 256);
      canvas.toBlob(blob => {
        if (!blob) return;
        submitting = 'profilePicture'
        USER.uploadPfp(blob as File).then(() => {
          me.profilePicture = me.username
          // reload
          // this doesn't work btw
          setTimeout(getMe, 200);
        }).catch(e => error = e.message).finally(() => submitting = "")
      });
    }
    img.src = pfpCropURL ?? "";
  }
  function handlePfpUpload(e: Event & { target: HTMLInputElement }) {
    if (!e.target) return;
    let files = e?.target?.files;
    if (!files || files.length <= 0) return;
    let file = files[0];
    console.log(me.username, me);
    switch (file.type) {
      case "image/gif":
        USER.uploadPfp(file).then(() => {
          me.profilePicture = me.username
          // reload
          setTimeout(getMe, 200);
        }).catch(e => error = e.message).finally(() => submitting = "")
        break;
      case "image/png":
      case "image/jpg":
      case "image/jpeg":
      case "image/webp":
        pfpCropURL = URL.createObjectURL(file);
        break;
      default:
        error = t("settings.profile.bad-format");
    }
  };

  const passwordAction = (node: HTMLInputElement, whether: boolean) => {
    if (whether) node.type = 'password'
  }
</script>

<main class="content">
  <div class="outer-title-options">
    <h2>{t('settings.title')}</h2>
    <nav>
      {#each tabs as tabName, i}
        <div transition:slide={{axis: 'x'}} class:active={tab === i}
             on:click={() => tab = i} on:keydown={e => e.key === 'Enter' && (tab = i)}
             role="button" tabindex="0">
          {ts(`settings.tabs.${tabName}`)}
        </div>
      {/each}
    </nav>
  </div>

  {#if tab === 0 && me}
    <!-- Tab 0: Profile settings -->
    <div out:fade={FADE_OUT} in:fade={FADE_IN} class="fields">
      <div class="field">
        <label for="profile-upload">{t('settings.profile.picture')}</label>
        <div>
          {#if me && me.profilePicture}
            <div on:click={() => pfpField.click()} on:keydown={e => e.key === 'Enter' && pfpField.click()}
                 role="button" tabindex="0" class="clickable">
              <img use:pfp={me} alt="Profile" />
            </div>
          {:else}
            <button on:click={() => pfpField.click()}>
              {t('settings.profile.upload-new')}
            </button>
          {/if}
        </div>
        <!-- Genuinely don't know why this is giving me an intellisense error. Works fine. -->
        <input id="profile-upload" type="file" accept="image/*" style="display: none" bind:this={pfpField}
               on:change={handlePfpUpload} />
      </div>

      {#each profileFields as [field, name], i (field)}
        <div class="field">
          <label for={field}>{name}</label>
          <div>
            <input id={field} type="text" use:passwordAction={field === 'password'}
                   bind:value={me[field]} on:input={() => changed = [...changed, field]}
                   placeholder={field === 'password' ? t('settings.profile.unchanged') : t('settings.profile.unset')}/>
            {#if changed.includes(field) && me[field]}
              <button transition:slide={{axis: 'x'}} on:click={() => submit(field, me[field])}>
                {#if submitting === field}
                  <Icon icon="line-md:loading-twotone-loop" />
                {:else}
                  {t('settings.profile.save')}
                {/if}
              </button>
            {/if}
          </div>
        </div>
      {/each}
      <div class="field m-t">
        <div class="bool">
          <input id="optOutOfLeaderboard" type="checkbox" bind:checked={me.optOutOfLeaderboard}
                 on:change={() => submit('optOutOfLeaderboard', me.optOutOfLeaderboard.toString())}/>
          <label for="optOutOfLeaderboard">
            <span class="name">{ts(`settings.fields.optOutOfLeaderboard.name`)}</span>
            <span class="desc">{ts(`settings.fields.optOutOfLeaderboard.desc`)}</span>
          </label>
        </div>
      </div>
    </div>
  {:else if tabs[tab] === 'chu3'}
    <!-- Userbox settings -->
    <UserBox />
  {:else if tabs[tab] === 'mai2'}
    <Mai2Settings username={me.username} />
  {:else if tabs[tab] === 'wacca'}
    <WaccaSettings />
  {:else if tabs[tab] === 'game'}
    <GeneralGameSettings />
  {/if}

  <StatusOverlays {error} loading={!me || !!submitting} />
</main>

{#if pfpCropURL != null}
  <div class="overlay" transition:fade>
    <div>
      <div class="cropper-container">
        <Cropper maxZoom={1e9} oncropcomplete={(e) => pfpCrop = e.pixels} image={pfpCropURL ?? "assets/imgs/no_profile.png"} aspect={1} cropShape="round"></Cropper>
      </div>
      <button on:click={uploadPfp}>
        {t("settings.profile.save")}
      </button>
      <button on:click={getMe}>
        {t("back")}
      </button>
    </div>
  </div>
{/if}

<style lang="sass">
  @use "../../vars"

  .fields
    display: flex
    flex-direction: column
    gap: 12px

  .bool
    display: flex
    align-items: center
    gap: 1rem

    label
      display: flex
      flex-direction: column

      .desc
        opacity: 0.6

  .field
    display: flex
    flex-direction: column

    label
      max-width: max-content

    > div:not(.bool)
      display: flex
      align-items: center
      gap: 1rem
      margin-top: 0.5rem

      > input
        flex: 1

    img
      max-width: 100px
      max-height: 100px
      border-radius: vars.$border-radius
      object-fit: cover
      aspect-ratio: 1

  .cropper-container
    position: relative
    width: 400px
    aspect-ratio: 1
</style>

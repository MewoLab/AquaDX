<script lang="ts">
  import { slide, fade } from "svelte/transition";
  import type { AquaNetUser, ConfirmProps } from "../../libs/generalTypes";
  import { CARD, GAME, USER, type ExportGameName } from "../../libs/sdk";
  import StatusOverlays from "../../components/StatusOverlays.svelte";
  import Icon from "@iconify/svelte";
  import { download, pfp } from "../../libs/ui";
  import { t, ts } from "../../libs/i18n";
  import Cropper from "svelte-easy-crop";

  let me: AquaNetUser;
  let error: string;
  let submitting = ""
  let loading = false;
  let confirm: ConfirmProps | null = null;

  const profileFields = [
    [ 'displayName', t('settings.profile.name') ],
    [ 'username', t('settings.profile.username') ],
    [ 'password', t('settings.profile.password') ],
    [ 'profileBio', t('settings.profile.bio') ],
  ] as const

  // Fetch user data
  const getMe = () => {
    loading = true;
    USER.me().then((m) => {
      if (pfpCropURL != null) {
        URL.revokeObjectURL(pfpCropURL);
        pfpField.value = "";
        pfpCropURL = null;
      }; me = m;
      loading = false;
    }).catch(e => error = e.message)
  }
  getMe();

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
    let canvas = document.createElement("canvas");
    let ctx = canvas.getContext("2d");
    const size = Math.round(Math.min(pfpCrop.width, pfpCrop.height, 1024));
    canvas.width = size;
    canvas.height = size;
    let img = document.createElement("img");
    img.onload = () => {
      ctx?.drawImage(img, pfpCrop.x, pfpCrop.y, pfpCrop.width, pfpCrop.height, 0, 0, size, size);
      canvas.toBlob(blob => {
        if (!blob) return;
        submitting = 'profilePicture'
        USER.uploadPfp(blob as File).then(() => {
          me.profilePicture = me.username
          setTimeout(getMe, 200);
        }).catch(e => error = e.message).finally(() => submitting = "")
      });
    }
    img.src = pfpCropURL ?? "";
  }
  function handlePfpUpload(e: Event & { currentTarget: HTMLInputElement }) {
    if (!e.target) return;
    let files = e?.currentTarget?.files;
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
      case "image/jpeg":
      case "image/webp":
        pfpCropURL = URL.createObjectURL(file);
        break;
      default:
        error = t("settings.profile.bad-format");
    }
  };
  function logOut() {
    localStorage.removeItem("token");
    location.href = "/";
  }

  function requestAccountDeletion() {
    if (submitting) return;
    confirm = {
      title: t('settings.profile.delete-confirm-title'),
      message: t('settings.profile.delete-confirm-message'),
      dangerous: true,
      cancel: () => confirm = null,
      confirm: deleteAccount,
    };
  }

  async function deleteAccount() {
    if (submitting) return;
    submitting = "deleteAccount";

    try {
      const gameSummary = await CARD.userGames(me.username);
      const games = (['mai2', 'chu3', 'ongeki', 'wacca', 'diva'] as ExportGameName[])
        .filter(game => gameSummary[game] !== null);
      const exports = await Promise.all(games.map(async game => ({
        game,
        data: await GAME.export(game),
      })));

      exports.forEach(({game, data}) => download(
        JSON.stringify(data, null, 2),
        `AquaDX_${game}_export_${me.username}.json`,
      ));
      await USER.deleteAccount();
      localStorage.removeItem("token");
      location.href = "/";
    } catch (e) {
      error = (e as Error).message;
      submitting = "";
    }
  }

  const passwordAction = (node: HTMLInputElement, whether: boolean) => {
    if (whether) node.type = 'password'
  }
</script>
<StatusOverlays bind:confirm={confirm} {error} loading={!!submitting || loading} />
{#if !submitting && !error && me}
  <div>
    <div class="fields">
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
        <input id="profile-upload" type="file" accept="image/gif,image/png,image/jpeg,image/webp" hidden bind:this={pfpField}
          on:change={handlePfpUpload} />
      </div>
      {#each profileFields as [field, name], i (field)}
        <div class="field">
          <label for={field}>{name}</label>
          <div>
            {#if field == "profileBio"}
              <textarea id={field} bind:value={me[field]} on:input={() => changed = [...changed, field]} maxlength=255 placeholder={t('settings.profile.unset')}></textarea>
            {:else}
              <input id={field} type="text" use:passwordAction={field === 'password'}
              bind:value={me[field]} on:input={() => changed = [...changed, field]}
              placeholder={field === 'password' ? t('settings.profile.unchanged') : t('settings.profile.unset')}/>
            {/if}

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
      <div class="field m-t">
        <div class="bool">
            <input id="hideCountry" type="checkbox" bind:checked={me.hideCountry}
              on:change={() => submit('hideCountry', me.hideCountry.toString())}/>
            <label for="hideCountry">
              <span class="name">{ts(`settings.fields.hideCountry.name`)}</span>
              <span class="desc">{ts(`settings.fields.hideCountry.desc`)}</span>
            </label>
        </div>
      </div>
      <div class="field m-t">
        <div>
            <button on:click={logOut}>{ts(`settings.profile.logout`)}</button>
        </div>
      </div>
      <div class="field danger-zone m-t">
        <span class="name">{t('settings.profile.delete-title')}</span>
        <span class="desc">{t('settings.profile.delete-description')}</span>
        <div>
          <button class="error" on:click={requestAccountDeletion}>
            <Icon icon="tabler:trash-x-filled" />
            {t('settings.profile.delete')}
          </button>
        </div>
      </div>
    </div>
  </div>
{/if}

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

      > input, > textarea
        flex: 1

    img
      max-width: 100px
      max-height: 100px
      border-radius: vars.$border-radius
      object-fit: cover
      aspect-ratio: 1

  .danger-zone
    margin-top: 1.5rem
    padding-top: 1.5rem
    border-top: 1px solid vars.$ov-lighter

    .desc
      opacity: 0.6

    button
      display: flex
      align-items: center
      gap: 0.5rem



  .cropper-container
    position: relative
    width: 400px
    aspect-ratio: 1
</style>

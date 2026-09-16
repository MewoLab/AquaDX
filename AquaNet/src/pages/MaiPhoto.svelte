<script lang="ts">
  import { onMount } from "svelte";
  import { GAME } from "../libs/sdk";
  import type { PhotoPage } from "../libs/sdk";
  import { AQUA_HOST } from "../libs/config";
  import Loading from "../components/ui/Loading.svelte";
  import Error from "../components/ui/Error.svelte";
  import Pagination from "../components/Pagination.svelte";
  import { t } from "../libs/i18n";

  let page = 1;
  const pageSize = 12;
  let photoData: PhotoPage | null = null;
  let loading = true;
  let error: any = null;

  // Monotonic token so a slow response can never overwrite a newer one
  let requestSeq = 0;

  function readPageFromUrl(): number {
    const raw = new URL(window.location.toString()).searchParams.get("page");
    return raw ? parseInt(raw, 10) || 1 : 1;
  }

  /**
   * Keep the url in sync with the page the server actually served, since
   * out-of-range pages are clamped server-side.
   */
  function syncUrl(effectivePage: number) {
    const url = new URL(window.location.toString());
    const current = url.searchParams.get("page");
    const target = effectivePage > 1 ? effectivePage.toString() : null;
    if (current === target) return;
    if (target === null) url.searchParams.delete("page");
    else url.searchParams.set("page", target);
    history.replaceState({}, "", url.toString());
  }

  async function loadPhotos(targetPage: number) {
    const seq = ++requestSeq;
    // Apply the target immediately so the pager and further clicks act on it
    // instead of on the page that is still being fetched
    page = targetPage;
    loading = true;
    error = null;
    try {
      const data = await GAME.photos(targetPage, pageSize);
      if (seq !== requestSeq) return;
      photoData = data;
      page = data.page;
      syncUrl(data.page);
    } catch (e) {
      if (seq !== requestSeq) return;
      error = e;
    } finally {
      if (seq === requestSeq) loading = false;
    }
  }

  function handleUpdatePage(event: CustomEvent<number>) {
    const newPage = event.detail;
    if (newPage === page) return;
    const url = new URL(window.location.toString());
    url.searchParams.set("page", newPage.toString());
    history.pushState({}, "", url.toString());
    loadPhotos(newPage);
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  onMount(() => {
    loadPhotos(readPageFromUrl());

    const onPopState = () => loadPhotos(readPageFromUrl());

    window.addEventListener("popstate", onPopState);
    return () => window.removeEventListener("popstate", onPopState);
  });
</script>

<main class="content">
  <div class="outer-title-options">
    <h2>{t("maiphoto.title")}</h2>
  </div>

  {#if loading && !photoData}
    <Loading/>
  {:else if error}
    <Error {error}/>
  {:else if photoData}
    {#if photoData.total === 0}
      <blockquote class="info">{t('maiphoto.none')}</blockquote>
    {:else}
      {#if photoData.totalPages > 1}
        <Pagination {page} totalPages={photoData.totalPages} on:updatePage={handleUpdatePage} />
      {/if}

      <div class="pictures" class:loading>
        {#each photoData.photos as photo (photo)}
          <div class="photo-container">
            <img class="rounded-2xl" loading="lazy" src="{AQUA_HOST}/api/v2/game/mai2/my-photo/{photo}" alt="Mai Memorial Photo" />
          </div>
        {/each}
      </div>

      {#if photoData.totalPages > 1}
        <Pagination {page} totalPages={photoData.totalPages} on:updatePage={handleUpdatePage} />
      {/if}
    {/if}
  {/if}
</main>

<style lang="sass">
  @use "../vars"

  .pictures
    display: flex
    flex-wrap: wrap
    justify-content: center
    row-gap: 1rem
    gap: 1rem
    transition: opacity 0.2s ease-in-out

    &.loading
      opacity: 0.5

  .photo-container
    flex: 1 1 300px
    min-width: 280px
    max-width: 100%
    display: flex
    justify-content: center

  .photo-container img
    width: 100%
    height: auto
    object-fit: contain
</style>

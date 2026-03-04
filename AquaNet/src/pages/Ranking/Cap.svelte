<script lang="ts">
    /* Most of this has been backported from AquaNet2.
	import { browser } from "$app/environment"
	import { page } from "$app/state"
	import type { RankedUser } from "$lib/api/game"
	import { LEADERBOARD_PAGE_LENGTH } from "$lib/app/formatting"
	import LL from "$lib/i18n/i18n-svelte"
    */
	import { onMount, tick } from "svelte"
    import type { GenericRanking } from "../../libs/generalTypes";
    import type { GameName } from "../../libs/scoring";
    import { t } from "../../libs/i18n";
    import { GAME } from "../../libs/sdk";
    const LEADERBOARD_PAGE_LENGTH = 100

    interface Props {
        loadedPages: GenericRanking[][],
        /**
         * @description Earliest page to load
         */
        earliestPage: number,
        addOffset: -1 | 1,
        game: GameName
    }
    
    let { loadedPages = $bindable([]), earliestPage = $bindable(), addOffset, game } = $props()

    const pageMayBeAvailable = $derived(
        (earliestPage + addOffset >= 0) // If we go to this page, is it geq 0?
        && (
            addOffset < 0 // If our goal is to go backwards, skip this step.
            || loadedPages[loadedPages.length-1].length === LEADERBOARD_PAGE_LENGTH // Is this page long enough to warrant trying?
        )
    )
    const nextPage = $derived(
        addOffset > 0
        ? earliestPage+loadedPages.length-1+addOffset
        : earliestPage+addOffset
    )
    const isEnd = $derived(loadedPages[loadedPages.length-1].length !== LEADERBOARD_PAGE_LENGTH && addOffset > 0);
    
    const mainElement = document.body // !!! UPDATE
    let cap: HTMLParagraphElement | undefined = $state()
    let loading = false

    async function loadNew(entries: IntersectionObserverEntry[]) {
        if (entries.some(e => !e.isIntersecting) || loading) return

        loading = true;

        // fetch a new page
        let newPage = await GAME.ranking(game, nextPage)

        // add the new page to loadedPages
        loadedPages.splice(((addOffset + 1) / 2) * loadedPages.length, 0, newPage)
        loadedPages = loadedPages // smoking that Svelte Legacy Mode pack

        // update offset if need be
        if (addOffset < 0) {
            earliestPage += addOffset
            // try not to jump up if loading backward
            let lastDocumentSz = mainElement!.scrollHeight
            let lastScrollY = window.scrollY
            tick().then(() => {
                window.scrollTo({ behavior: "instant", top: lastScrollY + mainElement!.scrollHeight - lastDocumentSz})
                loading = false;
            })
        } else loading = false;
    }

    onMount(() => {
        // rootMargin is 500px so we have a better infinite scroll type effect
        const observer = new IntersectionObserver(loadNew, {rootMargin: "500px 0px 500px 0px"})
        $effect(() => {
            if (cap)
                observer.observe(cap)
        })

        return () => observer.disconnect()
    })

</script>
{#if pageMayBeAvailable}
    <div class="cap">
        <p bind:this={cap}>{t("Leaderboard.Loading")}</p>
    </div>
{/if}


<style lang="scss">
    .cap {
        padding: .5em;
        font-weight: 500;
        text-align: center;
    }
</style>
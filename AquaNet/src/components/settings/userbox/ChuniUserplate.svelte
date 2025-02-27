<script lang="ts">
    import { DDS, type RGB } from "../../../libs/userbox/dds"
    import { ddsDB } from "../../../libs/userbox/userbox"

    const DDSreader = new DDS(ddsDB);

    export var chuniLevel: string = "╳"
    export var chuniName: string = "AquaDX"
    export var chuniRating: number = 1.23
    export var chuniNameplate: number = 1
    export var chuniCharacter: number = 0
    export var chuniTrophyName: string = "NEWCOMER"
    export var chuniIsUserbox: boolean = false;

    let ratingToString = (rating: number) => {
        return rating.toFixed(2)
    }

    interface RatingRange {
        min: number,
        offset: number,
        color?: RGB
    };
    // https://en.wikipedia.org/wiki/Chunithm#Rating
    const ratingColors: RatingRange[] = ([
        {min: 0.00, offset: 4, color: {r: 0, g: 191, b: 64}},
        {min: 4.00, offset: 4, color: {r: 255, g: 111, b: 0}},
        {min: 7.00, offset: 4, color: {r: 255, g: 64, b: 64}},
        {min: 10.00, offset: 4, color: {r: 147, g: 38, b: 255}},
        {min: 12.00, offset: 3},
        {min: 13.25, offset: 2},
        {min: 14.50, offset: 1},
        {min: 15.25, offset: 0},
        {min: 16.00, offset: 5}
    ]).filter(f => f.min <= chuniRating);
    const ratingDigitOrder = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."]
    const ratingColorData = (ratingColors[ratingColors.length - 1] ?? ratingColors[0]);
</script>
{#await DDSreader?.getFile(`nameplate:${chuniNameplate.toString().padStart(8, "0")}`, `nameplate:00000001`) then nameplateURL}
    <!-- svelte-ignore a11y_click_events_have_key_events -->
    <!-- svelte-ignore a11y_no_static_element_interactions -->
    <div on:click class="chuni-nameplate" class:chuni-nameplate-clickable={chuniIsUserbox} style:background={`url(${nameplateURL})`}>
        {#await DDSreader?.getFile(`characterThumbnail:${chuniCharacter.toString().padStart(6, "0")}`, `characterThumbnail:000000`) then characterThumbnailURL}
            <img class="chuni-character" src={characterThumbnailURL} alt="Character">
        {/await}
        {#await DDSreader?.getFileFromSheet("surfboard:CHU_UI_title_rank_00_v10.dds", 5, 5 + (75 * 2), 595, 64) then trophyURL}
            <div class="chuni-trophy" title={chuniTrophyName}>
                {chuniTrophyName}
            </div>
            <img src={trophyURL} class="chuni-trophy-bg" alt="Trophy" title={chuniTrophyName}>
        {/await}
        <div class="chuni-user-info">
            <div class="chuni-user-name">
                <span>
                    Lv.
                    <span class="chuni-user-level">
                        {chuniLevel}
                    </span>
                </span>
                <span class="chuni-user-name-text">
                    {chuniName}
                </span>
            </div>
            <div class={`chuni-user-rating color-${ratingColorData.color}`}>
                
                {#await DDSreader?.getFileFromSheet("surfboard:CHU_UI_Common_01_v11.dds", 485, 5 + (28 * ratingColorData.offset), 62, 15, undefined, ratingColorData.color) then url}
                    {#if url}
                        <img src={url} alt="Rating">
                        <span class="chuni-user-rating-number">
                            {#each ratingToString(chuniRating).split("") as digit}
                                {#await DDSreader?.getFileFromSheet("surfboard:CHU_UI_Common_01_v11.dds", 552 + (24 * (ratingDigitOrder.indexOf(digit) ?? 0)), 1 + (28 * ratingColorData.offset), 16, 20, undefined, ratingColorData.color) then url}
                                    <img src={url} alt="Rating Digit">
                                {/await}
                            {/each}
                        </span>
                    {:else}
                        RATING
                        <span class="chuni-user-rating-number">
                            {ratingToString(chuniRating)}
                        </span>
                    {/if}
                {/await}
            </div>
        </div>
    </div>
{/await}
<style lang="sass">
@use "../../../vars"

@font-face
    font-family: "Gothic A1"
    src: url("/assets/fonts/GothicA1.woff2")

.chuni-nameplate
    width: 576px
    height: 228px
    position: relative
    font-size: 16px
    /* Overlap penguin avatar when put side to side */
    z-index: 1 
    
    &.chuni-nameplate-clickable
        cursor: pointer

    .chuni-trophy
        width: 390px
        height: 45px
        background-position: center
        background-size: cover
        color: black

        display: flex
        justify-content: center
        align-items: center
        position: absolute
        right: 25px
        top: 40px

        font-size: 1.15em
        font-family: "Gothic A1", sans-serif
        font-weight: bold

        overflow-x: hidden
        white-space: nowrap
        text-overflow: ellipsis

        z-index: 1
        text-shadow: 0 1px white
        margin: 0 10px

    img.chuni-trophy-bg
        width: 410px
        height: 45px
        position: absolute
        top: 40px
        right: 25px
        z-index: -1

    .chuni-character
        position: absolute
        top: 87px
        right: 25px
        width: 82px
        aspect-ratio: 1
        box-shadow: 0 0 1px 1px white
        background: #efefef

    .chuni-user-info
        height: 82px
        width: 320px
        position: absolute
        top: 87px
        right: 110px
        background: #fff9
        border-radius: 1px
        box-shadow: 0 0 1px 1px #ccc
        display: flex
        flex-direction: column
        
        .chuni-user-name, .chuni-user-rating
            margin: 0 4px
            display: flex
            align-items: center
            color: black
            font-family: "Gothic A1", sans-serif
            font-weight: bold

        .chuni-user-name
            flex: 1 0 65%
            box-shadow: 0 1px 0 #ccc
            white-space: nowrap
            text-overflow: ellipsis

            .chuni-user-level
                font-size: 1.5em
                margin-left: 10px

            .chuni-user-name-text
                margin-left: auto
                font-size: 2em

        .chuni-user-rating
            flex: 1 0 35%
            font-size: 0.875em
            text-shadow: #333 1px 1px, #333 1px -1px, #333 -1px 1px, #333 -1px -1px
            color: #fff

            .chuni-user-rating-number
                font-size: 1.5em
                margin-left: 10px

</style>
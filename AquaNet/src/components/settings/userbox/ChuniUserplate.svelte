<script lang="ts">
    import { DDS } from "../../../libs/userbox/dds"
    import { ddsDB } from "../../../libs/userbox/userbox"

    const DDSreader = new DDS(ddsDB);

    export var chuniLevel: string = "╳"
    export var chuniName: string = "AquaDX"
    export var chuniRating: number = 1.23
    export var chuniNameplate: number = 1
    export var chuniCharacter: number = 0
    export var chuniTrophyName: string = "NEWCOMER"
</script>
{#await DDSreader?.getFile(`nameplate:${chuniNameplate.toString().padStart(8, "0")}`, `nameplate:00000001`) then nameplateURL}
    <!-- svelte-ignore a11y_click_events_have_key_events -->
    <!-- svelte-ignore a11y_no_static_element_interactions -->
    <div on:click class="chuni-nameplate" style:background={`url(${nameplateURL})`}>
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
            <div class="chuni-user-rating">
                RATING
                <span class="chuni-user-rating-number">
                    {chuniRating}
                </span>
            </div>
        </div>
    </div>
{/await}
<style lang="sass">
@use "../../../vars"
.chuni-nameplate
    width: 576px
    height: 228px
    position: relative
    font-size: 16px
    /* Overlap penguin avatar when put side to side */
    z-index: 2 
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
        font-family: sans-serif
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
            font-family: sans-serif
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
            color: #ddf

            .chuni-user-rating-number
                font-size: 1.5em
                margin-left: 10px

</style>
<script lang="ts">
  import { removeImg } from "../../../libs/ui";
    import { DDS } from "../../../libs/userbox/dds"
    import { ddsDB } from "../../../libs/userbox/userbox"

    const DDSreader = new DDS(ddsDB);

    export var chuniWear = 1100001;
    export var chuniHead = 1200001;
    export var chuniFace = 1300001;
    export var chuniSkin = 1400001;
    export var chuniItem = 1500001;
    export var chuniFront = 1600001;
    export var chuniBack = 1700001;
    export var classPassthrough: string = ``;
</script>
<div class="chuni-penguin {classPassthrough}">
    <div class="chuni-penguin-body">
        <!-- Body -->
        {#await DDSreader.getFileFromSheet(`avatarAccessory:${chuniSkin.toString().padStart(8, "0")}`, 0, 0, 256, 400, 0.75) then imageURL}
            <img class="chuni-penguin-skin" src={imageURL} alt="Body" on:error={removeImg}>
        {/await}

        <!-- Face -->
        {#await DDSreader.getFileFromSheet("surfboard:CHU_UI_Common_Avatar_face_00.dds", 0, 0, 225, 150, 0.75) then imageURL}
            <img class="chuni-penguin-eyes chuni-penguin-accessory" src={imageURL} alt="Eyes" on:error={removeImg}>
        {/await}
        {#await DDSreader.getFileFromSheet("surfboard:CHU_UI_Common_Avatar_body_00.dds", 86, 103, 96, 43, 0.75) then imageURL}
            <img class="chuni-penguin-beak chuni-penguin-accessory" src={imageURL} alt="Beak" on:error={removeImg}>
        {/await}

        {#if chuniItem != 1500001}
            <!-- Arms (straight) -->
            {#await DDSreader.getFileFromSheet("surfboard:CHU_UI_Common_Avatar_body_00.dds", 0, 0, 85, 160, 0.75) then imageURL}
                <img class="chuni-penguin-arm-left chuni-penguin-arm" src={imageURL} alt="Left Arm" on:error={removeImg}/>
                <div class="chuni-penguin-arm-left chuni-penguin-arm-type-1 chuni-penguin-arm">
                    {#await DDSreader.getFileFromSheet(`avatarAccessory:${chuniItem.toString().padStart(8, "0")}`, 0, 0, 200, 544, 0.75) then imageURL}
                        <img class="chuni-penguin-item chuni-penguin-accessory chuni-penguin-item-left" src={imageURL} alt="Item" on:error={removeImg}>
                    {/await}
                </div>
            {/await}
            {#await DDSreader.getFileFromSheet("surfboard:CHU_UI_Common_Avatar_body_00.dds", 0, 0, 85, 160, 0.75) then imageURL}
                <img class="chuni-penguin-arm-right chuni-penguin-arm" src={imageURL} alt="Right Arm" on:error={removeImg}>
                <div class="chuni-penguin-arm-right chuni-penguin-arm-type-1 chuni-penguin-arm">
                    {#await DDSreader.getFileFromSheet(`avatarAccessory:${chuniItem.toString().padStart(8, "0")}`, 200, 0, 200, 544, 0.75) then imageURL}
                        <img class="chuni-penguin-item chuni-penguin-accessory chuni-penguin-item-right" src={imageURL} alt="Item" on:error={removeImg}>
                    {/await}
                </div>
            {/await}
        {:else}
            <!-- Arms (bent) -->
            {#await DDSreader.getFileFromSheet("surfboard:CHU_UI_Common_Avatar_body_00.dds", 80, 0, 110, 100, 0.75) then imageURL}
                <img class="chuni-penguin-arm-left chuni-penguin-arm chuni-penguin-arm-type-2" src={imageURL} alt="Left Arm" on:error={removeImg}>
            {/await}
            {#await DDSreader.getFileFromSheet("surfboard:CHU_UI_Common_Avatar_body_00.dds", 80, 0, 110, 100, 0.75) then imageURL}
                <img class="chuni-penguin-arm-right chuni-penguin-arm chuni-penguin-arm-type-2" src={imageURL} alt="Right Arm" on:error={removeImg}>
            {/await}
        {/if}

        <!-- Wear -->
        {#await DDSreader.getFileScaled(`avatarAccessory:${chuniWear.toString().padStart(8, "0")}`, 0.75, `avatarAccessory:01100001`) then imageURL}
            <img class="chuni-penguin-wear chuni-penguin-accessory" src={imageURL} alt="Wear" on:error={removeImg}>
        {/await}

        <!-- Head -->
        {#await DDSreader.getFileScaled(`avatarAccessory:${chuniHead.toString().padStart(8, "0")}`, 0.75, `avatarAccessory:01200001`) then imageURL}
            <img class="chuni-penguin-head chuni-penguin-accessory" src={imageURL} alt="Head" on:error={removeImg}>
        {/await}
        {#if chuniHead == 1200001}
            <!-- If wearing original hat, add the feather -->
            {#await DDSreader.getFileFromSheet("surfboard:CHU_UI_Common_Avatar_body_00.dds", 5, 160, 100, 150, 0.75) then imageURL}
                <img class="chuni-penguin-head-3 chuni-penguin-accessory" src={imageURL} alt="Head3" on:error={removeImg}>
            {/await}
        {/if}
        <!-- Oops, I realized just now that the thing on it's forehead applies to all hats. My mistake! -->
        {#await DDSreader.getFileFromSheet("surfboard:CHU_UI_Common_Avatar_body_00.dds", 105, 153, 56, 58, 0.75) then imageURL}
            <img class="chuni-penguin-head-2 chuni-penguin-accessory" src={imageURL} alt="Head2" on:error={removeImg}>
        {/await}

        <!-- Face (Accessory) -->
        {#await DDSreader.getFileScaled(`avatarAccessory:${chuniFace.toString().padStart(8, "0")}`, 0.75, `avatarAccessory:01300001`) then imageURL}
            <img class="chuni-penguin-face-accessory chuni-penguin-accessory" src={imageURL} alt="Face (Accessory)" on:error={removeImg}>
        {/await}

        <!-- Front -->
        {#await DDSreader.getFileScaled(`avatarAccessory:${chuniFront.toString().padStart(8, "0")}`, 0.75) then imageURL}
            <img class="chuni-penguin-front chuni-penguin-accessory" src={imageURL} alt="Front" on:error={removeImg}>
        {/await}

        <!-- Back -->
        {#await DDSreader.getFileScaled(`avatarAccessory:${chuniBack.toString().padStart(8, "0")}`, 0.75) then imageURL}
            <img class="chuni-penguin-back chuni-penguin-accessory" src={imageURL} alt="Back" on:error={removeImg}>
        {/await}
    </div>
    <div class="chuni-penguin-feet">
        <!-- Feet -->
        {#await DDSreader.getFileFromSheet(`avatarAccessory:${chuniSkin.toString().padStart(8, "0")}`, 0, 410, 85, 80, 0.75) then imageURL}
            <img src={imageURL} alt="Foot" on:error={removeImg}>
        {/await}
        {#await DDSreader.getFileFromSheet(`avatarAccessory:${chuniSkin.toString().padStart(8, "0")}`, 85, 410, 85, 80, 0.75) then imageURL}
            <img src={imageURL} alt="Foot" on:error={removeImg}>
        {/await}
    </div>
</div>
<!-- Truly sorry for the horrors below -->
<style lang="sass">
    @keyframes chuniPenguinBodyBob
        0%
            transform: translate(-50%, 5px) translate(0%, -50%)
        50%
            transform: translate(-50%, 0%) translate(0%, -50%)
        100%
            transform: translate(-50%, 5px) translate(0%, -50%)
    @keyframes chuniPenguinArmLeft
        0%
            transform: translate(-50%, 0) rotate(-2deg)
        50%
            transform: translate(-50%, 0) rotate(2deg)
        100%
            transform: translate(-50%, 0) rotate(-2deg)
    @keyframes chuniPenguinArmRight
        0%
            transform: translate(-50%, 0) scaleX(-1) rotate(-2deg)
        50%
            transform: translate(-50%, 0) scaleX(-1) rotate(2deg)
        100%
            transform: translate(-50%, 0) scaleX(-1) rotate(-2deg)

    img
        -webkit-user-drag: none
        user-select: none

    .chuni-penguin
        height: 512px
        aspect-ratio: 1/2
        position: relative
        pointer-events: none

        z-index: 1

        &.chuni-penguin-float
            position: absolute
            top: 50%
            left: 50%
            transform: translate(-50%, -50%)

        .chuni-penguin-body, .chuni-penguin-feet
            transform: translate(-50%, -50%)
            position: absolute
            left: 50%

        .chuni-penguin-body
            top: 50%
            z-index: 1
            animation: chuniPenguinBodyBob 1s infinite cubic-bezier(0.45, 0, 0.55, 1)
        .chuni-penguin-feet
            top: 80%
            z-index: 0
            width: 175px
            display: flex
            justify-content: center

            img
                margin-left: auto
                margin-right: auto

        .chuni-penguin-arm
            transform-origin: 90% 10%
            position: absolute
            top: 40%
            z-index: 0
            &.chuni-penguin-arm-type-1
                width: calc(85px * 0.75)
                height: calc(160px * 0.75)
                z-index: 2
            &.chuni-penguin-arm-type-2
                transform-origin: 40% 10%
                z-index: 2

            &.chuni-penguin-arm-left
                left: 0%
                transform: translate(-50%, 0)
                animation: chuniPenguinArmLeft 1s infinite cubic-bezier(0.45, 0, 0.55, 1)
                &.chuni-penguin-arm-type-2
                    left: 15%
            &.chuni-penguin-arm-right
                left: 72.5%
                transform: translate(-50%, 0) scaleX(-1)
                animation: chuniPenguinArmRight 1s infinite cubic-bezier(0.45, 0, 0.55, 1)
                &.chuni-penguin-arm-type-2
                    left: 95%

        .chuni-penguin-accessory
            transform: translate(-50%, -50%)
            position: absolute
            top: 50%
            left: 50%

        .chuni-penguin-item
            z-index: 1
            top: 25%
            left: 0

            &.chuni-penguin-item-left
                transform: translate(-50%, -50%) rotate(-15deg)
            &.chuni-penguin-item-right
                transform: translate(-50%, -50%) scaleX(-1) rotate(15deg)

        .chuni-penguin-eyes
            top: 22.5%
        .chuni-penguin-beak
            top: 29.5%
        .chuni-penguin-wear
            top: 60%
        .chuni-penguin-head
            top: 7.5%
            z-index: 10
        .chuni-penguin-head-2
            top: 13.5%
        .chuni-penguin-head-3
            top: -12.5%
        .chuni-penguin-face-accessory
            top: 27.5%
        .chuni-penguin-back
            z-index: -1

</style>

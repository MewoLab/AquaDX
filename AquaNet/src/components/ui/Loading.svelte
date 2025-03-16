<script lang="ts">
  import Icon from '@iconify/svelte';
  import { fade } from 'svelte/transition'
</script>

<div class="overlay loading" transition:fade>
  <Icon class="icon" icon="svg-spinners:pulse-2"/>
  <span><span>LOADING</span></span>
</div>

<style lang="sass">
.loading.overlay
  font-size: 28rem

  :global(.icon)
    opacity: 0.5

  > span
    position: absolute
    inset: 0
    display: flex
    justify-content: center
    align-items: center
    background: transparent

    letter-spacing: 20px
    margin-left: 20px

    font-size: 1.5rem
</style>

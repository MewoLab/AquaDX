<script lang="ts">
  import { onMount, onDestroy } from 'svelte'

  export let triggeredBy: string
  export let loading: boolean = false
  let isHovered = false
  let x: number, y: number
  let targets: Element[] = []

  onMount(() => {
    targets = [...document.querySelectorAll(triggeredBy)]
    targets.forEach((el) => {
      el.addEventListener('mouseover', mouseOver)
      el.addEventListener('mousemove', mouseMove)
      el.addEventListener('mouseleave', mouseLeave)
    })
    if (targets.length === 0) {
      console.warn(`No elements found with selector "${triggeredBy}"`)
    }
  })

  onDestroy(() => {
    targets.forEach((el) => {
      el.removeEventListener('mouseover', mouseOver)
      el.removeEventListener('mousemove', mouseMove)
      el.removeEventListener('mouseleave', mouseLeave)
    })
  })

  function mouseOver(event: MouseEvent) {
    console.log('over')
    isHovered = true
    updatePosition(event)
  }

  function mouseMove(event: MouseEvent) {
    console.log('move')
    updatePosition(event)
  }

  function updatePosition(event: MouseEvent) {
    x = event.pageX + 5
    y = event.pageY + 20
  }

  function mouseLeave() {
    console.log('leave')
    isHovered = false
  }
</script>

{#if isHovered}
  <div style="top: {y}px; left: {x}px" class="tooltip" class:loading>
    <slot />
  </div>
{/if}

<style lang="sass">
  @use "../vars"

  .tooltip
    position: absolute
    z-index: 900
    background: white
    padding: 10px 16px
    border-radius: vars.$border-radius
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1)
    pointer-events: none
    white-space: nowrap
    color: #242424
    transform: translate(-50%, 0)
    transition: opacity 0.2s

    &.loading
      opacity: 0
</style>

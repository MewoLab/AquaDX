<script lang="ts">
  import { createEventDispatcher } from 'svelte'

  export let page: number
  export let totalPages: number

  const dispatch = createEventDispatcher()

  let editing = false
  let inputPage: number

  function updatePage(newPage: number) {
    if (newPage > 0 && newPage <= totalPages) dispatch('updatePage', newPage)
  }

  function startEditing() {
    inputPage = page
    editing = true
  }

  function finishEditing() {
    editing = false
    if (inputPage !== page) updatePage(inputPage)
  }

  function handleKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') finishEditing()
    else if (event.key === 'Escape') editing = false
  }
</script>

<div class="pagination">
  <button on:click={() => updatePage(page - 1)} disabled={page <= 1}>Previous</button>

  {#if editing}
    <input bind:value={inputPage} on:blur={finishEditing} on:keydown={handleKeydown} min="1" max={totalPages} autofocus/>
  {:else}
    <span on:click={startEditing} role="button" tabindex="0" on:keydown={(e) => e.key === 'Enter' && startEditing()}>
      Page {page} of {totalPages}
    </span>
  {/if}

  <button on:click={() => updatePage(page + 1)} disabled={page >= totalPages}>Next</button>
</div>

<style lang="sass">
  .pagination
    display: flex
    justify-content: center
    align-items: center
    margin: 1rem 0
    gap: 1rem

  input
    width: 100px
    text-align: center

  span[role="button"]
    cursor: pointer
</style>

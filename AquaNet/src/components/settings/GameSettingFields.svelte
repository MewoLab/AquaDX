<script lang="ts">
  import { slide } from "svelte/transition";
  import { SETTING } from "../../libs/sdk";
  import type { GameOption } from "../../libs/generalTypes";
  import { ts } from "../../libs/i18n";
  import StatusOverlays from "../StatusOverlays.svelte";
  import InputWithButton from "../ui/InputWithButton.svelte";
  import InputField from "../ui/InputField.svelte";

  export let game: string;
  let gameFields: GameOption[] = []
  let submitting = ""
  let error: string;

  SETTING.get().then(s => {
    gameFields = s.filter(it => it.game === game)
  })

  async function submitGameOption(field: string, value: any) {
    if (submitting) return false
    submitting = field

    await SETTING.set(field, value).catch(e => error = e.message).finally(() => submitting = "")
    return true
  }
</script>

<div class="fields">
  {#each gameFields as field}
    <InputField field={field} callback={() => submitGameOption(field.key, field.value)}/>
  {/each}
</div>

<StatusOverlays {error} loading={!gameFields.length || !!submitting}/>

<style lang="sass">
  .fields
    display: flex
    flex-direction: column
    gap: 12px
</style>

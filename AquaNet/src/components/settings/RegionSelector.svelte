<script lang="ts">
  import { USER} from "../../libs/sdk";
  import { ts } from "../../libs/i18n";
  import StatusOverlays from "../StatusOverlays.svelte";
  let regionId = 0;
  let submitting = ""
  let error: string;

  const prefectures = ["None","Aichi","Aomori","Akita","Ishikawa","Ibaraki","Iwate","Ehime","Oita","Osaka","Okayama","Okinawa","Kagawa","Kagoshima","Kanagawa","Gifu","Kyoto","Kumamoto","Gunma","Kochi","Saitama","Saga","Shiga","Shizuoka","Shimane","Chiba","Tokyo","Tokushima","Tochigi","Tottori","Toyama","Nagasaki","Nagano","Nara","Niigata","Hyogo","Hiroshima","Fukui","Fukuoka","Fukushima","Hokkaido","Mie","Miyagi","Miyazaki","Yamagata","Yamaguchi","Yamanashi","Wakayama"]

  USER.me().then(user => {
    const parsedRegion = parseInt(user.region);
    if (!isNaN(parsedRegion) && parsedRegion > 0) {
      regionId = parsedRegion - 1;
    } else {
      regionId = 0;
    }
  })

  async function saveNewRegion() {
    if (submitting) return false
    submitting = "region"

    await USER.changeRegion(regionId+1).catch(e => error = e.message).finally(() => submitting = "")
    return true
  }
</script>

<div class="fields">
  <label for="rounding">
    <span class="name">{ts(`settings.regionSelector.title`)}</span>
    <span class="desc">{ts(`settings.regionSelector.desc`)}</span>
  </label>
  <select bind:value={regionId} on:change={saveNewRegion}>
    <option value={0} disabled selected>{ts("settings.regionSelector.select")}</option>
    {#each prefectures.filter(p=>p!=="None") as prefecture, index}
      <option value={index}>{prefecture}</option>
    {/each}
  </select>
</div>

<StatusOverlays {error} loading={!!submitting}/>

<style lang="sass">
  @use "../../vars"

  .fields
    display: flex
    flex-direction: column
    gap: 12px

  label
    display: flex
    flex-direction: column

    .desc
      opacity: 0.6

</style>

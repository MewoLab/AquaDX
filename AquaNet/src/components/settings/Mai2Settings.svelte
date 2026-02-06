<script lang="ts">
  import { slide, fade } from "svelte/transition";
  import { FADE_IN, FADE_OUT, DATA_HOST } from "../../libs/config";
  import { t } from "../../libs/i18n.js";
  import Icon from "@iconify/svelte";
  import StatusOverlays from "../StatusOverlays.svelte";
  import { GAME } from "../../libs/sdk";
  import GameSettingFields from "./GameSettingFields.svelte";
  import { download } from "../../libs/ui";

  const profileFields = [
    ['name', t('settings.mai2.name')],
  ]

  export let username: string;
  let error: string
  let submitting = ""
  let values = Array(profileFields.length).fill('')
  let changed: string[] = []

  GAME.userSummary(username, 'mai2').then(({name}) => {
    values = [name]
  }).catch(e => error = e.message)

  function submit(field: string, value: string) {
    if (submitting) return
    submitting = field

    switch (field) {
      case 'name':
        GAME.changeName('mai2', value).then(({newName}) => {
          changed = changed.filter(c => c !== field)
          values = [newName]
        }).catch(e => error = e.message).finally(() => submitting = "")
        break
    }
  }
  async function exportBatchManual() {
    submitting = "batchExport"
    const DIFFICULTY_MAP: Record<number, string> = {
      0: "Basic",
      1: "Advanced",
      2: "Expert",
      3: "Master",
      4: "Re:Master"
    }

    const DAN_MAP: Record<number, string> = {
      1: "DAN_1",
      2: "DAN_2",
      3: "DAN_3",
      4: "DAN_4",
      5: "DAN_5",
      6: "DAN_6",
      7: "DAN_7",
      8: "DAN_8",
      9: "DAN_9",
      10: "DAN_10",
      11: "SHINDAN_1",
      12: "SHINDAN_2",
      13: "SHINDAN_3",
      14: "SHINDAN_4",
      15: "SHINDAN_5",
      16: "SHINDAN_6",
      17: "SHINDAN_7",
      18: "SHINDAN_8",
      19: "SHINDAN_9",
      20: "SHINDAN_10",
      21: "SHINKAIDEN",
      22: "URAKAIDEN"
    }

    const CLASS_MAP: Record<number, string> = {
      0: "B5",
      1: "B4",
      2: "B3",
      3: "B2",
      4: "B1",
      5: "A5",
      6: "A4",
      7: "A3",
      8: "A2",
      9: "A1",
      10: "S5",
      11: "S4",
      12: "S3",
      13: "S2",
      14: "S1",
      15: "SS5",
      16: "SS4",
      17: "SS3",
      18: "SS2",
      19: "SS1",
      20: "SSS5",
      21: "SSS4",
      22: "SSS3",
      23: "SSS2",
      24: "SSS1",
      25: "LEGEND"
    }

    let data: any
    let musicData: any
    let output: any = {
      "meta": {
        "game": "maimaidx",
        "playtype": "Single",
        "service": "AquaDX-Manual"
      },
      "scores": [],
      "classes": {}
    }
    try {
      musicData = await fetch(`${DATA_HOST}/d/mai2/00/all-music.json`).then(res => res.json())
    } catch (e) {
      error = e.message;
      submitting = ""
      return;
    }
    try {
      data = await GAME.export('mai2');
    } catch (e) {
      error = e.message;
      submitting = ""
      return;
    }
    if (data && "userPlaylogList" in data) {
      for (let score of data.userPlaylogList) {
        if(score.musicId > 100000){
          continue; // UTAGE charts are not supported
        }
        const musicItem = musicData[score.musicId as string];
        if (!musicItem) continue;
        let difficulty = null;

        if (!(score.level in DIFFICULTY_MAP))
          continue;

        const isDX = score.musicId >= 10000;
        difficulty = isDX ? `DX ${DIFFICULTY_MAP[score.level]}` : DIFFICULTY_MAP[score.level];

        const percent = score.achievement/10000;

        const pcrit = score.tapCriticalPerfect + score.holdCriticalPerfect + score.slideCriticalPerfect + score.touchCriticalPerfect + score.breakCriticalPerfect;
        const perfect = score.tapPerfect + score.holdPerfect + score.slidePerfect + score.touchPerfect + score.breakPerfect;
        const great = score.tapGreat + score.holdGreat + score.slideGreat + score.touchGreat + score.breakGreat;
        const good = score.tapGood + score.holdGood + score.slideGood + score.touchGood + score.breakGood;
        const miss = score.tapMiss + score.holdMiss + score.slideMiss + score.touchMiss + score.breakMiss;
        const judgements  = {
          "pcrit": pcrit,
          "perfect": perfect,
          "great": great,
          "good": good,
          "miss": miss
        }
        let lamp = null;
        if (score.isAllPerfect) {
          lamp = "ALL PERFECT";
          if (score.percent == 101.0) {
            lamp = "ALL PERFECT+";
          }
        } else if (score.isFullCombo) {
          lamp = "FULL COMBO";
          if (good == 0 && great == 0) {
            lamp = "FULL COMBO+";
          }
        } else if (score.isClear) {
          lamp = "CLEAR";
        } else {
          lamp = "FAILED";
        }

        const optional = {
          "fast": score.fastCount,
          "slow": score.lateCount,
          "maxCombo": score.maxCombo
        }

        output.scores.push({
          "percent": percent,
          "lamp": lamp,
          "matchType": "inGameID",
          "identifier": score.musicId.toString(),
          "difficulty": difficulty,
          "timeAchieved": new Date(score.userPlayDate).getTime(),
          "judgements": judgements,
          "optional": optional
        })
      }
    }

    if(data.userData.courseRank in DAN_MAP){
      output.classes["dan"] = DAN_MAP[data.userData.courseRank]
    }
    if(data.userData.classRank in CLASS_MAP){
      output.classes["matchingClass"] = CLASS_MAP[data.userData.classRank]
    }
    download(JSON.stringify(output), `AquaDX_maimai2_BatchManualExport_${values[0]}.json`)
    submitting = ""
  }

  function exportData() {
    submitting = "export"
    GAME.export('mai2')
      .then(data => download(JSON.stringify(data), `AquaDX_maimai2_export_${values[0]}.json`))
      .catch(e => error = e.message)
      .finally(() => submitting = "")
  }
</script>

<div class="fields" out:fade={FADE_OUT} in:fade={FADE_IN}>
  {#each profileFields as [field, name], i (field)}
    <div class="field">
      <label for={field}>{name}</label>
      <div>
        <input id={field} type="text"
               bind:value={values[i]} on:input={() => changed = [...changed, field]}
               placeholder={field === 'password' ? t('settings.profile.unchanged') : t('settings.profile.unset')}/>
        {#if changed.includes(field) && values[i]}
          <button transition:slide={{axis: 'x'}} on:click={() => submit(field, values[i])}>
            {#if submitting === field}
              <Icon icon="line-md:loading-twotone-loop"/>
            {:else}
              {t('settings.profile.save')}
            {/if}
          </button>
        {/if}
      </div>
    </div>
  {/each}
  <GameSettingFields game="mai2"/>
  <button class="exportButton" on:click={exportData}>
    <Icon icon="bxs:file-export"/>
    {t('settings.export')}
  </button>
  <button class="exportBatchManualButton" on:click={exportBatchManual}>
    <Icon icon="bxs:file-export"/>
    {t('settings.batchManualExport')}
  </button>
</div>

<StatusOverlays {error} loading={!values[0] || !!submitting}/>

<style lang="sass">
  .fields
    display: flex
    flex-direction: column
    gap: 12px

  .field
    display: flex
    flex-direction: column

    label
      max-width: max-content

    > div:not(.bool)
      display: flex
      align-items: center
      gap: 1rem
      margin-top: 0.5rem

      > input
        flex: 1

  .exportButton
    display: flex
    justify-content: center
    align-items: center
    gap: 5px
</style>

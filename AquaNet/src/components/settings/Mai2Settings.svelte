<script lang="ts">
  import { slide, fade } from "svelte/transition";
  import { FADE_IN, FADE_OUT, DATA_HOST } from "../../libs/config";
  import { t } from "../../libs/i18n.js";
  import Icon from "@iconify/svelte";
  import StatusOverlays from "../StatusOverlays.svelte";
  import { GAME, SETTING, USER } from "../../libs/sdk";
  import GameSettingFields from "./GameSettingFields.svelte";
  import { download } from "../../libs/ui";
  import UserOptionSlider from "./UserOptionSlider.svelte";
  import type { GameUserOption } from "../../libs/generalTypes";
  import InputField from "../ui/InputField.svelte";

  let userNameField: any

  let error: string
  let submitting = ""
  let loading = true
  let changed: string[] = []

  let userOptions: GameUserOption = {}
  let userIsUsingPreset = false

  USER.me().then(me =>
    GAME.userSummary(me.username, 'mai2').then(async ({name}) => {
      userNameField = {key: "gameUsername", value: name, type: "String"}

      userOptions = await SETTING.optionGet('mai2').catch(_ => {
        error = t("userbox.error.nodata")
        loading = true
      }) as GameUserOption
      if (userOptions["optionKind"] != 3)
        userIsUsingPreset = true
      loading = false
    }).catch(e => error = e.message));

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
    } catch (e: any) {
      error = e.message;
      submitting = ""
      return;
    }
    try {
      data = await GAME.export('mai2');
    } catch (e: any) {
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
          if (percent >= 101.0) {
            lamp = "ALL PERFECT+";
          }
        } else if (score.isFullCombo) {
          lamp = "FULL COMBO";
          if (!good) {
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
    download(JSON.stringify(output), `AquaDX_maimai2_BatchManualExport_${userNameField.value}.json`)
    submitting = ""
  }

  function exportData() {
    submitting = "export"
    GAME.export('mai2')
      .then(data => download(JSON.stringify(data), `AquaDX_maimai2_export_${userNameField.value}.json`))
      .catch(e => error = e.message)
      .finally(() => submitting = "")
  }
</script>

{#if !loading}
<div class="fields">
  <InputField bind:field={userNameField}
    callback={() => GAME.changeName('mai2', userNameField.value)}/>
  {#if userIsUsingPreset}
    <blockquote class="info">
      {t('settings.options.all.preset-warning')}
    </blockquote>
  {/if}
  <div class="fields-ranges">
    <!-- TODO: determine min & max -->
    <UserOptionSlider
      game="mai2" type="noteSpeed"
      minValue={0} maxValue={37}
      defaultValue={userOptions["noteSpeed"]}
      getTextFunction={(value: number) => value >= 37 ? "SONIC" : `${(value / 4) + 1}`}
    />
    <UserOptionSlider
      game="mai2" type="touchSpeed"
      minValue={0} maxValue={37}
      defaultValue={userOptions["touchSpeed"]}
      getTextFunction={(value: number) => value >= 37 ? "SONIC" : `${(value / 4) + 1}`}
    />
    <UserOptionSlider
      game="mai2" type="slideSpeed"
      minValue={0} maxValue={20}
      defaultValue={userOptions["slideSpeed"]}
      getTextFunction={(value: number) => value == 10 ? "Normal" : `${value > 10 ? "Late" : "Fast"} ${(Math.abs(value - 10) / 10).toFixed(1)}`}
    />
    <UserOptionSlider
      game="mai2" type="headPhoneVolume"
      minValue={0} maxValue={19}
      defaultValue={userOptions["headPhoneVolume"]}
      getTextFunction={(value: number) => `${((value / 19) * 100).toFixed(0)}%`}
    />
    <UserOptionSlider
      game="mai2" type="trackSkip"
      minValue={0} maxValue={11}
      defaultValue={userOptions["trackSkip"]}
      getTextFunction={(value: number) => [
        t('settings.options.all.none'),
        t('settings.options.all.push'),
        `S`, `SS`, `SSS`,
        t('settings.options.all.personal-best'),
        t('settings.options.all.rival-score'),
        `${t('settings.options.all.life')} 300`,
        `${t('settings.options.all.life')} 100`,
        `${t('settings.options.all.life')} 50`,
        `${t('settings.options.all.life')} 10`,
        `${t('settings.options.all.life')} 1`
      ][value]}
    />
  </div>
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
{/if}

<StatusOverlays {error} loading={!userNameField || !!submitting || loading}/>

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

  .fields-ranges
    display: flex
    flex-wrap: wrap
    margin: 0.5rem 0
    gap: 0 1rem
    justify-content: center

    :global(.field)
      flex: calc(50% - 1rem)
      width: 50%

  .exportButton
    display: flex
    justify-content: center
    align-items: center
    gap: 5px
</style>

<script lang="ts">
  import { fade } from "svelte/transition"
  import { t } from "../../libs/i18n";
  import ActionCard from "../../components/ActionCard.svelte";
  import { CARD, GAME, USER } from "../../libs/sdk";

  export let username: string;
  let shouldShow = navigator.language.startsWith('zh');
  let showWarning = false;
  let isCardBindIssue = false;

  if (!shouldShow) {
    fetch('https://47.122.72.135/ip/isChina')
      .then(it => it.json())
      .then(it => shouldShow = it)
      .catch(() => shouldShow = false);
  }

  CARD.userGames(username).then(games => {
    if (!Object.values(games).some(it => it)) {
      isCardBindIssue = true;
    }
  })

  const handleClick = () => {
    if (isCardBindIssue) {
      showWarning = true;
      return
    }
    jump()
  }

  const jump = () => {
    const token = localStorage.getItem('token')
    location.href = `https://portal.mumur.net/migrateFromAquaDx/${token}`
  }
</script>

{#if shouldShow}
    <ActionCard color="190, 149, 255" icon="system-uicons:jump-up" on:click={handleClick}>
        <h3>迁移到 MuNET</h3>
        <span>更适合中国宝宝体质的服务器，AquaDX 的继任者。点击查看详情</span>
    </ActionCard>
{/if}

{#if showWarning}
    <div class="overlay" transition:fade>
        <div>
            <h2>提示</h2>
            <p>看起来你在 AquaDX 还没有游戏数据，也许是因为没有绑卡或者绑定的卡不是在游戏中点击“查看卡号”获取的…</p>
            <p>现在迁移的话，大概会导致你的游戏数据无法被正确的迁移。建议你先去检查一下吧</p>
            <div class="buttons">
                <button on:click={() => showWarning = false}>{t('action.cancel')}</button>
                <button on:click={jump}>继续</button>
            </div>
        </div>
    </div>
{/if}

<style lang="sass">
  h3
    font-size: 1.3rem
    margin: 0

  .buttons
    display: grid
    grid-template-columns: 1fr 1fr
    gap: 1rem
</style>

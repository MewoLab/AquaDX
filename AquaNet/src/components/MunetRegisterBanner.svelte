<script lang="ts">
  export let username: string;
  export let email: string;

  let shouldShow = navigator.language.startsWith('zh');

  // 会导致瞬间出现，但是不知道为什么 svelte 的 transition 动画不工作
  // if (!shouldShow) {
  //   fetch('https://47.122.72.135/ip/isChina')
  //     .then(it => it.json())
  //     .then(it => shouldShow = it)
  //     .catch(() => shouldShow = false);
  // }

  const jump = () => {
    const params = new URLSearchParams();
    if (username) params.set('username', username);
    if (email) params.set('email', email);
    location.href = `https://portal.mumur.net/register?${params.toString()}`;
  }
</script>

{#if shouldShow}
    <div class="cursor-pointer" on:click={jump}>
        <h2>MuNET 了解一下！</h2>
        <div>
            <p>MuNET 是 AquaDX 的继任者，提供更适合中国用户的服务器和更好的游戏体验。</p>
            <p>如果你还没有游戏数据，建议在 MuNET 上创建账号并开始游戏。点击立即前往</p>
        </div>
    </div>
{/if}


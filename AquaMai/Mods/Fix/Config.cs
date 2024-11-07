using AquaMai.Attributes;

namespace AquaMai.Mods.Fix;

public class Config
{
    [ConfigComment(
        en: "Allow login with higher data version",
        zh: """
            原先如果你的账号版本比当前游戏设定的版本高的话，就会不能登录
            开了这个选项之后就可以登录了，不过你的账号版本还是会被设定为当前游戏的版本
            """)]
    public bool SkipVersionCheck { get; set; }

    [ConfigComment(
        zh: """
            如果你在用未经修改的客户端，会默认加密到服务器的连接，而连接私服的时候不应该加密
            开了这个选项之后就不会加密连接了，同时也会移除不同版本的客户端可能会对 API 接口加的后缀
            正常情况下，请保持这个选项开启
            """)]
    public bool RemoveEncryption { get; set; }

    [ConfigComment(
        zh: "如果要配置店内招募的话，应该要把这个关闭")]
    public bool ForceAsServer { get; set; } = true;

    [ConfigComment(
        en: "Force the game to be in FreePlay mode",
        zh: "强制改为免费游玩（FreePlay）")]
    public bool ForceFreePlay { get; set; } = true;

    [ConfigComment(
        en: "Force the game to be in PaidPlay mode with 24 coins locked, conflicts with ForceFreePlay",
        zh: "强制付费游玩并锁定 24 个币，和 ForceFreePlay 冲突")]
    public bool ForcePaidPlay { get; set; }

    [ConfigComment(
        en: "Add notes sprite to the pool to prevent use up",
        zh: "增加更多待命的音符贴图，防止奇怪的自制谱用完音符贴图池")]
    public int ExtendNotesPool { get; set; }

    [ConfigComment(
        en: "Force the frame rate limit to 60 FPS and disable vSync. Do not use if your game has no issues",
        zh: "强制设置帧率上限为 60 帧并关闭垂直同步。如果你的游戏没有问题，请不要使用")]
    public bool FrameRateLock { get; set; }

    [ConfigComment(
        en: """
            Use Microsoft YaHei Bold to display characters not in the font library
            Cannot be used together with CustomFont
            """,
        zh: """
            在显示字库里没有的字时使用微软雅黑 Bold 显示
            不可以和 CustomFont 一起使用
            """)]
    public bool FontFix { get; set; }

    [ConfigComment(
        en: """
            Make the AutoPlay random judgment mode really randomize all judgments (down to sub-judgments)
            The original random judgment will only produce all 15 judgment results from Miss(TooFast) ~ Critical ~ Miss(TooLate)
            Here, it is changed to a triangular distribution to produce all 15 judgment results from Miss(TooFast) ~ Critical ~ Miss(TooLate)
            Of course, it will not consider whether the original Note really has a corresponding judgment (such as Slide should not have non-Critical Prefect)
            """,
        zh: """
            让 AutoPlay 的随机判定模式真的会随机产生所有的判定 (精确到子判定)
            原本的随机判定只会等概率产生 Critical, LateGreat1st, LateGood, Miss(TooLate)
            这里改成三角分布产生从 Miss(TooFast) ~ Critical ~ Miss(TooLate) 的所有 15 种判定结果
            当然, 此处并不会考虑原本那个 Note 是不是真的有对应的判定 (比如 Slide 实际上不应该有小 p 之类的)
            """)]
    public bool RealisticRandomJudge { get; set; }

    [ConfigComment(
        en: "Cannot be used together with HideHanabi",
        zh: """
            修复 1p 模式下的烟花大小
            不能和 HideHanabi 一起使用
            """)]
    public bool HanabiFix { get; set; }

    [ConfigComment(
        en: "Prevent gray network caused by mistakenly thinking it's an AimeDB server issue",
        zh: "防止因错误认为 AimeDB 服务器问题引起的灰网，建议开启")]
    public bool IgnoreAimeServerError { get; set; }
}

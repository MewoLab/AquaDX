using System;

namespace AquaMai.Attributes;

[AttributeUsage(AttributeTargets.Class)]
public class ConfigSectionAttribute(string en = null, string zh = null, bool defaultOn = false, bool hideInExample = false) : Attribute
{
    public string CommentEn { get; } = en;
    public string CommentZh { get; } = zh;
    public bool DefaultOn { get; } = defaultOn;
    public bool HideInExample { get; } = hideInExample;
}

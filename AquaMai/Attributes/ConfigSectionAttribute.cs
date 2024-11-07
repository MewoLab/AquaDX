using System;

namespace AquaMai.Attributes;

[AttributeUsage(AttributeTargets.Class)]
public class ConfigSection(string en = null, string zh = null, bool defaultOn = false) : Attribute
{
    public string CommentEn { get; } = en;
    public string CommentZh { get; } = zh;
    public bool DefaultOn { get; } = defaultOn;
}

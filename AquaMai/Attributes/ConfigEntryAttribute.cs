using System;

namespace AquaMai.Attributes;

[AttributeUsage(AttributeTargets.Field)]
public class ConfigEntryAttribute(string en = null, string zh = null, object defaultValue = null) : Attribute
{
    public string CommentEn { get; } = en;
    public string CommentZh { get; } = zh;
    public object DefaultValue { get; } = defaultValue;
}

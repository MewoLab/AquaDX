using System;

namespace AquaMai.Config.Attributes;

[AttributeUsage(AttributeTargets.Field)]
public class ConfigEntryAttribute(string en = null, string zh = null) : Attribute
{
    public ConfigComment Comment { get; } = new ConfigComment(en, zh);
}

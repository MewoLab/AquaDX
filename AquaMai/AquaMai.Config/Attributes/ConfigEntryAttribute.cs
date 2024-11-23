using System;

namespace AquaMai.Config.Attributes;

public enum SpecialConfigEntry
{
    None,
    Locale
}

[AttributeUsage(AttributeTargets.Field)]
public class ConfigEntryAttribute(
    string en = null,
    string zh = null,
    // NOTE: Don't use this argument to hide any useful options.
    //       Only use it to hide options that really won't be used.
    bool hideWhenDefault = false,
    // NOTE: Use this argument to mark special config entries that need special handling.
    SpecialConfigEntry specialConfigEntry = SpecialConfigEntry.None) : Attribute
{
    public ConfigComment Comment { get; } = new ConfigComment(en, zh);
    public bool HideWhenDefault { get; } = hideWhenDefault;
    public SpecialConfigEntry SpecialConfigEntry { get; } = specialConfigEntry;
}

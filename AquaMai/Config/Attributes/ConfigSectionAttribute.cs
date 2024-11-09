using System;

namespace AquaMai.Config.Attributes;

public enum ConfigSectionExample
{
    Shown,
    HiddenDefaultOff,
    HiddenDefaultOn
}

[AttributeUsage(AttributeTargets.Class)]
public class ConfigSectionAttribute(string en = null, string zh = null, ConfigSectionExample example = ConfigSectionExample.Shown) : Attribute
{
    public ConfigComment Comment { get; } = new ConfigComment(en, zh);
    public ConfigSectionExample Example { get; } = example;
}

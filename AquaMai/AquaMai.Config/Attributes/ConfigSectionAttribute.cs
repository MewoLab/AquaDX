using System;

namespace AquaMai.Config.Attributes;

[AttributeUsage(AttributeTargets.Class)]
public class ConfigSectionAttribute(string en = null, string zh = null, bool exampleHidden = false, bool defaultOn = false) : Attribute
{
    public ConfigComment Comment { get; } = new ConfigComment(en, zh);
    public bool ExampleHidden { get; } = exampleHidden;
    public bool DefaultOn { get; } = defaultOn;
}

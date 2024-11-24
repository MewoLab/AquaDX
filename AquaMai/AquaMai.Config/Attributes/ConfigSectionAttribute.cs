using System;

namespace AquaMai.Config.Attributes;

[AttributeUsage(AttributeTargets.Class)]
public class ConfigSectionAttribute(
    string en = null,
    string zh = null,
    // It will be hidden if the default value is preserved.
    bool exampleHidden = false,
    // A "Disabled = true" entry is required to disable the section.
    bool defaultOn = false,
    // NOTE: You probably shouldn't use this. Only the "General" section is using this.
    //       Implies defaultOn = true.
    bool alwaysEnabled = false) : Attribute
{
    public ConfigComment Comment { get; } = new ConfigComment(en, zh);
    public bool ExampleHidden { get; } = exampleHidden;
    public bool DefaultOn { get; } = defaultOn || alwaysEnabled;
    public bool AlwaysEnabled { get; } = alwaysEnabled;
}

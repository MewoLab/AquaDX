using System;
using System.Collections.Generic;
using AquaMai.Config.Reflection;

namespace AquaMai.Config;

public static class ConfigState
{
    // NOTE: If a section's state is default, all underlying entries' states are default as well.

    public record SectionState
    {
        public bool IsDefault { get; set; }
        public bool DefaultEnabled { get; init; }
        public bool Enabled { get; set; }
    }

    public record EntryState
    {
        public bool IsDefault { get; set; }
        public object DefaultValue { get; init; }
        public object Value { get; set; }
    }

    private static readonly Dictionary<string, SectionState> sections = new(StringComparer.OrdinalIgnoreCase);
    private static readonly Dictionary<string, EntryState> entries = new(StringComparer.OrdinalIgnoreCase);

    public static void InitializeSection(ReflectionManager.Section section)
    {
        sections.Add(section.Path, new SectionState()
        {
            IsDefault = true,
            DefaultEnabled = section.Attribute.Example == Attributes.ConfigSectionExample.HiddenDefaultOn,
            Enabled = section.Attribute.Example == Attributes.ConfigSectionExample.HiddenDefaultOn
        });

        foreach (var entry in section.Entries)
        {
            entries.Add(entry.Path, new EntryState()
            {
                IsDefault = true,
                DefaultValue = entry.Field.GetValue(null),
                Value = entry.Field.GetValue(null)
            });
        }
    }

    public static SectionState GetSectionState(ReflectionManager.Section section)
    {
        return sections[section.Path];
    }

    public static SectionState GetSectionState(Type type)
    {
        if (!ReflectionManager.TryGetSection(type, out var section))
        {
            throw new ArgumentException($"Type {type.FullName} is not a config section.");
        }
        return sections[section.Path];
    }

    public static void SetSectionEnabled(ReflectionManager.Section section, bool enabled)
    {
        sections[section.Path].IsDefault = false;
        sections[section.Path].Enabled = enabled;
    }

    public static EntryState GetEntryState(ReflectionManager.Entry entry)
    {
        return entries[entry.Path];
    }

    public static void SetEntryValue(ReflectionManager.Entry entry, object value)
    {
        entry.Field.SetValue(null, value);
        entries[entry.Path].IsDefault = false;
        entries[entry.Path].Value = value;
    }
}

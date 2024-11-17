using System.Reflection;
using System.Linq;
using System.Collections.Generic;
using AquaMai.Config.Attributes;
using AquaMai.Config.Interfaces;
using System;

namespace AquaMai.Config.Reflection;

public class ReflectionManager : IReflectionManager
{
    public record Entry : IReflectionManager.IEntry
    {
        public string Path { get; init; }
        public string Name { get; init; }
        public IReflectionField Field { get; init; }
        public ConfigEntryAttribute Attribute { get; init; }
    }

    public record Section : IReflectionManager.ISection
    {
        public string Path { get; init; }
        public IReflectionType Type { get; init; }
        public ConfigSectionAttribute Attribute { get; init; }
        public List<Entry> entries;
        public List<IReflectionManager.IEntry> Entries => entries.Cast<IReflectionManager.IEntry>().ToList();
    }

    private readonly Dictionary<string, Section> sections = new(StringComparer.OrdinalIgnoreCase);
    private readonly Dictionary<string, Entry> entries = new(StringComparer.OrdinalIgnoreCase);
    private readonly Dictionary<string, Section> sectionsByFullName = [];

    public ReflectionManager(IReflectionProvider reflectionProvider)
    {
        var prefix = "AquaMai.Mods.";
        var types = reflectionProvider.GetTypes().Where(t => t.FullName.StartsWith(prefix));
        var collapsedNamespaces = new HashSet<string>();
        foreach (var type in types)
        {
            var sectionAttribute = type.GetCustomAttribute<ConfigSectionAttribute>();
            if (sectionAttribute == null) continue;
            if (collapsedNamespaces.Contains(type.Namespace))
            {
                throw new Exception($"Collapsed namespace {type.Namespace} contains multiple sections");
            }
            var path = type.FullName.Substring(prefix.Length);
            if (type.GetCustomAttribute<ConfigCollapseNamespaceAttribute>() != null)
            {
                var separated = path.Split('.');
                if (separated[separated.Length - 2] != separated[separated.Length - 1])
                {
                    throw new Exception($"Type {type.FullName} is not collapsable");
                }
                path = string.Join(".", separated.Take(separated.Length - 1));
                collapsedNamespaces.Add(type.Namespace);
            }

            var sectionEntries = new List<Entry>();
            foreach (var field in type.GetFields(BindingFlags.Static | BindingFlags.Public | BindingFlags.NonPublic))
            {
                var entryAttribute = field.GetCustomAttribute<ConfigEntryAttribute>();
                if (entryAttribute == null) continue;
                var entryPath = $"{path}.{field.Name}";
                var entry = new Entry()
                {
                    Path = entryPath,
                    Name = field.Name,
                    Field = field,
                    Attribute = entryAttribute
                };
                sectionEntries.Add(entry);
                entries.Add(entryPath, entry);
            }

            var section = new Section()
            {
                Path = path,
                Type = type,
                Attribute = sectionAttribute,
                entries = sectionEntries
            };
            sections.Add(path, section);
            sectionsByFullName.Add(type.FullName, section);
        }

        var order = reflectionProvider.GetEnum("AquaMai.Mods.SetionNameOrder");
        sections = sections
            .OrderBy(x => x.Key)
            .OrderBy(x =>
            {
                var parts = x.Key.Split('.');
                for (int i = parts.Length; i > 0; i--)
                {
                    var key = string.Join("_", parts.Take(i));
                    if (order.TryGetValue(key, out var value))
                    {
                        return (int)value;
                    }
                }
                Utility.Log($"Section {x.Key} has no order defined, defaulting to int.MaxValue");
                return int.MaxValue;
            })
            .ToDictionary(x => x.Key, x => x.Value);
    }

    public IEnumerable<Section> SectionValues => sections.Values;
    public IEnumerable<IReflectionManager.ISection> Sections => sections.Values.Cast<IReflectionManager.ISection>();

    public IEnumerable<Entry> EntryValues => entries.Values;
    public IEnumerable<IReflectionManager.IEntry> Entries => entries.Values.Cast<IReflectionManager.IEntry>();

    public bool ContainsSection(string path)
    {
        return sections.ContainsKey(path);
    }

    public bool TryGetSection(string path, out IReflectionManager.ISection section)
    {
        if (sections.TryGetValue(path, out var sectionValue))
        {
            section = sectionValue;
            return true;
        }
        section = null;
        return false;
    }

    public bool TryGetSection(Type type, out IReflectionManager.ISection section)
    {
        return TryGetSection(type.FullName, out section);
    }

    public IReflectionManager.ISection GetSection(string path)
    {
        if (!sections.TryGetValue(path, out var section))
        {
            throw new KeyNotFoundException($"Section {path} not found");
        }
        return section;
    }

    public IReflectionManager.ISection GetSection(Type type)
    {
        if (!sectionsByFullName.TryGetValue(type.FullName, out var section))
        {
            throw new KeyNotFoundException($"Section {type.FullName} not found");
        }
        return section;
    }

    public bool ContainsEntry(string path)
    {
        return entries.ContainsKey(path);
    }

    public bool TryGetEntry(string path, out IReflectionManager.IEntry entry)
    {
        if (entries.TryGetValue(path, out var entryValue))
        {
            entry = entryValue;
            return true;
        }
        entry = null;
        return false;
    }

    public IReflectionManager.IEntry GetEntry(string path)
    {
        if (!entries.TryGetValue(path, out var entry))
        {
            throw new KeyNotFoundException($"Entry {path} not found");
        }
        return entry;
    }
}

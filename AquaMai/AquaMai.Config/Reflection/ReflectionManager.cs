using System.Reflection;
using System.Linq;
using System.Collections.Generic;
using AquaMai.Config.Attributes;
using System;

namespace AquaMai.Config.Reflection;

public class ReflectionManager
{
    public record Entry
    {
        public string Path { get; init; }
        public string Name { get; init; }
        public IReflectionField Field { get; init; }
        public ConfigEntryAttribute Attribute { get; init; }
    }

    public record Section
    {
        public string Path { get; init; }
        public IReflectionType Type { get; init; }
        public ConfigSectionAttribute Attribute { get; init; }
        public List<Entry> Entries { get; init; }
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
                Entries = sectionEntries
            };
            sections.Add(path, section);
            sectionsByFullName.Add(type.FullName, section);
        }
    }

    public IEnumerable<Section> Sections => sections.Values;

    public IEnumerable<Entry> Entries => entries.Values;

    public bool ContainsSection(string path)
    {
        return sections.ContainsKey(path);
    }

    public bool TryGetSection(string path, out Section section)
    {
        return sections.TryGetValue(path, out section);
    }

    public bool TryGetSection(Type type, out Section section)
    {
        return sectionsByFullName.TryGetValue(type.FullName, out section);
    }

    public Section GetSection(string path)
    {
        if (!sections.TryGetValue(path, out var section))
        {
            throw new KeyNotFoundException($"Section {path} not found");
        }
        return section;
    }

    public Section GetSection(Type type)
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

    public bool TryGetEntry(string path, out Entry entry)
    {
        return entries.TryGetValue(path, out entry);
    }

    public Entry GetEntry(string path)
    {
        if (!entries.TryGetValue(path, out var entry))
        {
            throw new KeyNotFoundException($"Entry {path} not found");
        }
        return entry;
    }
}

using System.Collections.Generic;
using System;

namespace AquaMai.Config.Interfaces;

public interface IReflectionManager
{
    public interface IEntry
    {
        public string Path { get; }
        public string Name { get; }
        public IReflectionField Field { get; }
    }

    public interface ISection
    {
        public string Path { get; }
        public IReflectionType Type { get; }
        public List<IEntry> Entries { get; }
    }

    public IEnumerable<ISection> Sections { get; }

    public IEnumerable<IEntry> Entries { get; }

    public bool ContainsSection(string path);

    public bool TryGetSection(string path, out ISection section);

    public bool TryGetSection(Type type, out ISection section);

    public ISection GetSection(string path);

    public ISection GetSection(Type type);

    public bool ContainsEntry(string path);

    public bool TryGetEntry(string path, out IEntry entry);

    public IEntry GetEntry(string path);
}

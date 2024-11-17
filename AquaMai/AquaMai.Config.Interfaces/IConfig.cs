using System;

namespace AquaMai.Config.Interfaces;

public interface IConfig
{
    public interface IEntryState
    {
        public bool IsDefault { get; set; }
        public object DefaultValue { get; }
        public object Value { get; set; }
    }

    public interface ISectionState
    {
        public bool IsDefault { get; set; }
        public bool DefaultEnabled { get; }
        public bool Enabled { get; set; }
    }

    public IReflectionManager ReflectionManager { get; }

    public ISectionState GetSectionState(IReflectionManager.ISection section);
    public ISectionState GetSectionState(Type type);
    public void SetSectionEnabled(IReflectionManager.ISection section, bool enabled);
    public IEntryState GetEntryState(IReflectionManager.IEntry entry);
    public void SetEntryValue(IReflectionManager.IEntry entry, object value);
}

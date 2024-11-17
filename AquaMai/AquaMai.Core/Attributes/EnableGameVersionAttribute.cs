using System;

namespace AquaMai.Core.Attributes;

[AttributeUsage(AttributeTargets.Method | AttributeTargets.Class)]
public class EnableGameVersionAttribute(uint minVersion = 0, uint maxVersion = 0, bool noWarn = false) : Attribute
{
    public uint MinVersion { get; } = minVersion;
    public uint MaxVersion { get; } = maxVersion;
    public bool NoWarn { get; } = noWarn;

    public bool ShouldEnable(uint gameVersion)
    {
        if (MinVersion > 0 && MinVersion > gameVersion) return false;
        if (MaxVersion > 0 && MaxVersion < gameVersion) return false;
        return true;
    }
}

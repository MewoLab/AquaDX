using System;

namespace AquaMai.Attributes;

[AttributeUsage(AttributeTargets.Property)]
public class EnableIf(Func<bool> condition) : Attribute
{
    public Func<bool> Condition { get; } = condition;
}

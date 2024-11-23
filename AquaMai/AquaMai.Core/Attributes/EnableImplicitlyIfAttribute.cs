using System;

namespace AquaMai.Core.Attributes;

// If the field or property with this name is true, the patch will be implicitly enabled, regardless of the config state.
// This is handled outside the config module, while The config state won't be actually set to enabled by it.
// Won't bypass the restriction of [EnableIf()] and [EnableGameVersion()].
[AttributeUsage(AttributeTargets.Class)]
public class EnableImplicitlyIf(string memberName) : Attribute
{
    public string MemberName { get; } = memberName;
}

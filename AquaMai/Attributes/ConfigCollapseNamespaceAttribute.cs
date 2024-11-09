using System;

namespace AquaMai.Attributes;

// When The most inner namespace is the same name of the class, it should be collapsed.
// The class must be the only class in the namespace with a [ConfigSection] attribute.
[AttributeUsage(AttributeTargets.Class)]
public class ConfigCollapseNamespaceAttribute : Attribute
{}

using System;
using System.Collections.Generic;

namespace AquaMai.Config.Reflection;

public class HeadlessReflectionProvider : IReflectionProvider
{
    public record ReflectionField(
        string Name,
        Type FieldType,
        object Value,
        IDictionary<Type, object> Attributes) : IReflectionField
    {
        public object Value { get; set; } = Value;

        public T GetCustomAttribute<T>() where T : Attribute => Attributes.TryGetValue(typeof(T), out var value) ? (T)value : null;
        public object GetValue(object obj) => Value;
        public void SetValue(object obj, object value) => Value = value;
    }

    public record ReflectionType(
        string FullName,
        IReflectionField[] Fields,
        IDictionary<Type, object> Attributes) : IReflectionType
    {
        public string Namespace { get; } = FullName.Substring(0, FullName.LastIndexOf('.'));
        public T GetCustomAttribute<T>() where T : Attribute => Attributes.TryGetValue(typeof(T), out var value) ? (T)value : null;
        public IReflectionField[] GetFields() => Fields;
    }

    // TODO: implement
    public IReflectionType[] GetTypes() => [];
}

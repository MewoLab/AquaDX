using System;
using System.Reflection;
using AquaMai.Config.Interfaces;

namespace AquaMai.Config.Reflection;

public class SystemReflectionProvider(Assembly assembly) : IReflectionProvider
{
    public class ReflectionField(FieldInfo field) : IReflectionField
    {
        public FieldInfo UnderlyingField { get; } = field;

        public string Name => UnderlyingField.Name;
        public Type FieldType => UnderlyingField.FieldType;
        public T GetCustomAttribute<T>() where T : Attribute => UnderlyingField.GetCustomAttribute<T>();
        public object GetValue(object obj) => UnderlyingField.GetValue(obj);
        public void SetValue(object obj, object value) => UnderlyingField.SetValue(obj, value);
    }

    public class ReflectionType(Type type) : IReflectionType
    {
        public Type UnderlyingType { get; } = type;

        public string FullName => UnderlyingType.FullName;
        public string Namespace => UnderlyingType.Namespace;
        public T GetCustomAttribute<T>() where T : Attribute => UnderlyingType.GetCustomAttribute<T>();
        public IReflectionField[] GetFields(BindingFlags bindingAttr) => Array.ConvertAll(UnderlyingType.GetFields(bindingAttr), f => new ReflectionField(f));
    }

    public Assembly UnderlyingAssembly { get; } = assembly;

    public IReflectionType[] GetTypes() => Array.ConvertAll(UnderlyingAssembly.GetTypes(), t => new ReflectionType(t));
}

using System;
using System.Collections.Generic;
using System.Reflection;

namespace AquaMai.Config.Interfaces;

public interface IReflectionField
{
    public string Name { get; }
    public Type FieldType { get; }

    public T GetCustomAttribute<T>() where T : Attribute;
    public object GetValue(object objIsNull);
    public void SetValue(object objIsNull, object value);
}

public interface IReflectionType
{
    public string FullName { get; }
    public string Namespace { get; }

    public T GetCustomAttribute<T>() where T : Attribute;
    public IReflectionField[] GetFields(BindingFlags bindingAttr);
}

public interface IReflectionProvider
{
    public IReflectionType[] GetTypes();
    public Dictionary<string, object> GetEnum(string enumName);
}

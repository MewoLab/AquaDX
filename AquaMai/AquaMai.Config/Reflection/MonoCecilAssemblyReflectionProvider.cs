using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using AquaMai.Config.Attributes;
using AquaMai.Config.Interfaces;
using Mono.Cecil;

namespace AquaMai.Config.Reflection;

public class MonoCecilReflectionProvider : IReflectionProvider
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
        string Namespace,
        IReflectionField[] Fields,
        IDictionary<Type, object> Attributes) : IReflectionType
    {
        public T GetCustomAttribute<T>() where T : Attribute => Attributes.TryGetValue(typeof(T), out var value) ? (T)value : null;
        public IReflectionField[] GetFields(BindingFlags bindingAttr) => Fields;
    }

    private static readonly Type[] attributeTypes =
    [
        typeof(ConfigCollapseNamespaceAttribute),
        typeof(ConfigSectionAttribute),
        typeof(ConfigEntryAttribute),
    ];

    private readonly IReflectionType[] reflectionTypes = [];
    private readonly Dictionary<string, Dictionary<string, object>> enums = [];

    public IReflectionType[] GetTypes() => reflectionTypes;
    public Dictionary<string, object> GetEnum(string enumName) => enums[enumName];

    public MonoCecilReflectionProvider(AssemblyDefinition assembly)
    {
        reflectionTypes = assembly.MainModule.Types.Select(cType => {
            var typeAttributes = InstantiateAttributes(cType.CustomAttributes);
            var fields = cType.Fields.Select(cField => {
                var fieldAttributes = InstantiateAttributes(cField.CustomAttributes);
                if (fieldAttributes.Count == 0)
                {
                    return null;
                }
                var type = GetRuntimeType(cField.FieldType);
                var defaultValue = cField.HasDefault ? cField.InitialValue : GetDefaultValue(type);
                return new ReflectionField(cField.Name, type, defaultValue, fieldAttributes);
            }).Where(field => field != null).ToArray();
            return new ReflectionType(cType.FullName, cType.Namespace, fields, typeAttributes);
        }).ToArray();
        enums = assembly.MainModule.Types
            .Where(cType => cType.IsEnum)
            .ToDictionary(cType =>
                cType.FullName,
                cType => cType.Fields
                    .Where(cField => cField.IsPublic && cField.IsStatic && cField.Constant != null)
                    .ToDictionary(cField => cField.Name, cField => cField.Constant));
    }

    private Dictionary<Type, object> InstantiateAttributes(ICollection<CustomAttribute> attribute) =>
        attribute
            .Select(InstantiateAttribute)
            .Where(a => a != null)
            .ToDictionary(a => a.GetType(), a => a);

    private object InstantiateAttribute(CustomAttribute attribute) =>
        attributeTypes.FirstOrDefault(t => t.FullName == attribute.AttributeType.FullName) switch
        {
            Type type => Activator.CreateInstance(type,
                attribute.Constructor.Parameters
                    .Select((parameter, i) =>
                    {
                        var runtimeType = GetRuntimeType(parameter.ParameterType);
                        var value = attribute.ConstructorArguments[i].Value;
                        if (runtimeType.IsEnum)
                        {
                            return Enum.Parse(runtimeType, value.ToString());
                        }
                        return value;
                    })
                    .ToArray()),
            _ => null
        };

    private Type GetRuntimeType(TypeReference typeReference) {
        if (typeReference.IsGenericInstance)
        {
            var genericInstance = (GenericInstanceType)typeReference;
            var genericType = GetRuntimeType(genericInstance.ElementType);
            var genericArguments = genericInstance.GenericArguments.Select(GetRuntimeType).ToArray();
            return genericType.MakeGenericType(genericArguments);
        }

        var type = Type.GetType(typeReference.FullName);
        if (type == null)
        {
            throw new TypeLoadException($"Type {typeReference.FullName} not found.");
        }
        return type;
    }

    private static object GetDefaultValue(Type type)
    {
        if (type.IsValueType)
        {
            return Activator.CreateInstance(type);
        }
        else if (type == typeof(string))
        {
            return string.Empty;
        }
        else
        {
            return null;
        }
    }
}

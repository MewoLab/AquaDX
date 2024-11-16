using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using AquaMai.Config.Attributes;
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

    public IReflectionType[] GetTypes() => reflectionTypes;

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
    }

    public Dictionary<Type, object> InstantiateAttributes(ICollection<CustomAttribute> attribute)
    {
        return attribute.Select(InstantiateAttribute).Where(a => a != null).ToDictionary(a => a.GetType(), a => a);
    }

    public object InstantiateAttribute(CustomAttribute attribute)
    {
        var type = attributeTypes.FirstOrDefault(t => t.FullName == attribute.AttributeType.FullName);
        if (type != null)
        {
            var parameterTypes = attribute.Constructor.Parameters.Select(p => GetRuntimeType(p.ParameterType)).ToArray();
            var parameters = attribute.ConstructorArguments.Select(a => a.Value).ToArray();
            for (int i = 0; i < parameters.Length; i++)
            {
                if (parameterTypes[i].IsEnum)
                {
                    parameters[i] = Enum.Parse(parameterTypes[i], parameters[i].ToString());
                }
            }
            return Activator.CreateInstance(type, parameters);
        }
        return null;
    }

    public Type GetRuntimeType(TypeReference typeReference) {
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

    public static object GetDefaultValue(Type type)
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

using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using AquaMai.Config.Attributes;
using AquaMai.Config.Interfaces;
using Mono.Cecil;
using Mono.Cecil.Cil;

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
                try
                {
                    var fieldAttributes = InstantiateAttributes(cField.CustomAttributes);
                    if (fieldAttributes.Count == 0)
                    {
                        return null;
                    }
                    var type = GetRuntimeType(cField.FieldType);
                    var defaultValue = GetFieldDefaultValue(cType, cField, type);
                    return new ReflectionField(cField.Name, type, defaultValue, fieldAttributes);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                }
                return null;
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

    private static object GetFieldDefaultValue(TypeDefinition cType, FieldDefinition cField, Type fieldType)
    {
        object defaultValue = null;
        var cctor = cType.Methods.SingleOrDefault(m => m.Name == ".cctor");
        if (cctor != null)
        {
            var store = cctor.Body.Instructions.SingleOrDefault(i => i.OpCode == OpCodes.Stsfld && i.Operand == cField);
            if (store != null)
            {
                var loadOperand = ParseConstantLoadOperand(store.Previous);
                if (fieldType == typeof(bool))
                {
                    defaultValue = Convert.ToBoolean(loadOperand);
                }
                else
                {
                    defaultValue = loadOperand;
                }
            }
        }

        if (defaultValue == null && cField.HasDefault)
        {
            throw new InvalidOperationException($"Field {cType.FullName}.{cField.Name} has default value but no .cctor stsfld instruction.");
        }
        defaultValue ??= GetDefaultValue(fieldType);

        if (fieldType.IsEnum)
        {
            var enumType = fieldType.GetEnumUnderlyingType();
            // Assume casting is safe since we're getting the default value from the field
            var castedValue = Convert.ChangeType(defaultValue, enumType);
            if (Enum.IsDefined(fieldType, castedValue))
            {
                return Enum.ToObject(fieldType, castedValue);
            }
        }

        return defaultValue;
    }

    private static object ParseConstantLoadOperand(Instruction instruction)
    {
        if (instruction.OpCode == OpCodes.Ldc_I4_M1)
        {
            return -1;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_0)
        {
            return 0;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_1)
        {
            return 1;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_2)
        {
            return 2;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_3)
        {
            return 3;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_4)
        {
            return 4;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_5)
        {
            return 5;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_6)
        {
            return 6;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_7)
        {
            return 7;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_8)
        {
            return 8;
        }
        if (instruction.OpCode == OpCodes.Ldc_I4_S)
        {
            return Convert.ToInt32((sbyte)instruction.Operand);
        }
        if (instruction.OpCode == OpCodes.Ldc_I4)
        {
            return (int)instruction.Operand;
        }
        if (instruction.OpCode == OpCodes.Ldc_I8)
        {
            return (long)instruction.Operand;
        }
        if (instruction.OpCode == OpCodes.Ldc_R4)
        {
            return (float)instruction.Operand;
        }
        if (instruction.OpCode == OpCodes.Ldc_R8)
        {
            return (double)instruction.Operand;
        }
        if (instruction.OpCode == OpCodes.Ldstr)
        {
            return (string)instruction.Operand;
        }
        else
        {
            var currentMethod = MethodBase.GetCurrentMethod();
            throw new NotImplementedException($"Unsupported constant load: {instruction}. Please implement in {currentMethod.DeclaringType.FullName}.{currentMethod.Name}");
        }
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

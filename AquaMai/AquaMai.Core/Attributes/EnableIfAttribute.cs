using System;

namespace AquaMai.Core.Attributes;

public enum EnableIfCondition
{
    Equal,
    NotEqual,
    GreaterThan,
    LessThan,
    GreaterThanOrEqual,
    LessThanOrEqual
}

[AttributeUsage(AttributeTargets.Method | AttributeTargets.Class)]
public class EnableIfAttribute(
    Type referenceType,
    string referenceMember,
    EnableIfCondition condition,
    object baseValue) : Attribute
{
    public Type ReferenceType { get; } = referenceType;
    public string ReferenceMember { get; } = referenceMember;
    public EnableIfCondition Condition { get; } = condition;
    public object BaseValue { get; } = baseValue;

    // Referencing a field in another class and checking if it's true.
    public EnableIfAttribute(Type referenceType, string referenceMember)
    : this(referenceType, referenceMember, EnableIfCondition.Equal, true)
    { }

    // Referencing a field in the same class and comparing it with a value.
    public EnableIfAttribute(string referenceMember, EnableIfCondition condition, object value)
    : this(null, referenceMember, condition, value)
    { }

    // Referencing a field in the same class and checking if it's true.
    public EnableIfAttribute(string referenceMember)
    : this(referenceMember, EnableIfCondition.Equal, true)
    { }

    public bool ShouldEnable(Type selfType)
    {
        var referenceType = ReferenceType ?? selfType;
        var referenceField = referenceType.GetField(
            ReferenceMember,
            System.Reflection.BindingFlags.Static | System.Reflection.BindingFlags.Public | System.Reflection.BindingFlags.NonPublic);
        var referenceProperty = referenceType.GetProperty(
            ReferenceMember,
            System.Reflection.BindingFlags.Static | System.Reflection.BindingFlags.Public | System.Reflection.BindingFlags.NonPublic);
        if (referenceField == null && referenceProperty == null)
        {
            throw new ArgumentException($"Field or property {ReferenceMember} not found in {referenceType.FullName}");
        }
        var referenceMemberValue = referenceField != null ? referenceField.GetValue(null) : referenceProperty.GetValue(null);
        switch (Condition)
        {
            case EnableIfCondition.Equal:
                return referenceMemberValue.Equals(BaseValue);
            case EnableIfCondition.NotEqual:
                return !referenceMemberValue.Equals(BaseValue);
            case EnableIfCondition.GreaterThan:
            case EnableIfCondition.LessThan:
            case EnableIfCondition.GreaterThanOrEqual:
            case EnableIfCondition.LessThanOrEqual:
                var comparison = (IComparable)referenceMemberValue;
                return Condition switch
                {
                    EnableIfCondition.GreaterThan => comparison.CompareTo(BaseValue) > 0,
                    EnableIfCondition.LessThan => comparison.CompareTo(BaseValue) < 0,
                    EnableIfCondition.GreaterThanOrEqual => comparison.CompareTo(BaseValue) >= 0,
                    EnableIfCondition.LessThanOrEqual => comparison.CompareTo(BaseValue) <= 0,
                    _ => throw new NotImplementedException(),
                };
            default:
                throw new NotImplementedException();
        }
    }
}

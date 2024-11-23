using System;

namespace AquaMai.Config.Attributes;

public enum EnableConditionOperator
{
    Equal,
    NotEqual,
    GreaterThan,
    LessThan,
    GreaterThanOrEqual,
    LessThanOrEqual
}

public class EnableCondition(
    Type referenceType,
    string referenceMember,
    EnableConditionOperator @operator,
    object rightSideValue) : Attribute
{
    public Type ReferenceType { get; } = referenceType;
    public string ReferenceMember { get; } = referenceMember;
    public EnableConditionOperator Operator { get; } = @operator;
    public object RightSideValue { get; } = rightSideValue;

    // Referencing a field in another class and checking if it's true.
    public EnableCondition(Type referenceType, string referenceMember)
    : this(referenceType, referenceMember, EnableConditionOperator.Equal, true)
    { }

    // Referencing a field in the same class and comparing it with a value.
    public EnableCondition(string referenceMember, EnableConditionOperator condition, object value)
    : this(null, referenceMember, condition, value)
    { }

    // Referencing a field in the same class and checking if it's true.
    public EnableCondition(string referenceMember)
    : this(referenceMember, EnableConditionOperator.Equal, true)
    { }

    public bool Evaluate(Type selfType)
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
        switch (Operator)
        {
            case EnableConditionOperator.Equal:
                return referenceMemberValue.Equals(RightSideValue);
            case EnableConditionOperator.NotEqual:
                return !referenceMemberValue.Equals(RightSideValue);
            case EnableConditionOperator.GreaterThan:
            case EnableConditionOperator.LessThan:
            case EnableConditionOperator.GreaterThanOrEqual:
            case EnableConditionOperator.LessThanOrEqual:
                var comparison = (IComparable)referenceMemberValue;
                return Operator switch
                {
                    EnableConditionOperator.GreaterThan => comparison.CompareTo(RightSideValue) > 0,
                    EnableConditionOperator.LessThan => comparison.CompareTo(RightSideValue) < 0,
                    EnableConditionOperator.GreaterThanOrEqual => comparison.CompareTo(RightSideValue) >= 0,
                    EnableConditionOperator.LessThanOrEqual => comparison.CompareTo(RightSideValue) <= 0,
                    _ => throw new NotImplementedException(),
                };
            default:
                throw new NotImplementedException();
        }
    }
}

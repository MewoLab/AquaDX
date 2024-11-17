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
    string conditionField,
    EnableIfCondition condition,
    object baseValue) : Attribute
{
    public Type ReferenceType { get; } = referenceType;
    public string ConditionField { get; } = conditionField;
    public EnableIfCondition Condition { get; } = condition;
    public object BaseValue { get; } = baseValue;

    // Referencing a field in another class and checking if it's true.
    public EnableIfAttribute(Type referenceType, string conditionField)
    : this(referenceType, conditionField, EnableIfCondition.Equal, true)
    { }

    // Referencing a field in the same class and comparing it with a value.
    public EnableIfAttribute(string conditionField, EnableIfCondition condition, object value)
    : this(null, conditionField, condition, value)
    { }

    // Referencing a field in the same class and checking if it's true.
    public EnableIfAttribute(string conditionField)
    : this(conditionField, EnableIfCondition.Equal, true)
    { }

    public bool ShouldEnable(Type selfType)
    {
        var referenceType = ReferenceType ?? selfType;
        var conditionField = referenceType.GetField(
            ConditionField,
            System.Reflection.BindingFlags.Static | System.Reflection.BindingFlags.Public | System.Reflection.BindingFlags.NonPublic);
        if (conditionField == null)
        {
            throw new ArgumentException($"Field {ConditionField} not found in {referenceType.FullName}");
        }
        var conditionFieldValue = conditionField.GetValue(null);
        switch (Condition)
        {
            case EnableIfCondition.Equal:
                return conditionFieldValue.Equals(BaseValue);
            case EnableIfCondition.NotEqual:
                return !conditionFieldValue.Equals(BaseValue);
            case EnableIfCondition.GreaterThan:
            case EnableIfCondition.LessThan:
            case EnableIfCondition.GreaterThanOrEqual:
            case EnableIfCondition.LessThanOrEqual:
                var comparison = (IComparable)conditionFieldValue;
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

using System;

namespace AquaMai.Attributes;

public enum EnableIfCondition
{
    Equal,
    NotEqual,
    GreaterThan,
    LessThan,
    GreaterThanOrEqual,
    LessThanOrEqual
}

[AttributeUsage(AttributeTargets.Method)]
public class EnableIfAttribute : Attribute
{
    public Type ReferenceType { get; }
    public string ConditionField { get; }
    public EnableIfCondition Condition { get; }
    public object Value { get; }

    // Referencing a field in another class and comparing it with a value.
    public EnableIfAttribute(Type referenceType, string conditionField, EnableIfCondition condition, object value)
    {
        ReferenceType = referenceType;
        ConditionField = conditionField;
        Condition = condition;
        Value = value;
    }

    // Referencing a field in another class and checking if it's true.
    public EnableIfAttribute(Type referenceType, string conditionField)
    : this(referenceType, conditionField, EnableIfCondition.Equal, true)
    {}

    // Referencing a field in the same class and comparing it with a value.
    public EnableIfAttribute(string conditionField, EnableIfCondition condition, object value)
    : this(null, conditionField, condition, value)
    {}

    // Referencing a field in the same class and checking if it's true.
    public EnableIfAttribute(string conditionField)
    : this(conditionField, EnableIfCondition.Equal, true)
    {}
}

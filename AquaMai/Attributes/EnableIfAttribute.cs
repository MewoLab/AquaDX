using System;

namespace AquaMai.Attributes;

[AttributeUsage(AttributeTargets.Method)]
public class EnableIf(string conditionField) : Attribute
{
    public string ConditionField { get; } = conditionField;
}

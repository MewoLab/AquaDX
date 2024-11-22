using System;
using System.Linq;
using AquaMai.Config;
using AquaMai.Config.Interfaces;
using Tomlet;
using Tomlet.Models;

namespace AquaMail.Config;

public class ConfigView : IConfigView
{
    public readonly TomlTable root;

    public ConfigView()
    {
        root = new TomlTable();
    }

    public ConfigView(TomlTable root)
    {
        this.root = root;
    }

    public ConfigView(string tomlString)
    {
        var tomlValue = new TomlParser().Parse(tomlString);
        if (tomlValue is not TomlTable tomlTable)
        {
            throw new ArgumentException($"Invalid TOML, expected a table, got: {tomlValue.GetType()}");
        }
        root = tomlTable;
    }

    public void Set(string path, object value)
    {
        var pathComponents = path.Split('.');
        var current = root;
        foreach (var component in pathComponents.Take(pathComponents.Length - 1))
        {
            if (!current.TryGetValue(component, out var next))
            {
                next = new TomlTable();
                current.Put(component, next);
            }
            current = (TomlTable)next;
        }
        current.Put(pathComponents.Last(), value);
    }

    public T Get<T>(string path, T defaultValue = default)
    {
        return TryGet(path, out T resultValue) ? resultValue : defaultValue;
    }

    public bool TryGet<T>(string path, out T resultValue)
    {
        var pathComponents = path.Split('.');
        var current = root;
        foreach (var component in pathComponents.Take(pathComponents.Length - 1))
        {
            if (!TomlTryGetValueCaseInsensitive(current, component, out var next) || next is not TomlTable nextTable)
            {
                resultValue = default;
                return false;
            }
            current = nextTable;
        }
        if (!TomlTryGetValueCaseInsensitive(current, pathComponents.Last(), out var value))
        {
            resultValue = default;
            return false;
        }
        if (value is not T tValue)
        {
            Utility.Log($"Unexpected type {value.GetType()} at {path}, expected {typeof(T)}");
            resultValue = default;
            return false;
        }
        resultValue = tValue;
        return true;
    }

    private bool TomlTryGetValueCaseInsensitive(TomlTable table, string key, out TomlValue value)
    {
        // Prefer exact match
        if (table.TryGetValue(key, out value))
        {
            return true;
        }
        // Fallback to case-insensitive match
        foreach (var kvp in table)
        {
            if (string.Equals(kvp.Key, key, StringComparison.OrdinalIgnoreCase))
            {
                value = kvp.Value;
                return true;
            }
        }
        value = null;
        return false;
    }

    public string ToToml()
    {
        return root.SerializedValue;
    }
}

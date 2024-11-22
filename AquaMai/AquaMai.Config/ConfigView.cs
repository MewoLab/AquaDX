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

    public TomlTable EnsureDictionary(string path)
    {
        var pathComponents = path.Split('.');
        var current = root;
        foreach (var component in pathComponents)
        {
            if (!current.TryGetValue(component, out var next))
            {
                next = new TomlTable();
                current.Put(component, next);
            }
            current = (TomlTable)next;
        }
        return current;
    }

    public void SetValue(string path, object value)
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

    public T GetValueOrDefault<T>(string path, T defaultValue = default)
    {
        return TryGetValue(path, out T resultValue) ? resultValue : defaultValue;
    }

    public bool TryGetValue<T>(string path, out T resultValue)
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
        try
        {
            resultValue = Utility.ParseTomlValue<T>(value);
            return true;
        }
        catch (Exception e)
        {
            Utility.Log($"Failed to parse value at {path}: {e.Message}");
            resultValue = default;
            return false;
        }
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

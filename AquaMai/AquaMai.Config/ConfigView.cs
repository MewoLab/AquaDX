using System;
using System.Linq;
using AquaMai.Config.Interfaces;
using Tomlet;
using Tomlet.Models;

namespace AquaMai.Config;

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

        if (value == null)
        {
            current.Keys.Remove(pathComponents.Last());
            return;
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
            if (!Utility.TomlTryGetValueCaseInsensitive(current, component, out var next) || next is not TomlTable nextTable)
            {
                resultValue = default;
                return false;
            }
            current = nextTable;
        }
        if (!Utility.TomlTryGetValueCaseInsensitive(current, pathComponents.Last(), out var value))
        {
            resultValue = default;
            return false;
        }
        if (typeof(T) == typeof(object))
        {
            resultValue = (T)(object)value;
            return true;
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

    public bool Remove(string path)
    {
        var pathComponents = path.Split('.');
        var current = root;
        foreach (var component in pathComponents.Take(pathComponents.Length - 1))
        {
            if (!Utility.TomlTryGetValueCaseInsensitive(current, component, out var next) || next is not TomlTable nextTable)
            {
                return false;
            }
            current = (TomlTable)next;
        }
        var keyToRemove = pathComponents.Last();
        var keysCaseSensitive = current.Keys.Where(k => string.Equals(k, keyToRemove, StringComparison.OrdinalIgnoreCase));
        foreach (var key in keysCaseSensitive)
        {
            current.Keys.Remove(key);
        }
        return keysCaseSensitive.Any();
    }

    public string ToToml()
    {
        return root.SerializedValue;
    }

    public IConfigView Clone()
    {
        return new ConfigView(ToToml());
    }
}

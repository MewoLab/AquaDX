using System;
using System.Reflection;
using System.Text;
using AquaMai.Config.Attributes;
using Tomlet.Models;

namespace AquaMai.Config;

public static class ConfigSerializer
{
    public record Options
    {
        public string Lang { get; init; }
        public bool IncludeBanner { get; init; }
    }

    private const string ZH_MCM_BANNER = """
                                         试试使用 MaiChartManager 图形化配置 AquaMai 吧！
                                         https://github.com/clansty/MaiChartManager
                                         """;

    public static string Serialize(Config config, Options options)
    {
        StringBuilder sb = new();
        if (options.IncludeBanner)
        {
            var banner = options.Lang == "zh" ? ZH_MCM_BANNER : null;
            if (banner != null)
            {
                AppendComment(sb, banner.TrimEnd());
                sb.AppendLine();
            }
        }

        // Version
        AppendEntry(sb, null, "Version", 2);

        foreach (var section in config.reflectionManager.Sections)
        {
            var sectionState = config.GetSectionState(section);
            if (section.Attribute.Example != ConfigSectionExample.Shown && sectionState.IsDefault)
            {
                continue;
            }
            sb.AppendLine();

            AppendComment(sb, section.Attribute.Comment, options);
            sb.AppendLine(sectionState.IsDefault ? $"#[{section.Path}]" : $"[{section.Path}]");
            if (!sectionState.IsDefault && !sectionState.Enabled)
            {
                // Disabled explicitly
                AppendEntry(sb, null, "Disabled", true);
            }

            foreach (var entry in section.Entries)
            {
                var entryState = config.GetEntryState(entry);
                AppendComment(sb, entry.Attribute.Comment, options);
                AppendEntry(sb, section.Path, entry.Name, entryState.Value, entryState.IsDefault);
            }
        }

        return sb.ToString();
    }

    private static string SerializeTomlValue(string diagnosticPath, object value)
    {
        var type = value.GetType();
        if (value is bool b)
        {
            return b ? "true" : "false";
        }
        else if (value is string str)
        {
            return new TomlString(str).SerializedValue;
        }
        else if (type.IsEnum)
        {
            return new TomlString(value.ToString()).SerializedValue;
        }
        else if (Utility.IsIntegerType(type))
        {
            return value.ToString();
        }
        else if (Utility.IsFloatType(type))
        {
            return new TomlDouble(Convert.ToDouble(value)).SerializedValue;
        }
        else
        {
            var currentMethod = MethodBase.GetCurrentMethod();
            throw new NotImplementedException($"Unsupported config entry type: {type.FullName} ({diagnosticPath}). Please implement in {currentMethod.DeclaringType.FullName}.{currentMethod.Name}");
        }
    }

    private static void AppendComment(StringBuilder sb, ConfigComment comment, Options options)
    {
        if (comment != null)
        {
            AppendComment(sb, comment.GetLocalized(options.Lang));
        }
    }

    private static void AppendComment(StringBuilder sb, string comment)
    {
        comment = comment.Trim();
        if (!string.IsNullOrEmpty(comment))
        {
            foreach (var line in comment.Split('\n'))
            {
                sb.AppendLine($"## {line}");
            }
        }
    }

    private static void AppendEntry(StringBuilder sb, string diagnosticsSection, string key, object value, bool commented = false)
    {
        if (commented)
        {
            sb.Append('#');
        }
        var diagnosticsPath = string.IsNullOrEmpty(diagnosticsSection)
                                ? key
                                : $"{diagnosticsSection}.{key}";
        sb.AppendLine($"{key} = {SerializeTomlValue(diagnosticsPath, value)}");
    }
}

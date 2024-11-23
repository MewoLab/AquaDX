using System;
using System.Reflection;
using System.Text;
using AquaMai.Config.Attributes;
using AquaMai.Config.Interfaces;
using Tomlet.Models;

namespace AquaMai.Config;

public class ConfigSerializer(IConfigSerializer.Options Options) : IConfigSerializer
{
    private const string ZH_MCM_BANNER = """
                                         试试使用 MaiChartManager 图形化配置 AquaMai 吧！
                                         https://github.com/clansty/MaiChartManager
                                         """;

    private readonly IConfigSerializer.Options Options = Options;

    public string Serialize(IConfig config)
    {
        StringBuilder sb = new();
        if (Options.IncludeBanner)
        {
            var banner = Options.Lang == "zh" ? ZH_MCM_BANNER : null;
            if (banner != null)
            {
                AppendComment(sb, banner.TrimEnd());
                sb.AppendLine();
            }
        }

        // Version
        AppendEntry(sb, null, "Version", "2.0");

        foreach (var section in ((Config)config).reflectionManager.SectionValues)
        {
            var sectionState = config.GetSectionState(section);

            // If the state is default, print the example only. If the example is hidden, skip it.
            if (sectionState.IsDefault && section.Attribute.ExampleHidden)
            {
                continue;
            }
            sb.AppendLine();

            AppendComment(sb, section.Attribute.Comment);

            // If the section is hidden and hidden by default, print it as commented.
            if (sectionState.IsDefault && !sectionState.Enabled)
            {
                sb.AppendLine($"#[{section.Path}]");
            }
            else // If the section is overridden, or is enabled by any means, print it normally.
            {
                sb.AppendLine($"[{section.Path}]");
            }

            // If the section is disabled explicitly, print the "Disabled" meta entry.
            if (!sectionState.IsDefault && !sectionState.Enabled)
            {
                AppendEntry(sb, null, "Disabled", true);
            }
            // If the section is enabled by default, print the "Disabled" meta entry as commented.
            else if (sectionState.IsDefault && section.Attribute.DefaultOn)
            {
                AppendEntry(sb, null, "Disabled", false, true);
            }
            // Otherwise, don't print the "Disabled" meta entry.

            // Print entries.
            foreach (var entry in section.entries)
            {
                var entryState = config.GetEntryState(entry);
                AppendComment(sb, entry.Attribute.Comment);
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

    private void AppendComment(StringBuilder sb, ConfigComment comment)
    {
        if (comment != null)
        {
            AppendComment(sb, comment.GetLocalized(Options.Lang));
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

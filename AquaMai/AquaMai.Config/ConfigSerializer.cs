using System;
using System.Reflection;
using System.Text;
using AquaMai.Config.Attributes;
using AquaMai.Config.Interfaces;
using Tomlet.Models;

namespace AquaMai.Config;

public class ConfigSerializer(IConfigSerializer.Options Options) : IConfigSerializer
{
    private const string BANNER_ZH =
        """
        这是 AquaMai 的 TOML 配置文件

        - 井号 # 开头的行为注释，被注释掉的内容不会生效
            - 为方便使用 VSCode 等编辑器进行编辑，被注释掉的配置内容使用一个井号 #，而注释文本使用两个井号 ##
        - 以方括号包裹的行，如 [OptionalCategory.Section]，为一个栏目
            - 将默认被注释（即默认禁用）的栏目取消注释即可启用
            - 若要禁用一个默认启用的栏目，请在栏目下添加「Disabled = true」配置项，删除它/注释它不会有效
        - 形如「键 = 值」为一个配置项
            - 配置项应用到其上方最近的栏目，请不要在一个栏目被注释掉的情况下开启其配置项（会加到上一个栏目，无效）
            - 当对应栏目启用时，配置项生效，无论是否将其取消注释
            - 注释掉的配置项保留其注释中的默认值，默认值可能会随版本更新而变化
        - 该文件的格式和文字注释是固定的，配置文件将在启动时被重写，无法解析的内容将被删除

        试试使用 MaiChartManager 图形化配置 AquaMai 吧！
        https://github.com/clansty/MaiChartManager
        """;

    private const string BANNER_EN =
        """
        This is the TOML configuration file of AquaMai.

        - Lines starting with a hash # are comments. Commented content will not take effect.
            - For easily editing with editors (e.g. VSCode), commented configuration content uses a single hash #, while comment text uses two hashes ##.
        - Lines with square brackets like [OptionalCategory.Section] are sections.
            - Uncomment a section that is commented out by default (i.e. disabled by default) to enable it.
            - To disable a section that is enabled by default, add a "Disable = true" entry under the section. Removing/commenting it will not work.
        - Lines like "Key = Value" is a configuration entry.
            - Configuration entries apply to the nearest section above them. Do not enable a configuration entry when its section is commented out (will be added to the previous section, which is invalid).
            - Configuration entries take effect when the corresponding section is enabled, regardless of whether they are uncommented.
            - Commented configuration entries retain their default values (shown in the comment), which may change with version updates.
        - The format and text comments of this file are fixed. The configuration file will be rewritten at startup, and unrecognizable content will be deleted.
        """;

    private readonly IConfigSerializer.Options Options = Options;

    public string Serialize(IConfig config)
    {
        StringBuilder sb = new();
        if (Options.IncludeBanner)
        {
            var banner = Options.Lang == "zh" ? BANNER_ZH : BANNER_EN;
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

            if (// If the section is hidden and hidden by default, print it as commented.
                sectionState.IsDefault && !sectionState.Enabled &&
                // If the section is marked as always enabled, print it normally.
                !section.Attribute.AlwaysEnabled)
            {
                sb.AppendLine($"#[{section.Path}]");
            }
            else // If the section is overridden, or is enabled by any means, print it normally.
            {
                sb.AppendLine($"[{section.Path}]");
            }

            if (!section.Attribute.AlwaysEnabled)
            {
                // If the section is disabled explicitly, print the "Disabled" meta entry.
                if (!sectionState.IsDefault && !sectionState.Enabled)
                {
                    AppendEntry(sb, null, "Disabled", value: true);
                }
                // If the section is enabled by default, print the "Disabled" meta entry as commented.
                else if (sectionState.IsDefault && section.Attribute.DefaultOn)
                {
                    AppendEntry(sb, null, "Disabled", value: false, commented: true);
                }
                // Otherwise, don't print the "Disabled" meta entry.
            }

            // Print entries.
            foreach (var entry in section.entries)
            {
                var entryState = config.GetEntryState(entry);
                AppendComment(sb, entry.Attribute.Comment);
                if (entry.Attribute.SpecialConfigEntry == SpecialConfigEntry.Locale && Options.OverrideLocaleValue)
                {
                    AppendEntry(sb, section.Path, entry.Name, Options.Lang);
                }
                else
                {
                    AppendEntry(sb, section.Path, entry.Name, entryState.Value, entryState.IsDefault);
                }
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

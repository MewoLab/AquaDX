using AquaMai.Config.Interfaces;
using Tomlet.Models;

namespace AquaMai.Config.Migration;

public class ConfigMigration_V2_0_V2_1 : IConfigMigration
{
    public string FromVersion => "2.0";
    public string ToVersion => "2.1";

    public IConfigView Migrate(IConfigView src)
    {
        var dst = src.Clone();
        dst.SetValue("Version", ToVersion);

        if (IsSectionEnabled(src, "Tweaks.ResetTouchAfterTrack"))
        {
            dst.Remove("Tweaks.ResetTouchAfterTrack");
            dst.SetValue("Tweaks.ResetTouch.AfterTrack", true);
        }

        return dst;
    }

    public bool IsSectionEnabled(IConfigView src, string path)
    {
        if (src.TryGetValue(path, out object section))
        {
            if (section is bool enabled)
            {
                return enabled;
            }
            else if (section is TomlTable table)
            {
                if (Utility.TomlTryGetValueCaseInsensitive(table, "Disabled", out var disabled))
                {
                    return !Utility.IsTrutyOrDefault(disabled);
                }
                return true;
            }
        }
        return false;
    }
}

using AquaMai.Config.Interfaces;
using AquaMail.Config;

namespace AquaMai.Config.Migration;

public class ConfigMigration_V1_V2 : IConfigMigration
{
    public string FromVersion => "1";
    public string ToVersion => "2";

    public IConfigView Migrate(IConfigView src)
    {
        var dst = new ConfigView();
        return dst;
    }
}

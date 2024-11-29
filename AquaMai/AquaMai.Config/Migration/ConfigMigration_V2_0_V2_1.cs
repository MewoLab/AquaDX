using System;
using System.Collections.Generic;
using AquaMai.Config.Interfaces;
using AquaMai.Config.Types;
using Tomlet.Models;

namespace AquaMai.Config.Migration;

public class ConfigMigration_V2_0_V2_1 : IConfigMigration
{
    public string FromVersion => "2.0";
    public string ToVersion => "2.1";

    public IConfigView Migrate(IConfigView src)
    {
        src.SetValue("Version", ToVersion);

        if (src.GetValueOrDefault<bool>("Tweaks.ResetTouchAfterTrack"))
        {
            src.SetValue("Tweaks.ResetTouch.AfterTrack", true);
            src.SetValue("Tweaks.ResetTouchAfterTrack", null);
        }

        return src;
    }
}
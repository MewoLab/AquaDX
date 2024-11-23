using System;
using System.Collections.Generic;
using System.Linq;
using AquaMai.Config.Interfaces;

namespace AquaMai.Config.Migration;

public class ConfigMigrationManager : IConfigMigrationManager
{
    public static readonly ConfigMigrationManager Instance = new();

    private readonly Dictionary<string, IConfigMigration> migrationMap =
        new List<IConfigMigration>
        {
            new ConfigMigration_V1_0_V2_0()
        }.ToDictionary(m => m.FromVersion);

    public readonly string latestVersion;

    private ConfigMigrationManager()
    {
        latestVersion = migrationMap.Values
            .Select(m => m.ToVersion)
            .OrderByDescending(version =>
            {
                var versionParts = version.Split('.').Select(int.Parse).ToArray();
                return versionParts[0] * 100000 + versionParts[1];
            })
            .First();
    }

    public IConfigView Migrate(IConfigView config)
    {
        var currentVersion = GetVersion(config);
        while (migrationMap.ContainsKey(currentVersion))
        {
            var migration = migrationMap[currentVersion];
            Utility.Log($"Migrating config from v{migration.FromVersion} to v{migration.ToVersion}");
            config = migration.Migrate(config);
            currentVersion = migration.ToVersion;
        }
        if (currentVersion != latestVersion)
        {
            throw new ArgumentException($"Could not migrate the config from v{currentVersion} to v{latestVersion}");
        }
        return config;
    }

    public string GetVersion(IConfigView config)
    {
        if (config.TryGetValue<string>("Version", out var version))
        {
            return version;
        }
        // Assume v1.0 if not found
        return "1.0";
    }
}

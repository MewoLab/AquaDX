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
            new ConfigMigration_V1_V2()
        }.ToDictionary(m => m.FromVersion);

    public readonly string latestVersion;

    private ConfigMigrationManager()
    {
        latestVersion = migrationMap.Values
            .Select(m => m.ToVersion)
            .OrderByDescending(version =>
            {
                var versionParts = version.Split('.').Select(int.Parse).ToArray();
                return versionParts[0] * 100000 + (versionParts.Length < 2 ? 0 : versionParts[1]);
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
        return config;
    }

    public string GetVersion(IConfigView config)
    {
        if (config.TryGetValue<string>("Version", out var version))
        {
            return version;
        }
        // Assume version 1 if not found
        return "1";
    }
}

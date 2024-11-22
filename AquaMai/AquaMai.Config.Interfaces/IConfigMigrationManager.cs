namespace AquaMai.Config.Interfaces;

public interface IConfigMigrationManager
{
    public IConfigView Migrate(IConfigView config);
    public string GetVersion(IConfigView config);
}

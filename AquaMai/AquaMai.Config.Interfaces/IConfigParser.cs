namespace AquaMai.Config.Interfaces;

public interface IConfigParser
{
    public void Parse(IConfig config, string tomlString);
    public void Parse(IConfig config, IConfigView configView);
}

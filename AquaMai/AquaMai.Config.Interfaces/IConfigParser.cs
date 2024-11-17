namespace AquaMai.Config.Interfaces;

public interface IConfigParser
{
    public void Parse(IConfig config, string tomlString);
}

namespace AquaMai.Config.Interfaces;

public interface IConfigSerializer
{
    public record Options
    {
        public string Lang { get; init; }
        public bool IncludeBanner { get; init; }
    }

    public string Serialize(IConfig config);
}

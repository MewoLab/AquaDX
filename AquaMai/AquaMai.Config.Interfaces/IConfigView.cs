namespace AquaMai.Config.Interfaces;

public interface IConfigView
{
    public void Set(string path, object value);
    public T Get<T>(string path, T defaultValue = default);
    public bool TryGet<T>(string path, out T resultValue);
    public string ToToml();
}

namespace AquaMai.Config.Interfaces;

public interface IConfigView
{
    public void SetValue(string path, object value);
    public T GetValueOrDefault<T>(string path, T defaultValue = default);
    public bool TryGetValue<T>(string path, out T resultValue);
    public string ToToml();
}

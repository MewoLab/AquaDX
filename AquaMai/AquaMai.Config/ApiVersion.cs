namespace AquaMai.Config;

public static class ApiVersion
{
    // Using a raw string for API version instead of a constant for maximum compatibility.
    // When breaking changes are made, increment the major version.
    // When new APIs are added in a backwards-compatible but non-forward-compatible manner, increment the minor version.
    public const string Version = "1.0";
}

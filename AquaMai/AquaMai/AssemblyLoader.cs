using System;
using System.Reflection;
using MelonLoader;

namespace AquaMai;

public static class AssemblyLoader
{
    private static readonly string[] AssemblyNames =
    [
        "AquaMai.Config.dll",
        "AquaMai.Core.dll",
        "AquaMai.Mods.dll",
    ];

    public static void LoadAssemblies()
    {
        foreach (var assemblyName in AssemblyNames)
        {
            LoadAssemblyFromResource(assemblyName);
        }
    }

    private static void LoadAssemblyFromResource(string assemblyName)
    {
        var executingAssembly = Assembly.GetExecutingAssembly();
        using var stream = executingAssembly.GetManifestResourceStream(assemblyName);
        var assemblyRawBytes = new byte[stream.Length];
        stream.Read(assemblyRawBytes, 0, assemblyRawBytes.Length);
        AppDomain.CurrentDomain.Load(assemblyRawBytes);
    }
}

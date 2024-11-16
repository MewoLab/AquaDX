using System;
using System.Collections.Generic;
using System.Reflection;
using MelonLoader;

namespace AquaMai;

public static class AssemblyLoader
{
    public enum AssemblyName
    {
        Config,
        Core,
        Mods,
    }

    private static readonly Dictionary<AssemblyName, string> Assemblies = new()
    {
        [AssemblyName.Config] = "AquaMai.Config.dll",
        [AssemblyName.Core] = "AquaMai.Core.dll",
        [AssemblyName.Mods] = "AquaMai.Mods.dll",
    };

    private static readonly Dictionary<AssemblyName, Assembly> LoadedAssemblies = [];

    public static Assembly GetAssembly(AssemblyName assemblyName) => LoadedAssemblies[assemblyName];

    public static void LoadAssemblies()
    {
        foreach (var (assemblyName, assemblyFileName) in Assemblies)
        {
            LoadedAssemblies[assemblyName] = LoadAssemblyFromResource(assemblyFileName);
        }
    }

    private static Assembly LoadAssemblyFromResource(string assemblyName)
    {
        var executingAssembly = Assembly.GetExecutingAssembly();
        using var stream = executingAssembly.GetManifestResourceStream(assemblyName);
        var assemblyRawBytes = new byte[stream.Length];
        stream.Read(assemblyRawBytes, 0, assemblyRawBytes.Length);
        return AppDomain.CurrentDomain.Load(assemblyRawBytes);
    }
}

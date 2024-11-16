using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using AquaMai.Config.Reflection;
using Mono.Cecil;

namespace AquaMai.Config.HeadlessLoader;

public class HeadlessLoader
{
    public static Config LoadFromPacked(byte[] assemblyBinary, AppDomain appDomain = null)
        => LoadFromPacked(new MemoryStream(assemblyBinary), appDomain);

    public static Config LoadFromPacked(Stream assemblyStream, AppDomain appDomain = null)
        => LoadFromPacked(AssemblyDefinition.ReadAssembly(assemblyStream), appDomain);

    public static Config LoadFromPacked(AssemblyDefinition assembly, AppDomain appDomain = null)
    {
        return LoadFromUnpacked(
            assembly.MainModule.Resources
                .Where(resource => resource.Name.ToLower().EndsWith(".dll"))
                .Select(resource => resource switch
                {
                    EmbeddedResource embeddedResource => embeddedResource.GetResourceData(),
                    _ => null
                })
                .Where(data => data != null),
            appDomain);
    }

    public static Config LoadFromUnpacked(IEnumerable<byte[]> assemblyBinariess, AppDomain appDomain = null) =>
        LoadFromUnpacked(assemblyBinariess.Select(binary => new MemoryStream(binary)), appDomain);

    public static Config LoadFromUnpacked(IEnumerable<Stream> assemblyStreams, AppDomain appDomain = null)
    {
        var resolver = new CustomAssemblyResolver();
        var assemblies = assemblyStreams
            .Select(
                assembly =>
                    AssemblyDefinition.ReadAssembly(
                        assembly,
                        new ReaderParameters() {
                            AssemblyResolver = resolver
                        }))
            .ToArray();
        foreach (var assembly in assemblies)
        {
            resolver.RegisterAssembly(assembly);
        }

        var configAssembly = assemblies.First(assembly => assembly.Name.Name == "AquaMai.Config");
        if (configAssembly == null)
        {
            throw new InvalidOperationException("AquaMai.Config assembly not found");
        }
        LoadAssemblyToApp(configAssembly, appDomain);
        var modsAssembly = assemblies.First(assembly => assembly.Name.Name == "AquaMai.Mods");
        if (modsAssembly == null)
        {
            throw new InvalidOperationException("AquaMai.Mods assembly not found");
        }
        return LoadConfig(modsAssembly);
    }

    private static Config LoadConfig(AssemblyDefinition modsAssembly)
    {
        var reflectionProvider = new MonoCecilReflectionProvider(modsAssembly);
        var reflectionManager = new ReflectionManager(reflectionProvider);
        return new Config(reflectionManager);
    }

    private static void LoadAssemblyToApp(AssemblyDefinition assembly, AppDomain appDomain = null)
    {
        var stream = new MemoryStream();
        assembly.Write(stream);
        if (appDomain == null)
        {
            appDomain = AppDomain.CurrentDomain;
        }
        appDomain.Load(stream.ToArray());
    }
}

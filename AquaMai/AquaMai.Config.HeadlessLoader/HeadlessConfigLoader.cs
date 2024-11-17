using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Mono.Cecil;

namespace AquaMai.Config.HeadlessLoader;

public class HeadlessConfigLoader
{
    public static HeadlessConfigInterface LoadFromPacked(string fileName)
        => LoadFromPacked(new FileStream(fileName, FileMode.Open));

    public static HeadlessConfigInterface LoadFromPacked(byte[] assemblyBinary)
        => LoadFromPacked(new MemoryStream(assemblyBinary));

    public static HeadlessConfigInterface LoadFromPacked(Stream assemblyStream)
        => LoadFromPacked(AssemblyDefinition.ReadAssembly(assemblyStream));

    public static HeadlessConfigInterface LoadFromPacked(AssemblyDefinition assembly)
    {
        return LoadFromUnpacked(
            ResourceLoader.LoadEmbeddedAssemblies(assembly).Values);
    }

    public static HeadlessConfigInterface LoadFromUnpacked(IEnumerable<byte[]> assemblyBinariess) =>
        LoadFromUnpacked(assemblyBinariess.Select(binary => new MemoryStream(binary)));

    public static HeadlessConfigInterface LoadFromUnpacked(IEnumerable<Stream> assemblyStreams)
    {
        var resolver = new CustomAssemblyResolver();
        var assemblies = assemblyStreams
            .Select(
                assemblyStream =>
                    AssemblyDefinition.ReadAssembly(
                        assemblyStream,
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
        var loadedConfigAssembly = ConfigAssemblyLoader.LoadConfigAssembly(configAssembly);
        var modsAssembly = assemblies.First(assembly => assembly.Name.Name == "AquaMai.Mods");
        if (modsAssembly == null)
        {
            throw new InvalidOperationException("AquaMai.Mods assembly not found");
        }
        return new(loadedConfigAssembly, modsAssembly);
    }
}

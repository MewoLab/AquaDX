using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using Mono.Cecil;

namespace AquaMai.Config.HeadlessLoader;

public class ResourceLoader
{
    private const string DLL_SUFFIX = ".dll";
    private const string COMPRESSED_SUFFIX = ".compressed";
    private const string DLL_COMPRESSED_SUFFIX = $"{DLL_SUFFIX}{COMPRESSED_SUFFIX}";

    public static Dictionary<string, Stream> LoadEmbeddedAssemblies(AssemblyDefinition assembly)
    {
        return assembly.MainModule.Resources
            .Where(resource => resource.Name.ToLower().EndsWith(DLL_SUFFIX) || resource.Name.ToLower().EndsWith(DLL_COMPRESSED_SUFFIX))
            .Select(LoadResource)
            .Where(data => data.Name != null)
            .ToDictionary(data => data.Name, data => data.Stream);
    }

    public static (string Name, Stream Stream) LoadResource(Resource resource)
    {
        if (resource is EmbeddedResource embeddedResource)
        {
            if (resource.Name.ToLower().EndsWith(COMPRESSED_SUFFIX))
            {
                var decompressedStream = new MemoryStream();
                using (var deflateStream = new DeflateStream(embeddedResource.GetResourceStream(), CompressionMode.Decompress))
                {
                    deflateStream.CopyTo(decompressedStream);
                }
                decompressedStream.Position = 0;
                return (resource.Name.Substring(0, resource.Name.Length - COMPRESSED_SUFFIX.Length), decompressedStream);
            }
            return (resource.Name, embeddedResource.GetResourceStream());
        }
        return (null, null);
    }
}

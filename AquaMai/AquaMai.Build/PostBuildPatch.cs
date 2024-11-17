using System;
using System.IO;
using System.IO.Compression;
using System.Linq;
using Microsoft.Build.Framework;
using Microsoft.Build.Utilities;
using Mono.Cecil;

public class PostBuildPatch : Task
{
    [Required]
    public string DllPath { get; set; }

    public override bool Execute()
    {
        try
        {
            var assembly = AssemblyDefinition.ReadAssembly(new MemoryStream(File.ReadAllBytes(DllPath)));
            CompressEmbeddedAssemblies(assembly);
            var outputStream = new MemoryStream();
            assembly.Write(outputStream);
            File.WriteAllBytes(DllPath, outputStream.ToArray());
            return true;
        }
        catch (Exception e)
        {
            Log.LogErrorFromException(e, true);
            return false;
        }
    }

    private void CompressEmbeddedAssemblies(AssemblyDefinition assembly)
    {
        foreach (var resource in assembly.MainModule.Resources.ToList())
        {
            if (resource.Name.EndsWith(".dll") && resource is EmbeddedResource embeddedResource)
            {
                using var compressedStream = new MemoryStream();
                using (var deflateStream = new DeflateStream(compressedStream, CompressionLevel.Optimal))
                {
                    embeddedResource.GetResourceStream().CopyTo(deflateStream);
                }
                var compressedBytes = compressedStream.ToArray();

                Log.LogMessage($"Compressed {resource.Name} from {embeddedResource.GetResourceStream().Length} to {compressedBytes.Length} bytes");

                assembly.MainModule.Resources.Remove(resource);
                assembly.MainModule.Resources.Add(new EmbeddedResource(resource.Name + ".compressed", resource.Attributes, compressedBytes));
            }
        }
    }
}

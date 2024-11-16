using System.Linq;
using System;
using System.IO;
using Mono.Cecil;

namespace AquaMai.Config.HeadlessLoader;

class ConfigAssemblyLoader
{
    public static void LoadConfigAssembly(AssemblyDefinition assembly, AppDomain appDomain = null)
    {
        var references = assembly.MainModule.AssemblyReferences;
        foreach (var reference in references)
        {
            if (reference.Name == "mscorlib" || reference.Name == "System" || reference.Name.StartsWith("System."))
            {
                reference.Name = "netstandard";
                reference.Version = new Version(2, 0, 0, 0);
                reference.PublicKeyToken = null;
            }
        }

        var stream = new MemoryStream();
        assembly.Write(stream);
        if (appDomain == null)
        {
            appDomain = AppDomain.CurrentDomain;
        }
        appDomain.Load(stream.ToArray());
    }
}

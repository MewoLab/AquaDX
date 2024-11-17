using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using Mono.Cecil;

namespace AquaMai.Config.HeadlessLoader;

class ConfigAssemblyLoader
{
    public static Assembly LoadConfigAssembly(AssemblyDefinition assembly)
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

        var targetFrameworkAttribute = assembly.CustomAttributes.FirstOrDefault(attr => attr.AttributeType.Name == "TargetFrameworkAttribute");
        if (targetFrameworkAttribute != null)
        {
            targetFrameworkAttribute.ConstructorArguments.Clear();
            targetFrameworkAttribute.ConstructorArguments.Add(new CustomAttributeArgument(
                assembly.MainModule.TypeSystem.String, ".NETStandard,Version=v2.0"));
            targetFrameworkAttribute.Properties.Clear();
            targetFrameworkAttribute.Properties.Add(new Mono.Cecil.CustomAttributeNamedArgument(
                "FrameworkDisplayName", new CustomAttributeArgument(assembly.MainModule.TypeSystem.String, ".NET Standard 2.0")));
        }

        var stream = new MemoryStream();
        assembly.Write(stream);
        FixLoadedAssemblyResolution();
        return AppDomain.CurrentDomain.Load(stream.ToArray());
    }

    private static bool FixedLoadedAssemblyResolution = false;

    // XXX: Why, without this, the already loaded assemblies are not resolved?
    public static void FixLoadedAssemblyResolution()
    {
        if (FixedLoadedAssemblyResolution)
        {
            return;
        }
        FixedLoadedAssemblyResolution = true;

        var loadedAssemblies = new Dictionary<string, Assembly>();
        foreach (var assembly in AppDomain.CurrentDomain.GetAssemblies())
        {
            loadedAssemblies[assembly.FullName] = assembly;
        }

        AppDomain.CurrentDomain.AssemblyResolve += (sender, args) =>
        {
            if (loadedAssemblies.TryGetValue(args.Name, out var assembly))
            {
                return assembly;
            }
            return null;
        };
    }
}

using System;
using System.Reflection;
using AquaMai.Config.Interfaces;
using Mono.Cecil;

namespace AquaMai.Config.HeadlessLoader;

public class HeadlessConfigInterface
{
    private readonly Assembly loadedConfigAssembly;

    public IReflectionProvider ReflectionProvider { get; init; }
    public IReflectionManager ReflectionManager { get; init; }

    public HeadlessConfigInterface(Assembly loadedConfigAssembly, AssemblyDefinition modsAssembly)
    {
        this.loadedConfigAssembly = loadedConfigAssembly;

        ReflectionProvider = Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.Reflection.MonoCecilReflectionProvider"), [modsAssembly]) as IReflectionProvider;
        ReflectionManager = Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.Reflection.ReflectionManager"), [ReflectionProvider]) as IReflectionManager;
    }

    public IConfig CreateConfig()
    {
        return Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.Config"), [ReflectionManager]) as IConfig;
    }

    public IConfigParser CreateConfigParser()
    {
        return Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.ConfigParser"), []) as IConfigParser;
    }

    public IConfigSerializer CreateConfigSerializer(IConfigSerializer.Options options)
    {
        return Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.ConfigSerializer"), [options]) as IConfigSerializer;
    }
}

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

    public string ApiVersion { get; init; }

    public HeadlessConfigInterface(Assembly loadedConfigAssembly, AssemblyDefinition modsAssembly)
    {
        this.loadedConfigAssembly = loadedConfigAssembly;

        ReflectionProvider = Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.Reflection.MonoCecilReflectionProvider"), [modsAssembly]) as IReflectionProvider;
        ReflectionManager = Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.Reflection.ReflectionManager"), [ReflectionProvider]) as IReflectionManager;
        ApiVersion = loadedConfigAssembly
            .GetType("AquaMai.Config.ApiVersion")
            .GetField("Version", BindingFlags.Public | BindingFlags.Static)
            .GetRawConstantValue() as string;
    }

    public IConfigView CreateConfigView(string tomlString = null)
    {
        return Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.ConfigView"),
            tomlString == null ? [] : [tomlString]) as IConfigView;
    }

    public IConfig CreateConfig()
    {
        return Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.Config"), [ReflectionManager]) as IConfig;
    }

    public IConfigParser GetConfigParser()
    {
        return loadedConfigAssembly
            .GetType("AquaMai.Config.ConfigParser")
            .GetField("Instance", BindingFlags.Public | BindingFlags.Static)
            .GetValue(null) as IConfigParser;
    }

    public IConfigSerializer CreateConfigSerializer(IConfigSerializer.Options options)
    {
        return Activator.CreateInstance(
            loadedConfigAssembly.GetType("AquaMai.Config.ConfigSerializer"), [options]) as IConfigSerializer;
    }

    public IConfigMigrationManager GetConfigMigrationManager()
    {
        return loadedConfigAssembly
            .GetType("AquaMai.Config.Migration.ConfigMigrationManager")
            .GetField("Instance", BindingFlags.Public | BindingFlags.Static)
            .GetValue(null) as IConfigMigrationManager;
    }
}

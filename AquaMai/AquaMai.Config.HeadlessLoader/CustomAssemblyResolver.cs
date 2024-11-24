using Mono.Cecil;

namespace AquaMai.Config.HeadlessLoader;

public class CustomAssemblyResolver : DefaultAssemblyResolver
{
    public new void RegisterAssembly(AssemblyDefinition assembly)
    {
        base.RegisterAssembly(assembly);
    } 
}

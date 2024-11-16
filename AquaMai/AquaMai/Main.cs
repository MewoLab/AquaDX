using System.Reflection;
using System.Runtime.InteropServices;
using MelonLoader;

namespace AquaMai;

public class AquaMai : MelonMod
{
    public const string AQUAMAI_SAY = """
                                      如果你在 dnSpy / ILSpy 里看到了这行字，请从 resources 中解包 DLLs。
                                      If you see this line in dnSpy / ILSpy, please unpack the DLLs from resources.
                                      """;

    [DllImport("kernel32.dll", SetLastError = true)]
    private static extern bool SetConsoleOutputCP(uint wCodePageID);

    public override void OnInitializeMelon()
    {
        // Prevent Chinese characters from being garbled
        SetConsoleOutputCP(65001);

        AssemblyLoader.LoadAssemblies();

        var modsAssembly = AssemblyLoader.GetAssembly(AssemblyLoader.AssemblyName.Mods);
        var coreAssembly = AssemblyLoader.GetAssembly(AssemblyLoader.AssemblyName.Core);
        coreAssembly.GetType("AquaMai.Core.Mod")
                    .GetMethod("Initialize", BindingFlags.Public | BindingFlags.Static)
                    .Invoke(null, [modsAssembly, HarmonyInstance]);
    }

    public override void OnGUI()
    {
        var coreAssembly = AssemblyLoader.GetAssembly(AssemblyLoader.AssemblyName.Core);
        coreAssembly.GetType("AquaMai.Core.Mod")
                    .GetMethod("OnGUI", BindingFlags.Public | BindingFlags.Static)
                    .Invoke(null, []);
        base.OnGUI();
    }
}

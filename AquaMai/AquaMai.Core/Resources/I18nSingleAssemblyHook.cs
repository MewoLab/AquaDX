using System.Globalization;
using System.Resources;
using HarmonyLib;

namespace AquaMai.Core.Resources;

public class I18nSingleAssemblyHook
{
    [HarmonyPatch(typeof(ResourceManager), "InternalGetResourceSet", typeof(CultureInfo), typeof(bool), typeof(bool))]
    [HarmonyPrefix]
    public static bool GetResourceSet(CultureInfo culture, bool createIfNotExists, bool tryParents, ref ResourceSet __result, ResourceManager __instance)
    {
        var GetResourceFileName = __instance.GetType().GetMethod("GetResourceFileName", System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.Instance);
        var resourceFileName = (string)GetResourceFileName.Invoke(__instance, [culture]);
        var ResourcesAssembly = typeof(I18nSingleAssemblyHook).Assembly;
        var manifestResourceStream = ResourcesAssembly.GetManifestResourceStream(resourceFileName);
        if (manifestResourceStream == null)
        {
            return true;
        }

        var resourceGroveler = __instance.GetType().GetField("resourceGroveler", System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.Instance).GetValue(__instance);
        var CreateResourceSet = resourceGroveler.GetType().GetMethod("CreateResourceSet", System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.Instance);
        var resourceSet = CreateResourceSet.Invoke(resourceGroveler, [manifestResourceStream, ResourcesAssembly]);
        var AddResourceSet = __instance.GetType().GetMethod("AddResourceSet", System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.Static);
        var localResourceSets = __instance.GetType().GetField("_resourceSets", System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.Instance).GetValue(__instance);
        object[] args = [localResourceSets, culture.Name, resourceSet];
        AddResourceSet.Invoke(null, args);
        __result = (ResourceSet)args[2];
        return false;
    }
}

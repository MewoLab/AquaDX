using System.Reflection;
using MelonLoader;

[assembly: AssemblyTitle(AquaMai.BuildInfo.Description)]
[assembly: AssemblyDescription(AquaMai.BuildInfo.Description)]
[assembly: AssemblyCompany(AquaMai.BuildInfo.Company)]
[assembly: AssemblyProduct(AquaMai.BuildInfo.Name)]
[assembly: AssemblyCopyright("Created by " + AquaMai.BuildInfo.Author)]
[assembly: AssemblyTrademark(AquaMai.BuildInfo.Company)]
[assembly: AssemblyVersion(AquaMai.BuildInfo.Version)]
[assembly: AssemblyFileVersion(AquaMai.BuildInfo.Version)]
[assembly: MelonInfo(typeof(AquaMai.AquaMai), AquaMai.BuildInfo.Name, AquaMai.BuildInfo.Version, AquaMai.BuildInfo.Author, AquaMai.BuildInfo.DownloadLink)]
[assembly: MelonColor()]
[assembly: HarmonyDontPatchAll]

// Create and Setup a MelonGame Attribute to mark a Melon as Universal or Compatible with specific Games.
// If no MelonGame Attribute is found or any of the Values for any MelonGame Attribute on the Melon is null or empty it will be assumed the Melon is Universal.
// Values for MelonGame Attribute can be found in the Game's app.info file or printed at the top of every log directly beneath the Unity version.
[assembly: MelonGame(null, null)]

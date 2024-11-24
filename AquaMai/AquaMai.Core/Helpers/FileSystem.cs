using System;
using System.IO;

namespace AquaMai.Core.Helpers;

public static class FileSystem
{
    public static string ResolvePath(string path)
    {
        var varExpanded = Environment.ExpandEnvironmentVariables(path);
        return Path.IsPathRooted(varExpanded)
                 ? varExpanded
                 : Path.Combine(Environment.CurrentDirectory, varExpanded);
    }
}

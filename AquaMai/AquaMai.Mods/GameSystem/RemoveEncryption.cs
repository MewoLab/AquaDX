using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using AquaMai.Config.Attributes;
using AquaMai.Core.Helpers;
using HarmonyLib;
using Net.Packet;

namespace AquaMai.Mods.GameSystem;

[ConfigSection(
    en: """
        If you are using an unmodified client, requests to the server will be encrypted by default, but requests to the private server should not be encrypted.
        With this option enabled, the connection will not be encrypted, and the suffix added by different versions of the client to the API names are also removed.
        Please keep this option enabled normally.
        """,
    zh: """
        如果你在用未经修改的客户端，会默认加密到服务器的连接，而连接私服的时候不应该加密
        开了这个选项之后就不会加密连接了，同时也会移除不同版本的客户端可能会对 API 接口加的后缀
        正常情况下，请保持这个选项开启
        """,
    defaultOn: true)]
public class RemoveEncryption
{
    [HarmonyPrefix]
    [HarmonyPatch(typeof(Packet), "Obfuscator", typeof(string))]
    public static bool PreObfuscator(string srcStr, ref string __result)
    {
        __result = Shim.RemoveApiSuffix(srcStr);
        return false;
    }

    [HarmonyPatch]
    public class EncryptDecrypt
    {
        public static IEnumerable<MethodBase> TargetMethods()
        {
            var methods = AccessTools.TypeByName("Net.CipherAES").GetMethods();
            return
            [
                methods.FirstOrDefault(it => it.Name == "Encrypt" && it.IsPublic),
                methods.FirstOrDefault(it => it.Name == "Decrypt" && it.IsPublic)
            ];
        }

        public static bool Prefix(object[] __args, ref object __result)
        {
            if (__args.Length == 1)
            {
                // public static byte[] Encrypt(byte[] data)
	            // public static byte[] Decrypt(byte[] encryptData)
                __result = __args[0];
            }
            else if (__args.Length == 2)
            {
                // public static bool Encrypt(byte[] data, out byte[] encryptData)
	            // public static bool Decrypt(byte[] encryptData, out byte[] plainData)
                __args[1] = __args[0];
                __result = true;
            }
            return false;
        }
    }
}

using System;

namespace AquaMai.Config.Attributes;

public record ConfigComment(string CommentEn, string CommentZh)
{
    public string GetLocalized(string lang) => lang switch
    {
        "en" => CommentEn ?? "",
        "zh" => CommentZh ?? "",
        _ => throw new ArgumentException($"Unsupported language: {lang}")
    };
}

using System.Collections.Generic;
using System.Linq;
using DB;
using Manager;
using MelonLoader;
using UnityEngine;

namespace AquaMai.Mods.Fancy.GamePlay.CustomNoteTypes.Libs;

public class CustomSlideNoteData: NoteData
{
    public string SlideCode;
    public List<List<Vector4>> SlidePathList = new List<List<Vector4>>();
    public List<List<SlideManager.HitArea>> SlideHitAreasList = new List<List<SlideManager.HitArea>>();
    public float SlidePathLength;

    public bool ParseSlideCode(string slideCode, OptionMirrorID mirrorMode)
    {
        if (string.IsNullOrEmpty(slideCode))
        {
            return false;
        }
        
        SlidePathList.Clear();
        SlideHitAreasList.Clear();
        
        this.SlideCode = slideCode;
        var path = SlideCodeParser.Parse(slideCode);
        if (path == null)
        {
            return false;
        }

        var arrowData = SlideDataBuilder.BuildArrowData(path);
        SlidePathLength = (float)path.GetPathLength();
        var hitAreaData = SlideDataBuilder.BuildHitAreas(path);
        for (var i = 0; i < 8; i++)
        {
            SlidePathList.Add(SlideDataBuilder.ConvertAndRotateArrowData(arrowData, i, mirrorMode));
            SlideHitAreasList.Add(SlideDataBuilder.ConvertAndRotateHitAreas(hitAreaData, i, mirrorMode));
        }

        var msg = string.Join(", ",
            hitAreaData.Select(x => x.PanelAreas).Select(x => string.Join("/", x.Cast<InputManager.TouchPanelArea>())));
        MelonLogger.Msg(msg);

        this.slideData.type = path.GetEndType(mirrorMode);

        return true;
    }
}

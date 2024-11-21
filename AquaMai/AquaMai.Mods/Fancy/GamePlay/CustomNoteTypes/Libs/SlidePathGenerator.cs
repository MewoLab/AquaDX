using System;
using System.Collections.Generic;
using System.Numerics;

namespace AquaMai.Mods.Fancy.GamePlay.CustomNoteTypes.Libs;

public class SlidePathGenerator
{
    public List<ParametricSlidePath.PathSegment> PathSegments = new List<ParametricSlidePath.PathSegment>();
    public Complex CurrentEndPoint = Complex.Zero;

    public static SlidePathGenerator BeginAt(Complex point)
    {
        var obj = new SlidePathGenerator();
        obj.CurrentEndPoint = point;
        return obj;
    }

    public static double CalcTangentAngle(Complex point, MaiGeometry.CircleStruct circle, bool isCcw)
    {
        var hypot = point - circle.Center;
        var angleDelta = Math.Acos(circle.Radius / hypot.Magnitude);
        var tanAngle = hypot.Phase + (isCcw ? angleDelta : -angleDelta);
        return Math.IEEERemainder(tanAngle, Math.PI * 2.0);
    }

    public void TrySetLastParseMarker(ParametricSlidePath.ParseMarker marker)
    {
        if (PathSegments.Count <= 0) return;
        PathSegments[PathSegments.Count - 1].SetParseMarker(marker);
    }

    public void LineToPoint(Complex point)
    {
        PathSegments.Add(new ParametricSlidePath.LineSegment(CurrentEndPoint, point));
        CurrentEndPoint = point;
    }

    public void TangentToCircle(MaiGeometry.CircleStruct circle, bool isCcw)
    {
        var inAngle = CalcTangentAngle(CurrentEndPoint, circle, isCcw);
        var inPoint = Complex.FromPolarCoordinates(circle.Radius, inAngle) + circle.Center;
        LineToPoint(inPoint);
    }
    
    /// <summary>Note: endAngle should be in range [-PI, PI]</summary>
    public void ArcToAngle(Complex center, double endAngle, bool isCcw, bool skipIfZero)
    {
        var diff = CurrentEndPoint - center;
        var circle = new MaiGeometry.CircleStruct(center, diff.Magnitude);
        var startAngle = diff.Phase;
        // startAngle and endAngle in range [-PI, PI]
        if (isCcw)
        {
            if (startAngle > endAngle)
            {
                startAngle -= 2 * Math.PI;
            }

            if (Math.Abs(endAngle - startAngle) < 0.001)
            {
                if (skipIfZero) return;
                endAngle += 2 * Math.PI;
            }
        }
        else
        {
            if (startAngle < endAngle)
            {
                startAngle += 2 * Math.PI;
            }

            if (Math.Abs(endAngle - startAngle) < 0.001)
            {
                if (skipIfZero) return;
                endAngle -= 2 * Math.PI;
            }
        }
        
        var seg = new ParametricSlidePath.ArcSegment(circle, startAngle, endAngle);
        PathSegments.Add(seg);
        CurrentEndPoint = seg.GetPointAt(1f);
    }

    public void ArcToTangentTowards(Complex target, Complex center, bool isCcw)
    {
        var diff = CurrentEndPoint - center;
        var endAngle = CalcTangentAngle(target, new MaiGeometry.CircleStruct(center, diff.Magnitude), !isCcw);
        ArcToAngle(center, endAngle, isCcw, false);
    }

    public void FullCircle(Complex center, bool isCcw)
    {
        var diff = CurrentEndPoint - center;
        var circle = new MaiGeometry.CircleStruct(center, diff.Magnitude);
        PathSegments.Add(new ParametricSlidePath.CircleSegment(circle, diff.Phase, isCcw));
        // CurrentEndPoint not changed
    }

    public void ExternTangentTransfer(Complex currentCenter, MaiGeometry.CircleStruct targetCircle, bool isCcw)
    {
        var diff = CurrentEndPoint - currentCenter;
        double endAngle;
        if (Math.Abs(diff.Magnitude - targetCircle.Radius) < 0.001)
        {
            // two circles are approximately same radius
            var vector = targetCircle.Center - currentCenter;
            vector *= isCcw ? -Complex.ImaginaryOne : Complex.ImaginaryOne;
            endAngle = vector.Phase;
        }
        else if (targetCircle.Radius > diff.Magnitude)
        {
            // target circle larger
            var helperCircle = new MaiGeometry.CircleStruct(targetCircle.Center, targetCircle.Radius - diff.Magnitude);
            endAngle = CalcTangentAngle(currentCenter, helperCircle, isCcw);
        }
        else
        {
            var helperCircle = new MaiGeometry.CircleStruct(currentCenter, diff.Magnitude - targetCircle.Radius);
            endAngle = CalcTangentAngle(targetCircle.Center, helperCircle, !isCcw);
        }
        ArcToAngle(currentCenter, endAngle, isCcw, false);
        var inPoint = Complex.FromPolarCoordinates(targetCircle.Radius, endAngle) + targetCircle.Center;
        LineToPoint(inPoint);
    }

    public ParametricSlidePath GeneratePath()
    {
        return new ParametricSlidePath(PathSegments);
    }
}

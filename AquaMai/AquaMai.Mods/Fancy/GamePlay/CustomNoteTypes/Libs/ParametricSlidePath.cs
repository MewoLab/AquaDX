using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using DB;
using Manager;

namespace AquaMai.Mods.Fancy.GamePlay.CustomNoteTypes.Libs;

public class ParametricSlidePath
{
    public enum ParseMarker
    {
        None = 0,
        SmoothAlign,
        ForceAlign,
        SharpCorner
    }
    
    public abstract class PathSegment
    {
        public ParseMarker ParseMarker = ParseMarker.None;
        public double ArrowDistance = MaiGeometry.DefaultDistance;

        public abstract bool DoAngleLerp { get; }

        public abstract Complex GetPointAt(double t);
        
        public abstract Complex GetTangentAt(double t);
        
        public abstract double GetSegmentLength();

        public void SetParseMarker(ParseMarker marker) => ParseMarker = marker;
        
        public void SetArrowDistance(double distance) => ArrowDistance = distance;
    }
    
    public class LineSegment(Complex start, Complex end) : PathSegment
    {
        public readonly Complex StartPoint = start;
        public readonly Complex EndPoint = end;
        
        public override bool DoAngleLerp { get; } = false;

        public override Complex GetPointAt(double t)
        {
            return StartPoint + (EndPoint - StartPoint) * t;
        }

        public override Complex GetTangentAt(double t)
        {
            var v = EndPoint - StartPoint;
            return v / v.Magnitude;
        }

        public override double GetSegmentLength()
        {
            return (EndPoint - StartPoint).Magnitude;
        }
    }
    
    public class ArcSegment(MaiGeometry.CircleStruct circle, double startAngle, double endAngle) : PathSegment
    {
        public readonly MaiGeometry.CircleStruct Circle = circle;
        public readonly double StartAngle = startAngle;
        public readonly double EndAngle = endAngle;
        
        public override bool DoAngleLerp { get; } = true;

        public override Complex GetPointAt(double t)
        {
            var angle = StartAngle + t * (EndAngle - StartAngle);
            return Circle.Center + Complex.FromPolarCoordinates(Circle.Radius, angle);
        }

        public override Complex GetTangentAt(double t)
        {
            var angle = StartAngle + t * (EndAngle - StartAngle);
            if (StartAngle < EndAngle)
            {
                return Complex.FromPolarCoordinates(1, angle) * Complex.ImaginaryOne;
            }
            else
            {
                return Complex.FromPolarCoordinates(-1, angle) * Complex.ImaginaryOne;
            }
        }

        public override double GetSegmentLength()
        {
            return Math.Abs(EndAngle - StartAngle) * Circle.Radius;
        }
    }

    public class CircleSegment(MaiGeometry.CircleStruct circle, double startAngle, bool isCcw) : PathSegment
    {
        public readonly MaiGeometry.CircleStruct Circle = circle;
        public readonly double StartAngle = startAngle;
        public readonly bool IsCcw = isCcw;
        
        public override bool DoAngleLerp { get; } = true;

        public override Complex GetPointAt(double t)
        {
            double angle;
            if (IsCcw)
            {
                angle = StartAngle + t * Math.PI * 2f;
            }
            else
            {
                angle = StartAngle - t * Math.PI * 2f;
            }

            return Circle.Center + Complex.FromPolarCoordinates(Circle.Radius, angle);
        }

        public override Complex GetTangentAt(double t)
        {
            double angle;
            if (IsCcw)
            {
                angle = StartAngle + t * Math.PI * 2f;
                return Complex.FromPolarCoordinates(1, angle) * Complex.ImaginaryOne;
            }
            else
            {
                angle = StartAngle - t * Math.PI * 2f;
                return Complex.FromPolarCoordinates(-1, angle) * Complex.ImaginaryOne;
            }
        }

        public override double GetSegmentLength()
        {
            return Math.PI * Circle.Radius * 2;
        }
    }
    
    
    public readonly PathSegment[] Segments;
    public readonly double[] Fractions;
    public readonly double[] AccumulatedLengths;

    public ParametricSlidePath(IEnumerable<PathSegment> pathSegments)
    {
        Segments = pathSegments.ToArray();
        if (Segments.Length == 0)
        {
            throw new ArgumentException("At least one path segment is required.");
        }
        var lengths = Segments.Select(s => s.GetSegmentLength());
        var sum = 0.0;
        AccumulatedLengths = lengths.Select(x => (sum += x)).ToArray();
        Fractions = AccumulatedLengths.Select(x => x / sum).ToArray();
    }

    public PathSegment GetSegmentAt(double t, out double segmentT)
    {
        if (t <= 0.0)
        {
            segmentT = 0.0;
            return Segments[0];
        }

        if (t >= 1.0)
        {
            segmentT = 1.0;
            return Segments[Segments.Length - 1];
        }

        var idx = Array.BinarySearch(Fractions, t);
        if (idx < 0)
        {
            idx = ~idx;  // first entry > t
        }
        // if idx >= 0 then idx is the entry == t
        // so Fractions[idx-1] < t and Fractions[idx] >= t
        // Note: Fractions[i] marks the end point of Segments[i]

        if (idx >= Segments.Length)
        {
            segmentT = 1.0;
            return Segments[Segments.Length - 1];
        }

        if (idx == 0)
        {
            segmentT = t / Fractions[0];
            return Segments[0];
        }
            
        segmentT = (t - Fractions[idx - 1]) / (Fractions[idx] - Fractions[idx - 1]);
        return Segments[idx];
    }
    
    public double GetPathLength() => AccumulatedLengths[AccumulatedLengths.Length - 1];

    public Complex GetPointAt(double t)
    {
        var segment = GetSegmentAt(t, out var segT);
        return segment.GetPointAt(segT);
    }

    public Complex GetTangentAt(double t)
    {
        var segment = GetSegmentAt(t, out var segT);
        return segment.GetTangentAt(segT);
    }

    public SlideType GetEndType(OptionMirrorID mirrorMode)
    {
        var lastSegment = Segments[Segments.Length - 1];
        var flip = mirrorMode == OptionMirrorID.LR || mirrorMode == OptionMirrorID.UD;
        if (lastSegment is CircleSegment circle)
        {
            return circle.IsCcw != flip ? SlideType.Slide_Circle_L : SlideType.Slide_Circle_R;
        }

        if (lastSegment is ArcSegment arc)
        {
            return (arc.EndAngle > arc.StartAngle) != flip ? SlideType.Slide_Circle_L : SlideType.Slide_Circle_R;
        }

        return SlideType.Slide_Straight;
    }
}

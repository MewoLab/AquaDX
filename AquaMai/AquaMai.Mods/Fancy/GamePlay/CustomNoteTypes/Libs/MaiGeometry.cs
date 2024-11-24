using System;
using System.Numerics;

namespace AquaMai.Mods.Fancy.GamePlay.CustomNoteTypes.Libs;

public static class MaiGeometry
{
    public struct CircleStruct(Complex center, double radius)
    {
        public Complex Center = center;
        public double Radius = radius;
    }

    public static readonly double CanvasWidth = 1080.0;
    public static readonly double MainRadius  = 480.0;
    public static readonly double CenterRadius = MainRadius * Math.Cos(Math.PI * 3 / 8);
    public static readonly double GroupBRadius = CenterRadius / Math.Cos(Math.PI / 8);

    private static readonly double _b = Math.Cos(Math.PI / 8) / 2;
    private static readonly double _a = 1 - _b;
    private static readonly double _theta = Math.PI / 4;
    private static readonly double _s = (_a * _a + _b * _b - 2 * _a * _b * Math.Cos(_theta)) /
                                        (2 * _a - 2 * _b * Math.Cos(_theta));
    
    public static readonly double PPQQRadius = MainRadius * _b;
    public static readonly double TransferRadius = MainRadius * (_b + _s);
    public static readonly double EdgeTransferAngle = _theta;
    public static readonly double PPQQTransferAngle =
        Math.Acos((_s * _s + _b * _b - (_a - _s) * (_a - _s)) / (2 * _b * _s));
    
    public static readonly double DefaultDistance = MainRadius * Math.PI / 32;

    public static readonly int[,] MirrorInfo = new int[4, 17]
    {
        { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }, // Normal
        { 7, 6, 5, 4, 3, 2, 1, 0, 15, 14, 13, 12, 11, 10, 9, 8, 16 }, // L <-> R
        { 3, 2, 1, 0, 7, 6, 5, 4, 11, 10, 9, 8, 15, 14, 13, 12, 16 }, // U <-> D
        { 4, 5, 6, 7, 0, 1, 2, 3, 12, 13, 14, 15, 8, 9, 10, 11, 16 } // rotate 180 deg
    };

    /// <summary>
    /// Note: idx is 1-based, not 0-based
    /// </summary>
    public static Complex PointGroupA(int idx)
    {
        var angle = Math.PI * (5.0 / 8.0 - idx / 4.0);
        return Complex.FromPolarCoordinates(MainRadius, angle);
    }
    
    /// <summary>
    /// Note: idx is 1-based, not 0-based
    /// </summary>
    public static Complex PointGroupB(int idx)
    {
        var angle = Math.PI * (5.0 / 8.0 - idx / 4.0);
        return Complex.FromPolarCoordinates(GroupBRadius, angle);
    }
    
    public static Complex Center()
    {
        return Complex.Zero;
    }

    /// <summary>
    /// idx 0 is center circle, idx 1~8 are ppqq circles, idx 9 is outer circle
    /// </summary>
    public static CircleStruct GetCircle(int idx)
    {
        if (idx == 0)
        {
            return new CircleStruct(Complex.Zero, CenterRadius);
        }

        if (idx == 9)
        {
            return new CircleStruct(Complex.Zero, MainRadius);
        }

        var angle = Math.PI * (3.0 / 4.0 - idx / 4.0);
        var center = Complex.FromPolarCoordinates(PPQQRadius, angle);
        return new CircleStruct(center, PPQQRadius);
    }

    /// <summary>
    /// Note: idx is 1-based, not 0-based
    /// </summary>
    /// <returns>CircleStruct TransferCircle, double TransferStartAngle, double TransferEndAngle</returns>
    public static Tuple<CircleStruct, double, double> TransferOutData(int idx, bool isccw)
    {
        var ppqqRad = Math.PI * (3.0 / 4.0 - idx / 4.0);
        double startAngle, endAngle;
        if (isccw)
        {
            startAngle = ppqqRad - PPQQTransferAngle;
            endAngle = ppqqRad + EdgeTransferAngle;
        }
        else
        {
            startAngle = ppqqRad + PPQQTransferAngle;
            endAngle = ppqqRad - EdgeTransferAngle;
        }
        var d = MainRadius - TransferRadius;
        var center = Complex.FromPolarCoordinates(d, endAngle);
        return new Tuple<CircleStruct, double, double>(new CircleStruct(center, TransferRadius),
            Math.IEEERemainder(startAngle, Math.PI * 2), Math.IEEERemainder(endAngle, Math.PI * 2));
    }
}

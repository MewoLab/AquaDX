using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using MelonLoader;

namespace AquaMai.Mods.MaimaiDX2077;

public static class SlideCodeParser
{
    public enum CommandType
    {
        Invalid = -1,
        NodeA = 0,
        NodeB = 1,
        NodeC = 2,
        OrbitCCW = 3,
        OrbitCW = 4,
        NodeEnd = 5
    }

    public struct Command(CommandType type, int value)
    {
        public CommandType Type = type;
        public int Value = value;
        
        public static bool IsSame(Command a, Command b)
        {
            return a.Type == b.Type && a.Value == b.Value;
        }
    }

    public static readonly char[] CommandChars =
    [
        'A', 'B', 'C', 'P', 'Q', 'K'
    ];

    public static int TryParseDigit(char c)
    {
        if (c >= '0' && c <= '9') return c - '0';
        return -1;
    }

    public static List<Command> ParseCommands(string code)
    {
        if (!CommandChars.Contains(code[1]))
        {
            throw new ArgumentException($"the 2nd char should be a command");
        }

        if (code[code.Length - 2] != 'K')
        {
            throw new ArgumentException($"should end with 'K' command");
        }
        
        var commands = new List<Command>();
        var currentType = CommandType.NodeA;
        var value = TryParseDigit(code[0]);
        if (value < 0) throw new ArgumentException($"invalid char '{code[0]}'");
        
        commands.Add(new Command(currentType, value));
        
        for (var ptr = 1; ptr < code.Length; ptr++)
        {
            var ch = code[ptr];
            if (CommandChars.Contains(ch))
            {
                currentType = (CommandType) Array.IndexOf(CommandChars, ch);
                if (currentType == CommandType.NodeC)
                {
                    commands.Add(new Command(CommandType.NodeC, 0));
                }
            }
            else
            {
                value = TryParseDigit(ch);
                if (value < 0) throw new ArgumentException($"invalid char '{ch}'");
                if (currentType == CommandType.NodeC)
                {
                    throw new ArgumentException($"digit should not follow 'C'");
                }
                commands.Add(new Command(currentType, value));
            }
        }
        return commands;
    }

    public static Complex GetNodePosition(Command cmd)
    {
        switch (cmd.Type)
        {
            case CommandType.NodeA:
            case CommandType.NodeEnd:
                return MaiGeometry.PointGroupA(cmd.Value);
            case CommandType.NodeB:
                return MaiGeometry.PointGroupB(cmd.Value);
            case CommandType.NodeC:
                return MaiGeometry.Center();
            default:
                throw new ArgumentException($"invalid type for node: {cmd.Type}");
        }
    }

    public static void NodeToNode(SlidePathGenerator generator, Command last, Command current)
    {
        if (Command.IsSame(last, current)) return;
        generator.LineToPoint(GetNodePosition(current));
    }

    public static void NodeToOrbit(SlidePathGenerator generator, Command last, Command current)
    {
        var isCcw = (current.Type == CommandType.OrbitCCW);
        var node = GetNodePosition(last);
        var orbit = MaiGeometry.GetCircle(current.Value);
        var diff = node - orbit.Center;
        if (Math.Abs(diff.Magnitude - orbit.Radius) < 0.1)
        {
            if (last.Type == CommandType.NodeA && current.Value == 9)
            {
                generator.TrySetLastParseMarker(ParametricSlidePath.ParseMarker.ForceAlign);
            }
            return;  // node on circle, do nothing
        }

        if (diff.Magnitude < orbit.Radius)
            throw new ArgumentException($"impossible: {last.Type}{last.Value} -> Orbit{current.Value}");
        
        generator.TangentToCircle(orbit, isCcw);
    }
    
    public static void OrbitToNode(SlidePathGenerator generator, Command last, Command current)
    {
        var isCcw = (last.Type == CommandType.OrbitCCW);
        var node = GetNodePosition(current);
        var orbit = MaiGeometry.GetCircle(last.Value);
        var diff = node - orbit.Center;
        if (Math.Abs(diff.Magnitude - orbit.Radius) < 0.1)
        {
            generator.ArcToAngle(orbit.Center, diff.Phase, isCcw, false);
            return;
        }

        if (diff.Magnitude < orbit.Radius)
            throw new ArgumentException($"impossible: Orbit{last.Value} -> {current.Type}{current.Value}");
        
        generator.ArcToTangentTowards(node, orbit.Center, isCcw);
        generator.LineToPoint(node);
    }
    
    public static void OrbitToOrbit(SlidePathGenerator generator, Command last, Command current)
    {
        if (current.Type != last.Type) throw new ArgumentException($"orbit type mismatch");

        var isCcw = (last.Type == CommandType.OrbitCCW);
        var lastOrbit = MaiGeometry.GetCircle(last.Value);
        var currentOrbit = MaiGeometry.GetCircle(current.Value);
        if (current.Value == last.Value)
        {
            generator.FullCircle(lastOrbit.Center, isCcw);
            return;
        }

        if (last.Value == 0 && current.Value == 9 || last.Value == 9 && current.Value == 0)
            throw new ArgumentException($"impossible: Orbit{last.Value} -> Orbit{current.Value}");

        if (current.Value == 9)
        {
            var data = MaiGeometry.TransferOutData(last.Value, isCcw);
            generator.ArcToAngle(lastOrbit.Center, data.Item2, isCcw, false);
            generator.ArcToAngle(data.Item1.Center, data.Item3, isCcw, false);
            generator.TrySetLastParseMarker(ParametricSlidePath.ParseMarker.SmoothAlign);
            return;
        }

        if (last.Value == 9)
        {
            var data = MaiGeometry.TransferOutData(current.Value, !isCcw);
            generator.ArcToAngle(lastOrbit.Center, data.Item3, isCcw, true);
            generator.ArcToAngle(data.Item1.Center, data.Item2, isCcw, false);
            return;
        }
        
        generator.ExternTangentTransfer(lastOrbit.Center, currentOrbit, isCcw);
    }
    
    public static ParametricSlidePath Parse(string code)
    {
        try
        {
            var commands = ParseCommands(code);
            var lastCmd = commands[0];
            // The first command is guarantee to be 'A'
            var generator = SlidePathGenerator.BeginAt(MaiGeometry.PointGroupA(lastCmd.Value));

            for (var i = 1; i < commands.Count; i++)
            {
                var cmd = commands[i];
                switch (cmd.Type)
                {
                    case CommandType.NodeA:
                    case CommandType.NodeB:
                    case CommandType.NodeC:
                    case CommandType.NodeEnd:
                        switch (lastCmd.Type)
                        {
                            case CommandType.NodeA:
                            case CommandType.NodeB:
                            case CommandType.NodeC:
                                NodeToNode(generator, lastCmd, cmd);
                                break;
                            case CommandType.OrbitCCW:
                            case CommandType.OrbitCW:
                                OrbitToNode(generator, lastCmd, cmd);
                                break;
                            case CommandType.NodeEnd:
                                throw new ArgumentException($"'K' should be the last command");
                            default:
                                throw new ArgumentOutOfRangeException();
                        }
                        break;
                    case CommandType.OrbitCCW:
                    case CommandType.OrbitCW:
                        switch (lastCmd.Type)
                        {
                            case CommandType.NodeA:
                            case CommandType.NodeB:
                            case CommandType.NodeC:
                                NodeToOrbit(generator, lastCmd, cmd);
                                break;
                            case CommandType.OrbitCCW:
                            case CommandType.OrbitCW:
                                OrbitToOrbit(generator, lastCmd, cmd);
                                break;
                            case CommandType.NodeEnd:
                                throw new ArgumentException($"'K' should be the last command");
                            default:
                                throw new ArgumentOutOfRangeException();
                        }
                        break;
                    default:
                        throw new ArgumentOutOfRangeException();
                }

                lastCmd = cmd;
            }

            return generator.GeneratePath();
        }
        catch (ArgumentException e)
        {
            var msg = $"Invalid code: {code}";
            if (e.Message != "")
            {
                msg += $", {e.Message}";
            }
            MelonLogger.Error(msg);
            return null;
        }
    }
}

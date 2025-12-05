namespace AOC._2025.day_5;


  public class Results
  {
      public static string INPUT = "";
      private static List<string> id_ranges = new List<string>();
      private static List<long> avail_ingredients = new List<long>();

      public static void Main(string[] args)
      {
        Console.WriteLine("AOC 2025 - Day 5");
        ParseInput();
        int part1 = SolvePart1(id_ranges, avail_ingredients);
        Console.WriteLine($"Part 1: {part1}");
        long part2 = SolvePart2(id_ranges);
        Console.WriteLine($"Part 2: {part2}");
      }

      private static int SolvePart1(List<string> id_ranges, List<long> avail_ingredients)
      {
        int fresh = 0;
        foreach(long ingredient in avail_ingredients)
        {
          foreach(string range in id_ranges)
          {
            string[] bounds = range.Split("-");
            long lower = long.Parse(bounds[0]);
            long upper = long.Parse(bounds[1]);
            if(ingredient >= lower && ingredient <= upper)
            {
              fresh++;
              break; // Only count each ingredient once, even if it matches multiple ranges
            }
          }
        }
        return fresh;
      }

      private static long SolvePart2(List<string> id_ranges)
      {
        // Sort ranges by lower bound
        var sortedRanges = id_ranges
            .Select(r => r.Split("-"))
            .Select(bounds => (lower: long.Parse(bounds[0]), upper: long.Parse(bounds[1])))
            .OrderBy(r => r.lower)
            .ToList();
        
        // Merge overlapping ranges
        var mergedRanges = new List<(long lower, long upper)>();
        foreach (var range in sortedRanges)
        {
            if (mergedRanges.Count == 0 || mergedRanges[^1].upper < range.lower - 1)
            {
                mergedRanges.Add(range);
            }
            else
            {
                var last = mergedRanges[^1];
                mergedRanges[^1] = (last.lower, Math.Max(last.upper, range.upper));
            }
        }
        
        // Calculate total fresh IDs (sum of all merged range sizes)
        long freshCount = mergedRanges.Sum(r => r.upper - r.lower + 1);
        
        return freshCount;
      }

      private static void ParseInput()
      {
        INPUT = File.ReadAllText("input/input.txt");
        foreach (string line in INPUT.Split("\n"))
        {
          if(line.Contains("-"))
          {
            id_ranges.Add(line.Trim());
          }
          else
          {
            if(line.Trim() != "")
              avail_ingredients.Add(long.Parse(line.Trim()));
          }
        }
      }
  }

//package io.github.javaasasecondlanguage.homework01.mergeintervals;
import java.util.List;
import java.util.stream.Collectors;

public class IntervalsMerger {
    /**
     * Given array of intervals, merges overlapping intervals and sort by start in ascending order
     * Interval is defined as [start,end] where start < end
     * <p>
     * Examples:
     * [[1,3][2,4][5,6]] -> [[1,4][5,6]]
     * [[1,2][2,3]] -> [[1,3]]
     * [[1,4][2,3]] -> [[1,4]]
     * [[5,6][1,2]] -> [[1,2][5,6]]
     *
     * @param intervals nullable array of pairs [start, end]
     * @return merged intervals
     * @throws IllegalArgumentException if intervals is null
     */
    public static int[][] merge(int[][] intervals) {
		List<Integer> sortedList = java.util.Arrays.asList(intervals).stream()
			.sorted((o1, o2) -> o1[0] - o2[0])
			.flatMapToInt(a -> java.util.Arrays.asList(a).stream().map(Integer::new))
			.collect(Collectors.toList());

		return intervals;
    }

	public static void main(String [] argv) {
		int [][]intervals = {{0, 10}, {12, 20}};

		merge(intervals);

	}
}

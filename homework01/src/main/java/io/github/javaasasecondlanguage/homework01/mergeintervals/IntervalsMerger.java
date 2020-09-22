package io.github.javaasasecondlanguage.homework01.mergeintervals;

import java.util.Arrays;
import java.util.Comparator;

public class IntervalsMerger {
    /**
     * Given array of intervals, merge overlapping intervals and
     * sort them by start in ascending order
     * Interval is defined as [start, end] where start < end
     * <p>
     * Examples:
     * [[1,3][2,4][5,6]] -> [[1,4][5,6]]
     * [[1,2][2,3]] -> [[1,3]]
     * [[1,4][2,3]] -> [[1,4]]
     * [[5,6][1,2]] -> [[1,2][5,6]]
     *
     * @param intervals is a nullable array of pairs [start, end]
     * @return merged intervals
     * @throws IllegalArgumentException if intervals is null
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals == null) {
            throw new IllegalArgumentException("Null argument is not allowed");
        }

        int [][]sorted = Arrays.copyOf(intervals, intervals.length);
        int []flattened = new int[sorted.length * 2];
        int []filtered = new int[sorted.length * 2];

        if (intervals.length == 0) {
            return sorted;
        }

        Arrays.sort(sorted, Comparator.comparingInt(a -> a[0]));

        for (int n = 0; n < sorted.length; n++) {
            flattened[2 * n] = sorted[n][0];
            flattened[2 * n  + 1] = sorted[n][1];
        }

        int idx = 0;
        int start = flattened[0];
        int end = flattened[1];

        for (int n = 2; n < flattened.length; n += 2) {
            int nextStart = flattened[n];
            int nextEnd = flattened[n + 1];

            if (end < nextStart) {
                // [start  ; end]
                //               [nextStart ; nextEnd]
                filtered[idx] = start;
                filtered[idx + 1] = end;
                start = nextStart;
                end = nextEnd;
                idx += 2;

                continue;
            }

            // [start         ;        end]
            //    [ nextStart ; nextEnd]
            if (nextEnd <= end) {
                continue;
            }

            // [start         ;        end]
            //                 [ nextStart ; nextEnd]
            end = nextEnd;
        }

        filtered[idx] = start;
        filtered[idx + 1] = end;

        int [][]output = new int[(idx + 2) / 2][];

        for (int n = 0; n < output.length; n++) {
            output[n] = new int[2];
            output[n][0] = filtered[n * 2];
            output[n][1] = filtered[n * 2 + 1];
        }

        return output;
    }

}

package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] buckets = new int[M];
        for (int n : buckets) {
            n = 0;
        }

        for (Oomage o : oomages) {
            int pos = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[pos] += 1;
        }

        for (int n : buckets) {
            if (n <= oomages.size() / 50 || n >= oomages.size() / 2.5) {
                return false;
            }
        }

        return true;
    }
}

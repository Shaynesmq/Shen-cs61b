/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        int R = 256;

        int longest = 0;
        for (String s : asciis) {
            longest = longest > s.length() ? longest : s.length();
        }

        String[] aux = new String[asciis.length];
        String[] auxx = new String[asciis.length];
        System.arraycopy(asciis, 0, auxx, 0, asciis.length);

        for (int d = longest - 1; d >= 0; d--) {
            int[] counts = new int[R];
            for (int i = 0; i < auxx.length; i++) {
                if (d >= auxx[i].length()) {
                    counts[0]++;
                } else {
                    counts[(int) auxx[i].charAt(d)]++;
                }
            }

            int pos = 0;
            int[] starts = new int[R];
            for (int i = 0; i < starts.length; i++) {
                starts[i] = pos;
                pos += counts[i];
            }

            for (int i = 0; i < auxx.length; i++) {
                int diff = longest - auxx[i].length();
                int p;
                if (d >= auxx[i].length()) {
                    p = starts[0]++;
                } else {
                    char c = auxx[i].charAt(d);
                    p = starts[(int) c]++;
                }
                aux[p] = auxx[i];
            }
            System.arraycopy(aux, 0, auxx, 0, aux.length);
        }

        return aux;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        return;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

}

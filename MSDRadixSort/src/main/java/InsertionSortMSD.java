import javafx.util.Pair;

public class InsertionSortMSD {

    public static void sort(Pair[] a, int lo, int hi, int d) {
        for (int i = lo; i < hi; i++)
            for (int j = i; j > lo && less(a[j].getValue().toString(), a[j - 1].getValue().toString(), d); j--)
                swap(a, j, j - 1);
    }

    private static boolean less(String v, String w, int d) {
        return v.substring(d).compareTo(w.substring(d)) < 0;
    }

    private static void swap(Pair[] a, int j, int i) {
        Pair temp = a[j];
        a[j] = a[i];
        a[i] = temp;
    }
}

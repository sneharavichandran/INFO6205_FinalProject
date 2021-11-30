import java.text.Collator;
import java.util.*;


import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

// Import Pinyin
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class MSDRadixSort {
    /**
     * Sort an array of Strings using MSDStringSort.
     *
     * @param a the array to be sorted.
     */
    public static void main(String args[])
    {
        String[] a={"midnight", "badge", "bag",
                "worker", "banner", "wander" };
        String[] c= {
                "刘持平",
                "洪文胜",
                "樊辉辉",
                "苏会敏",
                "高民政"};
//        sortchinese();


        // pinyin implementation
        System.out.println("Convert Chinese to Eng Algorithm");
        String[] convertedNames;
        convertedNames = convertToPinyin(c);
        for (int i=0;i<convertedNames.length;i++) {
            System.out.println(convertedNames[i]);
        }
        Map<String, String> chinese = new HashMap<>();
        for(int i=0;i<c.length;i++){
            chinese.put(convertedNames[i],c[i]);
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println("Our Sorting Algorithm english");
        sort(convertedNames);
        for (int i=0;i<convertedNames.length;i++) {
            System.out.println(convertedNames[i]);
        }
        System.out.println("--------------------------------------------------------------");
        Map<String,String> sortedChinese = new LinkedHashMap<>();
        for(int i=0;i<c.length;i++){
            sortedChinese.put(chinese.get(convertedNames[i]),convertedNames[i]);
        }

        System.out.println("Sorted Chinese is ");
        List<String> arrayKey = new ArrayList<>(sortedChinese.keySet());
        for(int i=0;i<arrayKey.size();i++){
            System.out.println(arrayKey.get(i));
        }

        // testing
        System.out.println("Testing---------");
        String cnStr = "刘持平";
        System.out.println(toPinYin(cnStr));


    }
    public static String[] convertToPinyin(String[] c){
        String[] convertedNames = new String[c.length];
        for (int i=0; i<c.length;i++){
            convertedNames[i] = toPinYin(c[i]);
        }
        return convertedNames;
    }

    public static void sortchinese()
    {
        List<String> list = Arrays.asList(
                "刘持平",
                "洪文胜",
                "樊辉辉",
                "苏会敏",
                "高民政");
        Collator spCollator = Collator.getInstance(Locale.CHINESE);
        list.sort(spCollator);
        for(int i=0;i<list.size();i++)
            System.out.println(list.get(i));

    }
    public static void sort(String[] a) {
        int n = a.length;
        aux = new String[n];
        sort(a, 0, n, 0);
    }

    /**
     * Sort from a[lo] to a[hi] (exclusive), ignoring the first d characters of each String.
     * This method is recursive.
     *
     * @param a the array to be sorted.
     * @param lo the low index.
     * @param hi the high index (one above the highest actually processed).
     * @param d the number of characters in each String to be skipped.
     */
    private static void sort(String[] a, int lo, int hi, int d) {
        if (hi < lo + cutoff) InsertionSortMSD.sort(a, lo, hi, d);
        else {
            int[] count = new int[radix + 2];        // Compute frequency counts.
            for (int i = lo; i < hi; i++)
                count[charAt(a[i], d) + 2]++;
            for (int r = 0; r < radix + 1; r++)      // Transform counts to indices.
                count[r + 1] += count[r];
            for (int i = lo; i < hi; i++)     // Distribute.
                aux[count[charAt(a[i], d) + 1]++] = a[i];
            // Copy back.
            if (hi - lo >= 0) System.arraycopy(aux, 0, a, lo, hi - lo);
            // Recursively sort for each character value.
            for (int r = 0; r < 256; r++)
                sort(a, lo + count[r],
                        lo + count[r + 1] - 1, d + 1);
            // TO BE IMPLEMENTED
        }
    }

    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    public static String toPinYin(String string){
        char[] c = string.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i< c.length; i++){
            stringBuffer.append(toChar(c[i]));
        }
        return stringBuffer.toString();
    }

    private static String toChar(char c){
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        try{
            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if(pinYin != null){
                return pinYin[0];
            }
        }
        catch(BadHanyuPinyinOutputFormatCombination ex){
            ex.printStackTrace();
        }
        return String.valueOf(c);
    }

    private static final int radix = 256;
    private static final int cutoff = 15;
    private static String[] aux;       // auxiliary array for distribution
}


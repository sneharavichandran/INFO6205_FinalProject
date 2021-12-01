import java.io.*;
import java.text.CollationKey;
import java.text.Collator;
import java.util.*;


import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

// Import Pinyin
import javafx.util.Pair;
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
    public static void main(String args[]) throws IOException {
        // Read shuffled chinese
        String[] shuffledChinese =  readShuffledChinese(PATH+SourceFileName+".txt");

        // Convert Chinese to Pinyin
        Pair[] pinyinConvertedChinese = convertToPinyin(shuffledChinese);

        for (int i=0;i<pinyinConvertedChinese.length;i++)
            System.out.println(pinyinConvertedChinese[i]);

        // Sort Pinyin using MSD Radix Sort, In Place sorting
        msdRadixSort(pinyinConvertedChinese);

        for (int i=0;i<pinyinConvertedChinese.length;i++)
            System.out.println(pinyinConvertedChinese[i]);

        // Write sorted output to txt
        writeSortedChinese(pinyinConvertedChinese);
    }

    private static void writeSortedChinese(Pair[] pinyinConvertedChinese) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(PATH+DestinationFileName+".txt");

        for(int i=0;i<pinyinConvertedChinese.length;i++){
            out.println(pinyinConvertedChinese[i].getValue());
        }
        out.close();
    }

    private static void createFile(String fileName, String path) {
        try {
            File myObj = new File(path +fileName+".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
            else {
                System.out.println("File already exists.");
            }
        }
        catch (IOException e) {System.out.println("An error occurred.");
            e.printStackTrace();}
    }

    private static String[] readShuffledChinese(String path) throws IOException {
        BufferedReader in = null;
        String[] shuffledChinese  = new String[5];
        try {
            in = new BufferedReader(new FileReader(path));
            String name;
            int i=0;
            while ((name = in.readLine()) != null && i!=5) {
                shuffledChinese[i] = name;
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return shuffledChinese;
    }

    public static Pair[] convertToPinyin(String[] shuffledChinese){
        Pair[] pinyinConvertedChinese = new Pair[shuffledChinese.length];
        for (int i=0; i<shuffledChinese.length;i++){
            Pair pair = new Pair(toPinYin(shuffledChinese[i]), shuffledChinese[i]);
            pinyinConvertedChinese[i] = pair;
        }
        return pinyinConvertedChinese;
    }

    public static void sortchinese()
    {
        List<String> list = Arrays.asList(
                "刘思文", "刘斯文", "刘思源", "刘四文");
        Collator spCollator = Collator.getInstance(Locale.CHINESE);
        list.sort(spCollator);
        for(int i=0;i<list.size();i++)
            System.out.println(list.get(i));
    }
    public static void msdRadixSort(Pair[] pinyinConvertedChinese) {
        int n = pinyinConvertedChinese.length;
        aux = new Pair[n];
        sort(pinyinConvertedChinese, 0, n, 0);
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
    private static void sort(Pair[] a, int lo, int hi, int d) {
        if (hi < 1) InsertionSortMSD.sort(a, lo, hi, d);
        else {
            int[] count = new int[radix + 2];        // Compute frequency counts. 258
            for (int i = lo; i < hi; i++) {
                count[charAt(a[i].getKey().toString(), d) + 2]++;
            }
            for (int r = 0; r < radix + 1; r++)      // Transform counts to indices.
                count[r + 1] += count[r];
            for (int i = lo; i < hi; i++)     // Distribute.
                aux[count[charAt(a[i].getKey().toString(), d) + 1]++] = (Pair) a[i];

            for (int i=0;i<aux.length;i++)
                System.out.println(": "+aux[i]);


            if (hi - lo >= 0) System.arraycopy(aux, 0, a, lo, hi - lo);
            // Recursively sort for each character value.
            for (int r = 0; r < 256; r++)
                sort(a, lo + count[r],
                        lo + count[r + 1] - 1, d + 1);
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
    private static Pair[] aux;       // auxiliary array for distribution
    private  static String PATH = "src/main/resource/";
    private  static String DestinationFileName = "sortedChinese";
    private  static String SourceFileName = "shuffledChinese";

}


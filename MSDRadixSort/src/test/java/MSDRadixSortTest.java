import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;
import java.text.Collator;

public class MSDRadixSortTest {
    @Test
    public void TestSorting1() throws Exception{
        List<String> list = Arrays.asList(
                "刘持平",
                "洪文胜",
                "樊辉辉",
                "苏会敏",
                "高民政");

        List<String> sortedChinese = Arrays.asList();
        sortedChinese = sortchinese(list);


    }

    public static List<String> sortchinese(List<String> list)
    {
        Collator spCollator = Collator.getInstance(Locale.CHINESE);
        list.sort(spCollator);
        return list;
    }

}

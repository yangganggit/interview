import java.util.regex.Matcher;
import java.util.regex.Pattern;

/****************************************************************
 * @描述      : 
 * @作者      : 杨刚
 * @版本      : 
 * @时间      : 2016/9/17 23:23
 ****************************************************************/
public class PatternTest {
    public static void main(String[] args) {
        String testText = "0A.a450.12423400234234234";
        Pattern pattern = Pattern.compile("^[0]+[1-9]+");
        Matcher matcher = pattern.matcher(testText);
        if (matcher.find()){
            testText = testText.replaceAll("^[0]+", "");
        }else {
        }
        System.out.println(testText);

        //testText.replaceAll("[0]*[0-9]+]", "oo");

        //System.out.println(testText);

//        Pattern pattern = Pattern.compile("([1-9]{1}[0-9]+(\\.[0-9]+){0,1})|([0-9]{1}(\\.[0-9]+){0,1})");
//        Matcher matcher = pattern.matcher(testText);
//        if (matcher.matches()){
//            System.out.println("正确");
//        }else {
//            System.out.println("格式不正确");
//        }
    }
}

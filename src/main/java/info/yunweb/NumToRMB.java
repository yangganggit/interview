package info.yunweb;

import javax.swing.text.Element;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/****************************************************************
 * @描述      : 人民币 数字转大写中文格式
 * 人民币大写金额写法及注意事项
 * 银行、单位和个人填写的各种票据和结算凭证是办理支付结算和现金收付的重要依据，直接关系到支付结算的准确、及时和安全。
 * 票据和结算凭证是银行、单位和个人凭以记载账务的会计凭证，是记载经济业务和明确经济责任的一种书面证明。
 * 因此，填写票据和结算凭证必须做到标准化、规范化、要素齐全、数字正确、字迹清晰、不错漏、不潦草、防止涂改。
 * 中文大写金额数字应用正楷或行书填写，如壹、贰、叁、肆、伍、陆、柒、捌、玖、拾、佰、仟、万、亿、元、角、分、零、整（正）等字样，不得用一、二（两）、三、四、五、六、七、八、九、十、毛、另（或0）填写，不得自造简化字。如果金额数字书写中使用繁体字，如贰、陆、亿、万、圆的，也应受理。
 * 人民币大写的正确写法还应注意以下几项：
 * 一、中文大写金额数字到“元”为止的，在“元”之后、应写“整”（或“正”）字；在“角”之后，可以不写“整”（或“正”）字；大写金额数字有“分”的，“分”后面不写“整”（或“正”）字。
 * 二、中文大写金额数字前应标明“人民币”字样，大写金额数字应紧接“人民币”字样填写，不得留有空白。
 * 大写金额数字前未印“人民币”字样的，应加填“人民币”三字，在票据和结算凭证大写金额栏内不得预印固定的“仟、佰、拾、万、仟、佰、拾、元、角、分”字样。
 * 三、阿拉伯数字小写金额数字中有“0”时，中文大写应按照汉语语言规律、金额数字构成和防止涂改的要求进行书写。
 * 举例如下：
 * 1、阿拉伯数字中间有“0”时，中文大写要写“零”字，如￥1409.50应写成人民币壹仟肆佰零玖元伍角；
 * 2、阿拉伯数字中间连续有几个“0”时、中文大写金额中间可以只写一个“零”字，如￥6007.14应写成人民币陆仟零柒元壹角肆分。
 * 3、阿拉伯金额数字万位和元位是“0”，或者数字中间连续有几个“0”，万位、元位也是“0”但千位、角位不是“0”时，中文大写金额中可以只写一个零字，也 可以不写“零”字.
 * 如￥1680.32应写成人民币壹仟陆佰捌拾元零叁角贰分，或者写成人民币壹仟陆佰捌拾元叁角贰分。
 * 又如￥107000.53应写成人民币壹拾万柒仟 元零伍角叁分，或者写成人民币壹拾万零柒仟元伍角叁分。
 * 4、阿拉伯金额数字角位是“0”而分位不是“0”时，中文大写金额“元”后面应写“零”字，如￥16409.02应写成人民币壹万陆仟肆佰零玖元零贰分，又如￥325.04应写成人民币叁佰贰拾伍元零肆分。
 * 四、阿拉伯小写金额数字前面均应填写人民币符号“￥”，阿拉伯小写金额数字要认真填写，不得连写分辨不清。
 * 五、票据的出票日期必须使用中文大写，为防止变造票据的出票日期，在填写月、日时、月为壹、贰和壹拾的，日为壹至玖和壹拾、贰拾和叁拾的，应在其前加“零”，日为拾壹至拾玖的应在其前加“壹”，如1月15日应写成零壹月壹拾伍日，再如10月20日应写成零壹拾月零贰拾日。
 * 六、票据出票日期使用小写填写的，银行不予受理；大写日期未按要求规范填写的，银行可予受理，但由此造成损失的由出票人自行承担。
 * @作者      : 杨刚
 * @版本      : 
 * @时间      : 2016/9/15 1:48
 ****************************************************************/
public class NumToRMB {
    /*数字 正序*/
    private static String[] RMB_ARR = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    /*整数四位数单位  倒序*/
    private static String[] RMB_INT_MIN_UNIT_ARR = {"", "拾", "佰", "仟"};
    /*小数单位 正序*/
    private static String[] RMB_DEC_MAX_UNIT_ARR = {"角", "分"};
    /*整数每四位的单位 倒序*/
    private static String[] RMB_MAX_UNIT_ARR = {"圆", "万", "亿", "万", "亿"};
    /*开头*/
    private static String START_RMB = "人民币";
    /*只有整数的结尾*/
    private static String END_RMB = "整";

    public static void main(String[] args) {
        System.out.println("------------" + getRMB("10305070902.15"));
        System.out.println("------------" + getRMB("10305070902112341234"));
        System.out.println("------------" + getRMB("02.124"));
        System.out.println("------------" + getRMB("0.125"));
        System.out.println("------------" + getRMB("0"));
        System.out.println("------------" + getRMB("000"));
        System.out.println("------------" + getRMB("0.0"));
        System.out.println("------------" + getRMB("0.00"));
        System.out.println("------------" + getRMB("0.00000"));
        System.out.println("------------" + getRMB("0000.00000"));
        System.out.println("------------" + getRMB("0.05"));
        System.out.println("------------" + getRMB(".69"));
        System.out.println("------------" + getRMB("A.123"));
    }

    private static String getRMB(String number) {
        try {
            System.out.println("输入字符串: " + number);
            number = format(number);
            System.out.println("格式字符串: " + number);
        } catch (Exception e) {
            /*格式有误*/
            System.out.println(e.getMessage());
            return "";
        }
        String[] integralDecimal = number.split("\\.");
        String integral = integralDecimal[0];
        /* 圆 万 亿 万*/
        String[] integrals = new String[(integral.length()+3)/4];
        int start, end;
        for (int i = 0, j = 0; i < integrals.length; i++) {
            start = (integral.length()-4*i-4) < 0 ? 0 : (integral.length()-4*i-4);
            end = integral.length() - 4*i;
            integrals[j++] = integral.substring(start, end);
        }
        /*整数部分*/
        String integralRMB = "";
        for (int i = 0; i < integrals.length; i++) {
            integralRMB = getFourRMB(integrals[i]) + RMB_MAX_UNIT_ARR[i] + integralRMB;
        }
        /*小数部分 - 没有小数*/
        if ( integralDecimal.length <= 1){
            return START_RMB + integralRMB + END_RMB;
        }
        /*小数部分 - 有小数 - 四舍五入*/
        String decimal = integralDecimal[1];
        BigDecimal bigDecimal = new BigDecimal("0."+decimal);
        decimal = String.valueOf(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).split("\\.")[1];
        /*小数部分 - 有小数 - 获取小数的大写中文格式*/
        String decimalRMB = getDecimalRMB(decimal);
        if(!"".equals(decimalRMB) && (RMB_ARR[0]+RMB_MAX_UNIT_ARR[0]).equals(integralRMB)){
            integralRMB = "";
        }
        return START_RMB + integralRMB + decimalRMB;
    }

    /**
     * @描述 : 格式化字符串
     * @作者 : 杨刚
     * @时间 : 2016/9/18 0:09
     * @参数 : null
     * @版本 : V1.0.0
     */
    private static String format(String number) throws Exception{
        /*
            输入字符串: 10305070902.15
            格式字符串: 10305070902.15
            ------------人民币壹佰零叁亿零伍佰零柒万零玖佰零贰圆壹角伍分
            输入字符串: 10305070902112341234
            格式字符串: 10305070902112341234
            ------------人民币壹仟零叁拾亿伍仟零柒拾万玖仟零贰拾壹亿壹仟贰佰叁拾肆万壹仟贰佰叁拾肆圆整
            输入字符串: 02.124
            格式字符串: 2.124
            ------------人民币贰圆壹角贰分
            输入字符串: 0.125
            格式字符串: 0.125
            ------------人民币壹角叁分
            输入字符串: 0
            格式字符串: 0
            ------------人民币零圆整
            输入字符串: 000
            格式字符串: 0
            ------------人民币零圆整
            输入字符串: 0.0
            格式字符串: 0.0
            ------------人民币零圆
            输入字符串: 0.00
            格式字符串: 0.0
            ------------人民币零圆
            输入字符串: 0.00000
            格式字符串: 0.0
            ------------人民币零圆
            输入字符串: 0000.00000
            格式字符串: 0.0
            ------------人民币零圆
            输入字符串: 0.05
            格式字符串: 0.05
            ------------人民币伍分
            输入字符串: .69
            格式字符串: 0.69
            ------------人民币陆角玖分
            输入字符串: A.123
            您输入的数字格式不正确
            ------------
        */
        number = "0" + number;
        Pattern pattern = Pattern.compile("([0-9]+(\\.[0-9]+){0,1})");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()){
            throw new RuntimeException("您输入的数字格式不正确");
        }
        pattern = Pattern.compile("^[0]+");
        matcher = pattern.matcher(number);
        if (matcher.find()){
            number = number.replaceAll("^[0]+", "");
            number = number.replaceAll("[0]+$", "");
        }
        if ("".equals(number)){
            number = "0";
        }
        if (number.startsWith(".")){
            number = "0" + number;
        }
        if (number.endsWith(".")){
            number = number + "0";
        }
        return number;
    }

    /**
     * @描述 : 获取小数的大写中文格式
     * @作者 : 杨刚
     * @时间 : 2016/9/15 3:24
     * @参数 : null
     * @版本 : V1.0.0
     */
    private static String getDecimalRMB(String decimal) {
        StringBuilder decimalRMB = new StringBuilder();
        for (int i = 0; i < decimal.length(); i++) {
            Integer integer = Integer.valueOf(String.valueOf(decimal.charAt(i)));
            if ( integer > 0 ){
                decimalRMB.append(RMB_ARR[integer] + RMB_DEC_MAX_UNIT_ARR[i]);
            }
        }
        return decimalRMB.toString();
    }

    /**
     * @描述 : 获取四位人民币的中文大写格式
     * @作者 : 杨刚
     * @时间 : 2016/9/15 3:24
     * @参数 : null
     * @版本 : V1.0.0
     */
    private static String getFourRMB(String fourNum) {
        StringBuilder fourRMB = new StringBuilder();
        boolean zeroFlag = false;
        if( "0".equals(fourNum)){
            return RMB_ARR[0];
        }
        for (int i = fourNum.length() - 1; i >= 0 ; i--) {
            Integer integer = Integer.valueOf(String.valueOf(fourNum.charAt(i)));
            if ( integer > 0 ){
                fourRMB.insert(0, RMB_ARR[integer] + RMB_INT_MIN_UNIT_ARR[fourNum.length() - i - 1]);
                zeroFlag = true;
            } else if (zeroFlag){
                fourRMB.insert(0, RMB_ARR[0]);
                zeroFlag = false;
            }
        }
        return fourRMB.toString();
    }
}

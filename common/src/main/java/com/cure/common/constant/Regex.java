package com.cure.common.constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title: Regex
 * @description: 常用正则表达式
 * @author: dengmiao
 * @create: 2019-05-23 17:56
 **/
public interface Regex {

    /** 整数 */
    String INTEGER = "^-?[1-9]\\d*$";

    /** 正整数 */
    String Z_INDEX = "^[1-9]\\d*$";

    /** 负整数 */
    String NEGATIVE_INTEGER = "^-[1-9]\\d*$";

    /** 数字 */
    String NUMBER = "^([+-]?)\\d*\\.?\\d+$";

    /** 正数 */
    String POSITIVE_NUMBER = "^[1-9]\\d*|0$";

    /** 负数 */
    String NEGATIVE_NUMBER = "^-[1-9]\\d*|0$";

    /** 浮点数 */
    String FLOAT = "^([+-]?)\\d*\\.\\d+$";

    /** 正浮点数 */
    String POSITIVE_FLOAT = "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$";

    /** 负浮点数 */
    String NEGATIVE_FLOAT = "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$";

    /** 邮件 */
    String EMAIL = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

    /** 颜色 */
    String COLOR = "^[a-fA-F0-9]{6}$";

    /** url */
    String URL = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";

    /** 仅中文 */
    String CHINESE = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";

    /** 仅ACSII字符 */
    String ASCII = "^[\\x00-\\xFF]+$";

    /** 邮编 */
    String ZIP_CODE = "^\\d{6}$";

    /** 手机 */
    String MOBILE = "^1[2345678][0-9]{9}$";

    /** ip地址 */
    String IP4 = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";

    /** 非空 */
    String NOT_EMPTY = "^\\S+$";

    /** 图片 */
    String PICTURE = "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$";

    /** 压缩文件 */
    String RAR = "(.*)\\.(rar|zip|7zip|tgz)$";

    /** 日期 */
    String DATE =
            "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";

    /** QQ号码 */
    String QQ_NUMBER = "^[1-9]*[1-9][0-9]*$";

    /** 电话号码的函数(包括验证国内区号,国际区号,分机号) */
    String TEL = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$";

    /** 字母 */
    String LETTER = "^[A-Za-z]+$";

    /** 大写字母 */
    String LETTER_U = "^[A-Z]+$";

    /** 小写字母 */
    String LETTER_I = "^[a-z]+$";

    /** 身份证 */
    String IDCARD = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

    /** 编码 */
    String CODE = "^[A-Za-z0-9][-A-Za-z0-9_]*$";

    /**
     * 匹配正则
     * @param str 源字符串
     * @param regex 正则表达式
     * @return
     */
    static boolean is(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 数字
     * @param str
     * @return
     */
    static boolean isNumber(String str) {
        return is(str, Regex.NUMBER);
    }

    /**
     * 邮箱
     * @param str
     * @return
     */
    static boolean isEmail(String str) {
        return is(str, Regex.EMAIL);
    }

    /**
     * ip地址
     * @param str
     * @return
     */
    static boolean isIP(String str) {
        return is(str, Regex.IP4);
    }

    /**
     * 电话号码
     * @param str
     * @return
     */
    static boolean isMobile(String str) {
        return is(str, Regex.MOBILE);
    }

    /**
     * url
     * @param str
     * @return
     */
    static boolean isUrl(String str) {
        return is(str, Regex.URL);
    }
}

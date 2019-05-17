package com.cure.common.toolkit;

import cn.hutool.core.util.StrUtil;

import java.util.List;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;

/**
 * @description: 字符串处理类
 * @author: dengmiao
 * @create: 2019-04-11 14:19
 **/
public class StringUtil {

    public static final String SPLIT_DEFAULT = ",";
    public static final String SPLIT_FILE_DEFAULT = "|";
    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";


    public static String[] splitDefault(final String str) {
        return StrUtil.split(str, SPLIT_DEFAULT);
    }

    /**
     * 拼接字符串
     *
     * @param strs
     * @return String
     */
    public static String toAppendStr(Object... strs) {
        StringBuilder sb = new StringBuilder();
        for (Object str : strs) {
            if (isNotEmpty(str)) {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 字符串列表转成字符串
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append("'" + list.get(i) + "',");
                } else {
                    sb.append("'" + list.get(i) + "'");
                }
            }
        }
        return sb.toString();
    }

    /**
     *
     * 获得字符串长度
     * @param s
     * @return
     */
    public static int getStringLength(String s) {
        int length = 0;
        if ((null != s) && (!"".equals(s))) {
            for (int i = 0; i < s.length(); i++) {
                int ascii = Character.codePointAt(s, i);
                if (ascii >= 0 && ascii <= 255) {
                    length++;
                } else {
                    length += 2;
                }
            }
        }
        return length;
    }
}

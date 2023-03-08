package com.coder.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author coder
 * @Date 2023/3/7 22:06
 * @Description
 */
public class StringUtil {

    // todo 学习并训练正则匹配的使用方法 主要了解Matcher类的使用
    public static String camelCaseToUnderlineCase(String s){
        Pattern p = Pattern.compile("[A-Z]");
        Matcher matcher = p.matcher(s);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}

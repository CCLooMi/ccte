package com.ccte.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**@类名 CCTEConstant
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月6日-下午2:23:08
 */
public interface CCTEConstant {
	public static final String _if_="if";
	public static final String _else_if_="else_if";
	public static final String _else_="else";
	public static final String _for_="for";
	public static final String _repeat_="repeat";
	/**代码隔离用，用来将纯html代码和java代码分离,java代码前面和后面一定有一个seprator*/
	public static final char seprator=0;
	/**java代码标识,避免和html中的javascript代码混淆*/
	public static final char codeflag=1;
	/**匹配值输出*/
	public static final Pattern elpattern=Pattern.compile("\\$\\{.+?\\}");
	/**匹配if判断中=号两边的代码(包括=号)*/
	public static final Pattern eqpattern=Pattern.compile(".+[<>=!][<>=]?.+");
	/**匹配cc_判断多功能赋值代码<br>{([^"{},]+:[^"{},]+,?)+}\([^"]+\)*/
	public static final Pattern ccpattern=Pattern.compile("\\{([^\"{},]+:[^\"{},]+,?)+\\}\\([^\"]+\\)");
	
	public static final Pattern ccpattern_left=Pattern.compile("\\{[^\"{}]+\\}");
	/**匹配k:v*/
	public static final Pattern kvpattern=Pattern.compile("([^\"{},]+:[^\"{},]+)");
	/**匹配比较符号*/
	public static final Pattern cppattern=Pattern.compile("[<>=!][<>=]?");
	public static final Pattern cpmatcher=Pattern.compile("[^|&<>=()]+[<>=!][<>=]?[^|&<>=()]+");
	/**匹配数字*/
	public static final Pattern numeric=Pattern.compile("\\d+\\.?\\d+|-\\d+\\.?\\d+");
	public static final Set<String>voidTags=new HashSet<>(Arrays
			.asList("br","hr","img","input","link","meta",
			"area","base","col","command","embed","keygen","param","source","track","wbr"));
}

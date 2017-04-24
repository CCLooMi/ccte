package ccte.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ccte.core.CCTEConstant;
/**
 * 
 * @author Chenxj
 * 2017/04/15 23:01
 */
public class StringUtil implements CCTEConstant{
	public static List<String>split(String s,String regex){
		List<String>result=new ArrayList<>();
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(s);
		StringBuffer sb=new StringBuffer();
		while(matcher.find()){
			matcher.appendReplacement(sb, "");
			if(sb.length()>0){
				result.add(sb.toString());
			}
			result.add(matcher.group());
		}
		sb.delete(0, sb.length());
		matcher.appendTail(sb);
		if(sb.length()>0){
			result.add(sb.toString());
		}
		return result;
	}
	public static List<String>split(String s,Pattern pattern){
		List<String>result=new ArrayList<>();
		Matcher matcher=pattern.matcher(s);
		StringBuffer sb=new StringBuffer();
		while(matcher.find()){
			matcher.appendReplacement(sb, "");
			if(sb.length()>0){
				result.add(sb.toString());
			}
			result.add(matcher.group());
		}
		sb.delete(0, sb.length());
		matcher.appendTail(sb);
		if(sb.length()>0){
			result.add(sb.toString());
		}
		return result;
	}
	public static Map<String, String> jsonString2map(String json){
		Map<String, String>m=new HashMap<>();
		Matcher matcher=kvpattern.matcher(json);
		while(matcher.find()){
			String[]s=matcher.group().split(":");
			m.put(s[0], s[1]);
		}
		return m;
	}
}

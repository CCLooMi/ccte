package com.ccte.core;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**@类名 CCTETemplate
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月1日-上午10:44:28
 */
public abstract class CCTETemplate {
	public static final Map<Integer, byte[]>resources=new HashMap<>();
	private Charset charset;
	public abstract void render(Map<String, Object>model,OutputStream out) throws Exception;
	protected static byte[] getResource(int k){
		return resources.get(k);
	}
	protected void outprint(OutputStream out,Object value) throws Exception{
		if(value instanceof String){
			out.write(((String)value).getBytes(charset));
		}else if(value instanceof byte[]){
			out.write((byte[])value);
		}else {
			out.write(String.valueOf(value).getBytes(charset));
		}
	}
	protected ListType listType(Object list){
		if(list instanceof List){
			return ListType.list;
		}else if(list.getClass().isArray()){
			return ListType.array;
		}else if(list instanceof Set){
			return ListType.set;
		}else if(list instanceof Map){
			return ListType.map;
		}else{
			return ListType.object;
		}
	}
	
	protected static Object[] list2array(Object list){
		if(list.getClass().isArray()){
			return (Object[])list;
		}else if(list instanceof Map){
			return ((Map<?,?>)list).entrySet().toArray();
		}else{
			return new Object[]{list};
		}
	}
	/**设置 charset*/
	public CCTETemplate setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}
}

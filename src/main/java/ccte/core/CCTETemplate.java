package ccte.core;

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
//	private Charset charset;
	public abstract void render(Map<String, Object>model,OutputStream out) throws Exception;
	protected static byte[] getResource(int k){
		return resources.get(k);
	}
	protected void outprint(OutputStream out,Object value) throws Exception{
		String s=null;
		if(value instanceof String){
			s=(String)value;
		}else {
			s=String.valueOf(value);
		}
//		out.write(s.getBytes(charset));
		int len = s.length();
        for (int c,i = 0; i < len; i++) {
            c = s.charAt (i);
            if((c&0xFF00)==0){
            	out.write(c);
            }else{
            	out.write(unicode2utf8(c));
            }
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
	protected byte[] unicode2utf8(long unic){
		byte[]b = null;
		if (unic <= 0x0000007F) {
			b=new byte[1];
			// * U-00000000 - U-0000007F: 0xxxxxxx
			b[0] = (byte) (unic & 0x7F);
		} else if (unic >= 0x00000080 && unic <= 0x000007FF) {
			b=new byte[2];
			// * U-00000080 - U-000007FF: 110xxxxx 10xxxxxx
			b[1] = (byte) ((unic & 0x3F) | 0x80);
			b[0] = (byte) (((unic >> 6) & 0x1F) | 0xC0);
		} else if (unic >= 0x00000800 && unic <= 0x0000FFFF) {
			b=new byte[3];
			// * U-00000800 - U-0000FFFF: 1110xxxx 10xxxxxx 10xxxxxx
			b[2] = (byte) ((unic & 0x3F) | 0x80);
			b[1] = (byte) (((unic >> 6) & 0x3F) | 0x80);
			b[0] = (byte) (((unic >> 12) & 0x0F) | 0xE0);
		} else if (unic >= 0x00010000 && unic <= 0x001FFFFF) {
			b=new byte[4];
			// * U-00010000 - U-001FFFFF: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
			b[3] = (byte) ((unic & 0x3F) | 0x80);
			b[2] = (byte) (((unic >> 6) & 0x3F) | 0x80);
			b[1] = (byte) (((unic >> 12) & 0x3F) | 0x80);
			b[0] = (byte) (((unic >> 18) & 0x07) | 0xF0);
		} else if (unic >= 0x00200000 && unic <= 0x03FFFFFF) {
			b=new byte[5];
			// * U-00200000 - U-03FFFFFF: 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
			b[4] = (byte) ((unic & 0x3F) | 0x80);
			b[3] = (byte) (((unic >> 6) & 0x3F) | 0x80);
			b[2] = (byte) (((unic >> 12) & 0x3F) | 0x80);
			b[1] = (byte) (((unic >> 18) & 0x3F) | 0x80);
			b[0] = (byte) (((unic >> 24) & 0x03) | 0xF8);
		} else if (unic >= 0x04000000 && unic <= 0x7FFFFFFF) {
			b=new byte[6];
			// * U-04000000 - U-7FFFFFFF: 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
			b[5] = (byte) ((unic & 0x3F) | 0x80);
			b[4] = (byte) (((unic >> 6) & 0x3F) | 0x80);
			b[3] = (byte) (((unic >> 12) & 0x3F) | 0x80);
			b[2] = (byte) (((unic >> 18) & 0x3F) | 0x80);
			b[1] = (byte) (((unic >> 24) & 0x3F) | 0x80);
			b[0] = (byte) (((unic >> 30) & 0x01) | 0xFC);
		}else {
			b=new byte[0];
		}
		return b;
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
//		this.charset = charset;
		return this;
	}
}

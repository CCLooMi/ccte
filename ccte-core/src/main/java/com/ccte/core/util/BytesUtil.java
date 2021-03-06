package com.ccte.core.util;

/**© 2015-2016 CCLooMi.Inc Copyright
 * 类    名：BytesUtil
 * 类 描 述：
 * 作    者：Chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2016年6月25日-上午11:39:12
 */
public class BytesUtil {
	private static final char[][] csm = new char[256][];
	private static final char[][] CSM = new char[256][];
	private static final char[]cc=new char[256];
	private static final int start=0x0100;
	static {
		String cs = "0123456789abcdef";
		String CS = "0123456789ABCDEF";
		int i,j,n = 0;
		for (i = 0; i < 16; i++) {
			for (j = 0; j < 16; j++,n++) {
				csm[n] = new char[] {cs.charAt(i),cs.charAt(j)};
				CSM[n] = new char[] {CS.charAt(i),CS.charAt(j)};
			}
		}
		for(i=start,j=0;i<start+256;i++) {
			cc[j++]|=i;
		}
	}

	public static String bytesToCCString(byte[]bytes) {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<bytes.length;i++) {
			sb.append(cc[bytes[i]&0xff]);
		}
		return sb.toString();
	}
	public static byte[] ccStringToBytes(String cc) {
		byte[]b=new byte[cc.length()];
		for(int i=0;i<b.length;i++) {
			b[i]|=(cc.charAt(i)-start);
		}
		return b;
	}
	public static String bytesToHexString(byte[] byts) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byts.length; i++) {
			sb.append(csm[byts[i]&0xff]);
		}
		return sb.toString();
	}
	public static String bytesToHEXString(byte[] byts) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byts.length; i++) {
			sb.append(CSM[byts[i]&0xff]);
		}
		return sb.toString();
	}
	public static byte[] hexStringToBytes(String hex){
		int l=(hex.length()+1)/2;
		byte[]bytes=new byte[l];
		for(int i=0,j=0;i<hex.length();i++) {
			switch (hex.charAt(i)) {
			case '1':bytes[j]|=0x10>>((i&1)<<2);break;
			case '2':bytes[j]|=0x20>>((i&1)<<2);break;
			case '3':bytes[j]|=0x30>>((i&1)<<2);break;
			case '4':bytes[j]|=0x40>>((i&1)<<2);break;
			case '5':bytes[j]|=0x50>>((i&1)<<2);break;
			case '6':bytes[j]|=0x60>>((i&1)<<2);break;
			case '7':bytes[j]|=0x70>>((i&1)<<2);break;
			case '8':bytes[j]|=0x80>>((i&1)<<2);break;
			case '9':bytes[j]|=0x90>>((i&1)<<2);break;
			case 'a':case 'A':bytes[j]|=0xA0>>((i&1)<<2);break;
			case 'b':case 'B':bytes[j]|=0xB0>>((i&1)<<2);break;
			case 'c':case 'C':bytes[j]|=0xC0>>((i&1)<<2);break;
			case 'd':case 'D':bytes[j]|=0xD0>>((i&1)<<2);break;
			case 'e':case 'E':bytes[j]|=0xE0>>((i&1)<<2);break;
			case 'f':case 'F':bytes[j]|=0xF0>>((i&1)<<2);break;
			default:break;
			}
			j+=i&1;
		}
		return bytes;
	}
	/**
	 * 描述：字节数组转整形
	 * 作者：Chenxj
	 * 日期：2016年6月6日 - 下午10:26:02
	 * @param bytes 字节数组
	 * @param endianness 字节序( 1:Big Endian,-1:Little Endian)
	 * @return
	 */
	public static int readBytesToInt(byte[]bytes,int endianness){
		int a=0;
		int length=bytes.length;
		if(endianness==1){
			for(int i=length-1;i>-1;i--){
				a|=(bytes[i]&0xFF)<<(i*8);
			}
		}else if(endianness==-1){
			for(int i=0;i<length;i++){
				a|=(bytes[i]&0xFF)<<((length-1-i)*8);
			}
		}
		return a;
	}
	/**
	 * 描述：整形转字节数组
	 * 作者：Chenxj
	 * 日期：2016年6月6日 - 下午10:25:22
	 * @param a 整形
	 * @param length 字节数组长度
	 * @param endianness 字节序( 1:Big Endian,-1:Little Endian)
	 * @return
	 */
	public static byte[] intToBytes(int a,int length,int endianness){
		byte[]b=new byte[length];
		if(endianness==1){
			for(int i=length-1;i>-1;i--){
				b[i]= (byte) (a>>(8*i)&0xFF);
			}
		}else if(endianness==-1){
			for(int i=0;i<length;i++){
				b[i]= (byte) (a>>(8*(length-1-i))&0xFF);
			}
		}
		return b;
	}
	/**
	 * 描述：字节数组转长整形
	 * 作者：Chenxj
	 * 日期：2016年6月6日 - 下午10:31:49
	 * @param bytes
	 * @param endianness 字节序( 1:Big Endian,-1:Little Endian)
	 * @return
	 */
	public static long readBytesToLong(byte[]bytes,int endianness){
		long a=0;
		int length=bytes.length;
		if(endianness==1){
			for(int i=length-1;i>-1;i--){
				a|=((long)bytes[i]&0xFF)<<(i*8);
			}
		}else if(endianness==-1){
			for(int i=0;i<length;i++){
				a|=((long)bytes[i]&0xFF)<<((length-1-i)*8);
			}
		}
		return a;
	}
	/**
	 * 描述：长整形转字节数组
	 * 作者：Chenxj
	 * 日期：2016年6月6日 - 下午10:32:18
	 * @param a 长整形
	 * @param length 字节数组长度
	 * @param endianness 字节序( 1:Big Endian,-1:Little Endian)
	 * @return
	 */
	public static byte[] longToBytes(long a,int length,int endianness){
		byte[]b=new byte[length];
		if(endianness==1){
			for(int i=length-1;i>-1;i--){
				b[i]= (byte) (a>>(8*i)&0xFF);
			}
		}else if(endianness==-1){
			for(int i=0;i<length;i++){
				b[i]= (byte) (a>>((length-1-i)*8)&0xFF);
			}
		}
		return b;
	}
	/**
	 * 描述：字节数组转双精度类型
	 * 作者：Chenxj
	 * 日期：2016年6月6日 - 下午10:40:16
	 * @param bytes 字节数组
	 * @param endianness 字节序( 1:Big Endian,-1:Little Endian)
	 * @return
	 */
	public static double readBytesToDouble(byte[]bytes,int endianness){
		return Double.longBitsToDouble(readBytesToLong(bytes,endianness));
	}
	/**
	 * 描述：双精度类型转字节数组
	 * 作者：Chenxj
	 * 日期：2016年6月6日 - 下午10:42:16
	 * @param a 双精度类型
	 * @param length 字节数组长度
	 * @param endianness 字节序( 1:Big Endian,-1:Little Endian)
	 * @return
	 */
	public static byte[] doubleToBytes(double a,int length,int endianness){
		return longToBytes(Double.doubleToLongBits(a), length,endianness);
	}
	/**
	 * 描述：字节数组转Float
	 * 作者：Chenxj
	 * 日期：2016年6月25日 - 下午12:30:53
	 * @param bytes
	 * @param endianness 字节序( 1:Big Endian,-1:Little Endian)
	 * @return
	 */
	public static float readBytesToFloat(byte[]bytes,int endianness){
		int a=0;
		int length=bytes.length;
		if(endianness==1){
			for(int i=length-1;i>-1;i--){
				a|=((long)bytes[i]&0xFF)<<(i*8);
			}
		}else if(endianness==-1){
			for(int i=0;i<length;i++){
				a|=((long)bytes[i]&0xFF)<<((length-1-i)*8);
			}
		}
		return Float.intBitsToFloat(a);
	}
	/**
	 * 描述：Float转字节数组
	 * 作者：Chenxj
	 * 日期：2016年6月25日 - 下午12:31:24
	 * @param a Float
	 * @param length 字节数组长度
	 * @param endianness 字节序( 1:Big Endian,-1:Little Endian)
	 * @return
	 */
	public static byte[] floatToBytes(float a,int length,int endianness){
		long b=Float.floatToIntBits(a);
		return longToBytes(b, length,endianness);
	}
}

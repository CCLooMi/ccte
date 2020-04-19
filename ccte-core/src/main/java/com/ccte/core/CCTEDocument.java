package com.ccte.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**@类名 CCTEDocument
 * @说明	html文档类,用于解决html的include问题
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月7日-下午5:23:30
 */
public class CCTEDocument {
	private static final Pattern includePattern=Pattern.compile("<include src=.+?/>|<include src=.+?>[^<>]*?</include>");
	private static final Pattern srcPattern=Pattern.compile("(?<=src\\=\").*(?=\")");
	private List<CCTEElement>elements;
	private boolean hasRef=false;
	/**是否是代码片段,根据文件名是否包含snippet进行判断*/
	private boolean isSnippet=false;
	/**
	 * @param in
	 * @param charset
	 * @param filePath 文件路径用来判断是否包含snippet
	 */
	public CCTEDocument(InputStream in,Charset charset,String filePath){
		isSnippet=filePath.contains("snippet");
		int bufferSize=60000;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        int read;
        int remaining = bufferSize;
        try{
        	while (!Thread.interrupted()) {
                read = in.read(buffer);
                if (read == -1) break;
                if (read > remaining) {
                    outStream.write(buffer, 0, remaining);
                    break;
                }
                remaining -= read;
                outStream.write(buffer, 0, read);
            }
        }catch (Exception e) {
        	e.printStackTrace();
		}
        byte[]bytes=outStream.toByteArray();
        String html=new String(bytes,charset);
        
        Matcher matcher=includePattern.matcher(html);
		
        elements=new ArrayList<>();
		
		StringBuffer sb=new StringBuffer();
		while(matcher.find()){
			sb.delete(0, sb.length());
			matcher.appendReplacement(sb, "");
			elements.add(new CCTEElement(sb.toString(), null));
			elements.add(new CCTEElement(null, elementSrc(matcher.group())));
			if(!hasRef){
				hasRef=true;
			}
		}
		if(hasRef){
			sb.delete(0, sb.length());
			matcher.appendTail(sb);
			elements.add(new CCTEElement(sb.toString(), null));
		}else{
			sb=null;
			elements.add(new CCTEElement(html, null));
		}
	}
	public CCTEDocument repareRef(Map<String, CCTEDocument>cctedocMap){
		if(hasRef){
			int l=elements.size();
			for(int i=0;i<l;i++){
				if(elements.get(i).getRefName()!=null){
					elements.get(i).setRefDoc(cctedocMap.get(elements.get(i).getRefName()));
				}
			}
		}
		return this;
	}
	private String elementSrc(String s){
		Matcher matcher=srcPattern.matcher(s);
		if(matcher.find()){
			return matcher.group();
		}
		return null;
	}
	
	@Override
	public String toString() {
		if(elements==null){
			return "";
		}else{
			StringBuilder sb=new StringBuilder();
			int l=elements.size();
			for(int i=0;i<l;i++){
				sb.append(elements.get(i).toString());
			}
			return sb.toString();
		}
	}
	/**获取 isSnippet*/
	public boolean isSnippet() {
		return isSnippet;
	}
}

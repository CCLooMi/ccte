package com.ccte.springboot;

import java.util.Properties;

import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**@类名 CCTEProperties
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年3月31日-下午4:43:35
 */
@ConfigurationProperties(prefix="spring.ccte")
public class CCTEProperties extends AbstractTemplateViewResolverProperties{
	public static final String DEFAULT_TEMPLATE_LOADER_PATH = "templates";

	public static final String DEFAULT_PREFIX = "";

	public static final String DEFAULT_SUFFIX = ".html";
	
	private String[]templateLoaderPath=new String[]{DEFAULT_TEMPLATE_LOADER_PATH};
	
	public CCTEProperties() {
		super(DEFAULT_PREFIX, DEFAULT_SUFFIX);
	}

	public String[] getTemplateLoaderPath() {
		return templateLoaderPath;
	}

	public void setTemplateLoaderPath(String[] templateLoaderPath) {
		this.templateLoaderPath = templateLoaderPath;
	}
	public Properties toProperties(){
		Properties p=new Properties();
		p.put("templateLoadPath", DEFAULT_TEMPLATE_LOADER_PATH);
		p.put("charset", getCharsetName());
		p.put("prefix", DEFAULT_PREFIX);
		p.put("suffix", DEFAULT_SUFFIX);
		return p;
	}
}

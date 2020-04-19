package com.ccte.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import com.ccte.core.CCTETemplateFactory;
import com.ccte.springmvc.CCTEViewResolver;

/**@类名 CCTEAutoConfiguration
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年3月31日-下午4:42:44
 */
@Configurable
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(CCTEProperties.class)
public class CCTEAutoConfiguration {
	@Autowired
	private CCTEProperties properties;
	
	@Bean
	@ConditionalOnMissingBean(CCTETemplateFactory.class)
	public CCTETemplateFactory ccteTemplateFactory(){
		CCTETemplateFactory factory=new CCTETemplateFactory()
				.applyProperties(this.properties.toProperties())
				.compileTemplates();
		return factory;
	}
	@Bean
	@ConditionalOnProperty(name = "spring.ccte.enabled", matchIfMissing = true)
	public CCTEViewResolver ccteViewResolver(){
		CCTEViewResolver resolver=new CCTEViewResolver();
		this.properties.applyToMvcViewResolver(resolver);
		return resolver;
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledResourceChain
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		return new ResourceUrlEncodingFilter();
	}
}

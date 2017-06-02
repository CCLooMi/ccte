package ccte.spring.springmvc;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.AbstractTemplateView;

import ccte.core.CCTETemplateFactory;

/**@类名 CCTEView
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年3月31日-下午4:33:38
 */
public class CCTEView extends AbstractTemplateView{
	private static CCTETemplateFactory templateFactory;
	protected CCTETemplateFactory autodetectConfiguration() throws BeansException {
		try {
			return BeanFactoryUtils.beanOfTypeIncludingAncestors(
					getApplicationContext(), CCTETemplateFactory.class, true, false);
		}
		catch (NoSuchBeanDefinitionException ex) {
			throw new ApplicationContextException(
					"Must define a single CCTETemplateFactory bean in this web application context " +
					"(may be inherited): CCTETemplateFactory is the usual implementation. " +
					"This bean may be given any name.", ex);
		}
	}
	@Override
	protected void initServletContext(ServletContext servletContext) throws BeansException {
		if(templateFactory==null){
			templateFactory=autodetectConfiguration();
		}
	}
	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return templateFactory.checkTemplate(getUrl());
	}
	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding(templateFactory.getCharset().name());
		templateFactory
		.findTemplate(getUrl())
		.render(model,response.getOutputStream());
	}

}

package ccte.spring.springmvc;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

/**@类名 CCTEViewResolver
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年3月31日-下午4:29:37
 */
public class CCTEViewResolver extends AbstractTemplateViewResolver{
	
	public CCTEViewResolver(){
		setViewClass(requiredViewClass());
	}
	
	public CCTEViewResolver(String prefix,String suffix){
		this();
		setPrefix(prefix);
		setSuffix(suffix);
	}
	
	@Override
	protected Class<?> requiredViewClass() {
		return CCTEView.class;
	}
}

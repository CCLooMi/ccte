package ccte.spring.springmvc;

import java.util.Properties;

import ccte.core.CCTETemplateFactory;

/**© 2015-2017 Chenxj Copyright
 * 类    名：CCTETemplateFactoryCreator
 * 类 描 述：
 * 作    者：chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2017年5月31日-上午10:54:59
 */
public class CCTETemplateFactoryCreator {
	private static CCTETemplateFactoryCreator fc;
	private static CCTETemplateFactory tf;
	private Properties initProperties;
	public static CCTETemplateFactoryCreator getInstance(){
		if(fc==null){
			fc=new CCTETemplateFactoryCreator();
		}
		return fc;
	}
	public CCTETemplateFactory createCCTETemplateFactory(){
		if(tf==null){
			tf=new CCTETemplateFactory();
			tf.applyProperties(initProperties);
			tf.compileTemplates();
		}
		return tf;
	}
	/**获取 initProperties*/
	public Properties getInitProperties() {
		return initProperties;
	}
	/**设置 initProperties*/
	public void setInitProperties(Properties initProperties) {
		this.initProperties = initProperties;
	}
}

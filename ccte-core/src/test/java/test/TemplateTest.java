package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.ccte.core.CCTETemplate;
import com.ccte.core.CCTETemplateFactory;

/**
 * © 2015-2017 Chenxj Copyright
 * 类    名：TemplateTest
 * 类 描 述：
 * 作    者：Chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2017年4月24日-上午11:49:16
 */
public class TemplateTest {
	public static void main(String[] args) throws Exception {
		CCTETemplateFactory fac=new CCTETemplateFactory();
		Properties properties=new Properties();
		properties.put("templateLoadPath", "classpath*:templates,templates");
		fac.applyProperties(properties);
		fac.compileTemplates(Thread.currentThread()
				.getContextClassLoader());
		
		new Thread(()->{
			while(true) {
				try {
					String turl="test.html";
					CCTETemplate template=fac.findTemplate(turl);
					Map<String, Object>model=new HashMap<>();
					model.put("name", "Seemie的博客-Seemie的个人博客-CCLooMi");
					model.put("names", new String[]{"Seemie","Tommy","Google","Apple"});
					Map<String, Object>copyright=new HashMap<>();
					copyright.put("desc", " background-color:rgba(255,255,255,0.4); //透明度调整范围（0~1) //背景色颜色值必需是RGB值 a //表示透明度alpha  href表示http+reference display:flex align-");
					model.put("copyright", copyright);
					template.render(model, System.out);
					Thread.sleep(5000);
				} catch (Exception e) {
					break;
				}
			}
		}).start();
	}
}

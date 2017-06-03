package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ccte.core.CCTETemplate;
import ccte.core.CCTETemplateFactory;

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
		fac.applyProperties(new Properties());
		CCTETemplate template=fac.getTemplate("templates/test.html");
		Map<String, Object>model=new HashMap<>();
		model.put("name", "Seemie的博客-Seemie的个人博客-CCLooMi");
		model.put("names", new String[]{"Seemie","Tommy","Google","Apple"});
		Map<String, Object>copyright=new HashMap<>();
		copyright.put("desc", "CCLooMi All right resoved.2009-2017");
		model.put("copyright", copyright);
		template.render(model, System.out);
	}
}

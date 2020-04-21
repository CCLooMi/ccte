package com.ccte.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccte.core.compiler.CCTECompiler;
import com.ccte.core.compiler.CCTECompilerResult;
import com.ccte.core.compiler.CCTEParser;
import com.ccte.core.util.ResourceResolveUtil;
import com.ccte.core.util.ResourceResolveUtil.FileFilter;
import com.ccte.core.util.ResourceResolveUtil.JarEntryFilter;
import com.ccte.core.util.digest.DigestUtils;

/**@类名 CCTETemplateFactory
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月6日-下午2:41:22
 */
public final class CCTETemplateFactory implements CCTEConstant{
	private static final Logger log=LoggerFactory.getLogger(CCTETemplateFactory.class);
	private static Map<String, CCTETemplate>templatesMap=new HashMap<>();
	private Charset charset;
	private String suffix;
	private CCTEParser parser;
	private String[] classTemplateLoadPath;
	private String[] fileTemplateLoadPath;
	private Map<String, CCTEDocument>cctedocMap;
	private Map<String, Document>docMap;
	private Map<String, Set<String>>docImports;
	private Map<String, Set<String>>docSets;
	public CCTETemplateFactory(){
		cctedocMap=new HashMap<>();
		docMap=new HashMap<>();
		docImports=new HashMap<>();
		docSets=new HashMap<>();
	}
	private CCTETemplateFactory scanTemplates(){
		JarEntryFilter je=null;
		FileFilter ff=null;
		
		if(classTemplateLoadPath!=null) {
			je=new JarEntryFilter() {
				@Override
				public void filter(JarEntry je) {
					String name=je.getName();
					if(name.charAt(name.length()-1)!='/') {
						for(int i=0;i<classTemplateLoadPath.length;i++) {
							if(name.startsWith(classTemplateLoadPath[i])&&name.endsWith(suffix)) {
								int idx=name.lastIndexOf('/');
								cctedocMap.put(name.substring(idx<0?0:idx),
										new CCTEDocument(ClassLoader.getSystemResourceAsStream(name), charset, name));
							}
						}
					}
				}
			};
		}
		if(fileTemplateLoadPath!=null) {
			ff=new FileFilter() {
				@Override
				public void filter(Path file) {
					for(int i=0;i<fileTemplateLoadPath.length;i++) {
						file=file.resolve(fileTemplateLoadPath[i]);
						if(file.toFile().exists()) {
							try {
								Files.walkFileTree(file, new SimpleFileVisitor<Path>() {
									@Override
									public FileVisitResult visitFile(Path pf, BasicFileAttributes attrs) throws IOException {
										File f=pf.toFile();
										if(f.getName().endsWith(suffix)) {
											cctedocMap.put(f.getName(),
													new CCTEDocument(new FileInputStream(f),charset,f.getAbsolutePath()));
										}
										return FileVisitResult.CONTINUE;
									}
								});
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
		}
		
		if(je!=null||ff!=null) {
			ResourceResolveUtil.resolveAllFile(je,ff,false);
		}
		return this;
	}
	private CCTETemplateFactory pretreatment(){
		for(CCTEDocument cctedoc:cctedocMap.values()){
			cctedoc.repareRef(cctedocMap);
		}
		for(Entry<String, CCTEDocument>entry:cctedocMap.entrySet()){
			if(!entry.getValue().isSnippet()){
				Document doc=Jsoup.parse(entry.getValue().toString());
				docMap.put(entry.getKey(), doc);
				//处理import
				Set<String>imports=docImports.get(entry.getKey());
				if(imports==null){
					imports=new HashSet<>();
					docImports.put(entry.getKey(), imports);
				};
				Elements es=doc.getElementsByTag("import");
				int esl=es.size();
				for(int i=0;i<esl;i++){
					String[]ss=es.get(i).ownText().trim().split(",+");
					es.get(i).remove();
					int ssl=ss.length;
					for(int ii=0;ii<ssl;ii++){
						imports.add(ss[ii]);
					}
				}
				//处理set，用于类型推断
				Set<String>sets=docSets.get(entry.getKey());
				if(sets==null){
					sets=new HashSet<>();
					docSets.put(entry.getKey(), sets);
				}
				es=doc.getElementsByTag("set");
				esl=es.size();
				for(int i=0;i<esl;i++){
					String[]ss=es.get(i).ownText().trim().split(",+");
					es.get(i).remove();
					int ssl=ss.length;
					for(int ii=0;ii<ssl;ii++){
						sets.add(ss[ii]);
					}
				}
			}
		}
		cctedocMap.clear();
		cctedocMap=null;
		return this;
	}
	/**
	 * @名称 compile
	 * @说明	编译
	 * @作者 Chenxj
	 * @邮箱 chenios@foxmail.com
	 * @日期 2017年4月7日-下午4:01:24
	 * @return
	 */
	private CCTETemplateFactory compile(){
		if(docMap.isEmpty()) {
			return this;
		}
		ICompilationUnit[]compilationUnits=new ICompilationUnit[docMap.size()];
		//新生成的class名称和对应的文档key的映射map
		Map<String, String>classDocMap=new HashMap<>();
		int docId=0;
		StringBuilder fileHead=new StringBuilder();
		for(Entry<String, Document>entry:docMap.entrySet()){
			fileHead.delete(0, fileHead.length());
			fileHead
			.append("import java.util.*;")
			.append("import java.io.*;")
			.append("import java.math.*;");
			
			Set<String>fieldNames=new HashSet<>();
			Set<String>imports=docImports.get(entry.getKey());
			for(String imp:imports){
				//一定要去掉空格
				fileHead.append("import ").append(imp.trim()).append(';');
			}
			Set<String>sets=docSets.get(entry.getKey());
			String newClassName="CCTE"+DigestUtils.MD5HEX(entry.getKey());
			
			classDocMap.put(newClassName, entry.getKey());
			fileHead.append("public class ")
			.append(newClassName)
			.append(" extends ")
			.append(CCTETemplate.class.getName())
			.append('{');
			
			StringBuilder java=parser.document2java(entry.getValue(),fieldNames,fileHead);
			StringBuilder java_before=new StringBuilder("public void render(Map<String, Object>model,OutputStream out) throws Exception {");
			for(String set:sets){
				set=set.replaceAll("^ +", "");
				String[]ss=set.split(" +");
				java_before.append(set).append("=(").append(ss[0]).append(")model.get(\"").append(ss[1].trim()).append("\");");
			}
			java.insert(0, java_before).append("}}");
			
			fileHead.append(java);
			//TODO
//			System.out.println(fileHead);
			char[]content=new char[fileHead.length()];
			fileHead.getChars(0, fileHead.length(), content, 0);
			compilationUnits[docId++]=new CompilationUnit(content, newClassName, charset.name(), "none", true);
		}
		CCTECompiler.compile(compilationUnits,new CCTECompilerResult<CCTETemplate>() {
			@Override
			public void result(String name, CCTETemplate instance) {
				templatesMap.put(classDocMap.get(name), instance.setCharset(charset));
				log.info("mapping [{}] to [{}]",classDocMap.get(name), name);
			}
		});
		return this;
	}
	public boolean checkTemplate(String url){
		return templatesMap.containsKey(url);
	}
	public CCTETemplate findTemplate(String url){
		return templatesMap.get(url);
	}
	/**@名称 applyProperties
	 * @说明 
	 * @作者 Chenxj
	 * @邮箱 chenios@foxmail.com
	 * @日期 2017年4月6日-下午4:15:30
	 * @param properties
	 */
	public CCTETemplateFactory applyProperties(Properties properties) {
		String[] ps=properties.getProperty("templateLoadPath", "templates").split(",|;");
		List<String>cps=new ArrayList<>();
		List<String>fps=new ArrayList<>();
		for(String p:ps) {
			if(p.startsWith("classpath*:")) {
				cps.add(p.substring(11, p.length()));
				fps.add(cps.get(cps.size()-1));
			}else {
				fps.add(p);
			}
		};
		if(cps.size()>0) {
			classTemplateLoadPath=new String[cps.size()];
			cps.toArray(classTemplateLoadPath);
		}
		if(fps.size()>0) {
			fileTemplateLoadPath=new String[fps.size()];
			fps.toArray(fileTemplateLoadPath);
		}
		this.charset=Charset.forName(properties.getProperty("charset", "UTF-8"));
		this.parser=new CCTEParser(charset);
		this.suffix=properties.getProperty("suffix", ".html");
		return this;
	}

	/**@名称 compileTemplates
	 * @说明 编译模版到class
	 * @作者 Chenxj
	 * @邮箱 chenios@foxmail.com
	 * @日期 2017年4月7日-下午3:58:55
	 */
	public CCTETemplateFactory compileTemplates() {
		return scanTemplates()
		.pretreatment()
		.compile();
	}
	/**获取 charset
	 * 可用于response设置返回编码*/
	public Charset getCharset() {
		return charset;
	}
}

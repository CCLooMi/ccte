package com.ccte.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类    名：ResourceResolveUtil
 * 类 描 述：
 * 作    者：Chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2020年04月18日 22:52:36
 */
public class ResourceResolveUtil {
	/**
	 * 扫描所有类加载路径下所有资源
	 * @param jeFilter jar中资源过滤器
	 * @param fileFilter 文件资源过滤器
	 * @param walkDirectory 是否遍历文件夹
	 */
	public static void resolveAllFile(JarEntryFilter jeFilter,FileFilter fileFilter,boolean walkDirectory) {
		splitByChar(System.getProperty("java.class.path"), ':', path->{
			File file=new File(path);
			if(file.isFile()) {
				if(jeFilter!=null) {
					JarFile jar=null;
					try {
						jar = new JarFile(file);
						Enumeration<JarEntry> entrys = jar.entries();
				        while(entrys.hasMoreElements()){
				        	jeFilter.filter(entrys.nextElement());
				        }
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if(jar!=null) {
							try {jar.close();}catch (Exception e) {}
						}
					}
				}
			}else if(file.isDirectory()) {
				if(fileFilter!=null) {
					if(walkDirectory) {
						try {
							Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
								@Override
								public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
									fileFilter.filter(file);
									return FileVisitResult.CONTINUE;
								}
							});
						}catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						fileFilter.filter(file.toPath());
					}
				}
			}
		});
	}
	public static void splitByChar(String s,char splitChar,PathFilter filter) {
		int pL=s.length();
		int i=0,j=-1;
		for(;i<pL;i++) {
			if(s.charAt(i)==splitChar&&i>j) {
				if(i!=j+1) {
					filter.filter(s.substring(j+1, i));
				}
				j=i;
			}
		}
		if(i>j&&i!=j+1) {
			filter.filter(s.substring(j+1, i));
		}
	}
	public static interface JarEntryFilter{
		public void filter(JarEntry je);
	}
	public static interface FileFilter{
		public void filter(Path file);
	}
	public static interface PathFilter{
		public void filter(String path);
	}
}

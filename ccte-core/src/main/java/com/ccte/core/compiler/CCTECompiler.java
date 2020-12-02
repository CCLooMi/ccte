package com.ccte.core.compiler;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**@类名 CCTECompiler
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月13日-下午3:41:12
 */
public class CCTECompiler {
	private static final String charset="UTF-8";
	private static final Logger log=LoggerFactory.getLogger(CCTECompiler.class);
	private static final INameEnvironment ne=new CCTENameEnvironment();
	private static final IErrorHandlingPolicy ehp=DefaultErrorHandlingPolicies.proceedWithAllProblems();
	private static final CompilerOptions options=new CompilerOptions();
	private static final IProblemFactory pf=new DefaultProblemFactory();
	private static final PrintWriter pw=new PrintWriter(System.out);
	private static final CompilationProgress progress=new CCTECompilationProgress();
	
	public static <T>void compile(ICompilationUnit[]sourceUnits,
			CCTECompilerResult<T> compilerResult,ClassLoader cl){
		options.set(getSettings());
		Compiler compiler=new Compiler(
				ne,
				ehp,
				options,
				(CompilationResult result)->{
					boolean hasError=false;
					if(result.hasProblems()){
						IProblem[] problems=result.getProblems();
						for(int i=0;i<problems.length;i++){
							if(problems[i].isError()){
								hasError=true;
								log.error(problems[i].getMessage());
							}
						}
					}
					if(!hasError){
						ClassFile[]classFiles=result.getClassFiles();
						try{
							for(int i=0;i<classFiles.length;i++){
								char[][]cns=classFiles[i].getCompoundName();
								StringBuilder compoundName=new StringBuilder();
								String sep="";
								for(int ii=0;ii<cns.length;ii++){
									compoundName.append(sep).append(cns[ii]);
									sep=".";
								}
								String className=compoundName.toString();
								CCTEClassLoad classLoad=new CCTEClassLoad(cl);
								Class<T>nc=classLoad.loadClass(classFiles[i].getBytes(), className);
								Constructor<?>cons=nc.getConstructor();
								if(!Modifier.isPublic(nc.getModifiers())){
									AccessController.doPrivileged(new PrivilegedAction<Void>() {
						                public Void run() {
						                    cons.setAccessible(true);
						                    return null;
						                }
						            });
								}
								compilerResult.result(className, nc.newInstance());
							}
						}catch (Exception e) {
							log.info("类加载异常:\t", e);
						}
					}else{
						log.error("出错代码:\t{}", new String(result.getCompilationUnit().getContents()));
					}
				},
				pf,
				pw,
				progress);
		compiler.compile(sourceUnits);
	}
	public static <T>void compile(StringBuilder source,String className,
			CCTECompilerResult<T> compilerResult,ClassLoader cl){
		char[]content=new char[source.length()];
		source.getChars(0, source.length(), content, 0);
		compile(new ICompilationUnit[]{new CompilationUnit(content, className, charset, Main.NONE, true,null)},
				compilerResult,cl);
	}
	public static <T>void compile(String source,String className,
			CCTECompilerResult<T> compilerResult,ClassLoader cl){
		compile(new ICompilationUnit[]{new CompilationUnit(source.toCharArray(), className, charset, Main.NONE, true, null)},
				compilerResult,cl);
	}
	public static class CCTEClassLoad extends ClassLoader{
		private CCTEClassLoad(ClassLoader pcl) {
			super(pcl);
		}
		@SuppressWarnings("unchecked")
		public <T>Class<T> loadClass(byte[]b,String className){
			return (Class<T>) defineClass(className, b, 0, b.length);
		}
	}
	private static Map<String,String> getSettings() {
        Map<String,String> settings = new HashMap<>();
        settings.put(CompilerOptions.OPTION_LineNumberAttribute,CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_SourceFileAttribute,CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_ReportDeprecation,CompilerOptions.IGNORE);
        settings.put(CompilerOptions.OPTION_Encoding,System.getProperty("file.encoding"));
        // Source JVM
        settings.put(CompilerOptions.OPTION_Source, System.getProperty("java.specification.version"));
        // Target JVM
        settings.put(CompilerOptions.OPTION_TargetPlatform,System.getProperty("java.specification.version"));
        settings.put(CompilerOptions.OPTION_Compliance,System.getProperty("java.specification.version"));
        return settings;
    }
}

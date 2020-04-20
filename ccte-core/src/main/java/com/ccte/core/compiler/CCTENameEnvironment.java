package com.ccte.core.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

/**@类名 CCTENameEnvironment
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月12日-下午2:50:38
 */
public class CCTENameEnvironment implements INameEnvironment{
	private Map<Integer, NameEnvironmentAnswer>neamap=new HashMap<>();
	private Set<Integer>packages=new HashSet<>();
	private StringBuilder result=new StringBuilder();
	
	@Override
	public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
//		StringBuilder result=new StringBuilder();
		result.delete(0, result.length());
		String sep="";
		for(int i=0;i<compoundTypeName.length;i++){
			result.append(sep).append(compoundTypeName[i]);
			sep=".";
		}
		return findType(result.toString());
	}
	@Override
	public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) {
//		StringBuilder result=new StringBuilder();
		result.delete(0, result.length());
		String sep="";
		for(int i=0;i<packageName.length;i++){
			result.append(sep).append(packageName[i]);
			sep=".";
		}
		result.append(sep).append(typeName);
		return findType(result.toString());
	}

	@Override
	public boolean isPackage(char[][] parentPackageName, char[] packageName) {
//		StringBuilder result=new StringBuilder();
		result.delete(0, result.length());
		String sep="";
		if(parentPackageName!=null){
			for(int i=0;i<parentPackageName.length;i++){
				result.append(sep).append(parentPackageName[i]);
				sep=".";
			}
		}
		if(Character.isUpperCase(packageName[0])){
			if(!isPackage(result.toString())){
				return false;
			}
		}
		result.append(sep).append(packageName);
		return isPackage(result.toString());
	}

	@Override
	public void cleanup() {
		neamap.clear();
		packages.clear();
		result.delete(0, result.length());
	}

	private NameEnvironmentAnswer findType(String className){
		int hash=className.hashCode();
		NameEnvironmentAnswer nea=neamap.get(hash);
		if(nea!=null){
			return nea;
		}else{
			try{
				ClassFileReader classFileRead=new ClassFileReader(readClass(className, true), className.toCharArray());
				nea=new NameEnvironmentAnswer(classFileRead, null);
				neamap.put(hash, nea);
				return nea;
			}catch (Exception e) {
				return null;
			}
		}
	}
	private boolean isPackage(String result){
		int hash=result.hashCode();
		if(packages.contains(hash)){
			return true;
		}else{
			try {
				Class.forName(result);
				return false;
			} catch (ClassNotFoundException e) {
				packages.add(hash);
				return true;
			}
		}
	}
	private byte[] readClass(String className, boolean close)throws IOException {
		InputStream is=null;
		if(!isPackage(className)){
			String clazz=className.replace('.', '/')+ ".class";
			is=ClassLoader.getSystemResourceAsStream(clazz);
			if(is==null){
				is=getClass().getClassLoader()
						.getResourceAsStream(clazz);
			}
	        if (is == null) {
	            throw new IOException("Class file read error.");
	        }
		}else{
			throw new IOException("Class not found");
		}
        try {
            byte[] b = new byte[is.available()];
            int len = 0;
            while (true) {
                int n = is.read(b, len, b.length - len);
                if (n == -1) {
                    if (len < b.length) {
                        byte[] c = new byte[len];
                        System.arraycopy(b, 0, c, 0, len);
                        b = c;
                    }
                    return b;
                }
                len += n;
                if (len == b.length) {
                    int last = is.read();
                    if (last < 0) {
                        return b;
                    }
                    byte[] c = new byte[b.length + 1000];
                    System.arraycopy(b, 0, c, 0, len);
                    c[len++] = (byte) last;
                    b = c;
                }
            }
        } finally {
            if (close) {
                is.close();
            }
        }
    }
}

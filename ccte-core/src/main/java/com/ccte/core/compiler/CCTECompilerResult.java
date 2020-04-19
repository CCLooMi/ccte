package com.ccte.core.compiler;

/**@类名 CCTECompilerRequestor
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月12日-下午3:01:38
 */
public interface CCTECompilerResult<T>{
	public void result(String name,T instance);
}

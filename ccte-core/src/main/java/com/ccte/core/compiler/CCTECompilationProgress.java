package com.ccte.core.compiler;

import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**@类名 CCTECompilationProgress
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月12日-下午3:04:11
 */
public class CCTECompilationProgress extends CompilationProgress{
	private Logger log=LoggerFactory.getLogger(getClass());
	
	@Override
	public void begin(int remainingWork) {
	}

	@Override
	public void done() {
	}

	@Override
	public boolean isCanceled() {
		return false;
	}

	@Override
	public void setTaskName(String name) {
		log.info(name);
	}

	@Override
	public void worked(int workIncrement, int remainingWork) {
		log.info("Complete/Remaining [{}/{}]",workIncrement,remainingWork);
	}

}

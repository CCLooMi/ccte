package ccte.core.compiler;

/**@类名 CCTESingleCompilerResult
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月13日-下午5:21:08
 */
public class CCTESingleCompilerResult<T> implements CCTECompilerResult<T>{
	private T instance;
	@Override
	public void result(String name, T instance) {
		this.instance=instance;
	}
	public T getInstance(){
		return instance;
	}
}

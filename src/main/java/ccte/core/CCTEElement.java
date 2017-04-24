package ccte.core;

/**@类名 CCTEElement
 * @说明	html元素类，用于解决include问题
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月7日-下午5:18:20
 */
public class CCTEElement {
	/**字符串数据*/
	private String data;
	/**引用的html文件名*/
	private String refName;
	/**引用数据*/
	private CCTEDocument refDoc;
	public CCTEElement(String data,String refName){
		this.data=data;
		this.refName=refName;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getRefName() {
		return refName;
	}
	public void setRefName(String refName) {
		this.refName = refName;
	}
	
	public CCTEDocument getRefDoc() {
		return refDoc;
	}
	public void setRefDoc(CCTEDocument refDoc) {
		this.refDoc = refDoc;
	}
	@Override
	public String toString() {
		if(data==null&&refDoc==null){
			return "";
		}else if(data==null){
			return refDoc.toString();
		}else if(refDoc==null){
			return data;
		}else{
			return data+refDoc.toString();
		}
	}
}

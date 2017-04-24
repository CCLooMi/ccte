package ccte.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ccte.core.compiler.CCTECompiler;
import ccte.core.compiler.CCTESingleCompilerResult;

/**@类名 MapBean
 * @说明 
 * @作者 Chenxj
 * @邮箱 chenios@foxmail.com
 * @日期 2017年4月13日-下午2:07:41
 */
public abstract class MapBean {
	public MapBean clone(){return null;};
	public Object getAttr(String name){return null;}
	public void setAttr(String name,Object value){}
	public boolean hasAttr(String name){return false;}
	/**
	 * @名称 createMapBeanByMap
	 * @说明	创建MapBean
	 * @作者 Chenxj
	 * @邮箱 chenios@foxmail.com
	 * @日期 2017年4月13日-下午5:01:02
	 * @param map
	 * @return
	 */
	public static <T>MapBean createMapBeanByMap(Map<String, ? extends Object>map,Class<T>t){
		if(map!=null&&!map.isEmpty()){
			String className="MapBean_"+Integer.toHexString(System.identityHashCode(map));
			StringBuilder sb=new StringBuilder();
			sb.append("public class ")
			.append(className)
			.append(" extends ").append(MapBean.class.getName())
			.append('{');
			String[]fields=new String[map.size()];
			map.keySet().toArray(fields);
			//key中可能包含非法字符，因此这里计算key的hashCode的16进制字符串作为属性值
			for(int i=0;i<fields.length;i++){
				sb.append("private ").append(t.getName()).append(" _")
				.append(Integer.toHexString(fields[i].hashCode()))
				.append(';');
			}
			sb.append("public ").append(MapBean.class.getName()).append(" clone(){")
			.append("return new ").append(className)
			.append("();}");
			sb.append("public Object getAttr(String attr){");
			for(int i=0;i<fields.length;i++){
				if(i==0){
					sb.append("if(\"");
				}else{
					sb.append("else if(\"");
				}
				sb.append(fields[i])
				.append("\".equals(attr)){return this._")
				.append(Integer.toHexString(fields[i].hashCode()))
				.append(";}");
			}
			sb.append("else{return null;}}");
			
			sb.append("public void setAttr(String attr,Object value){");
			for(int i=0;i<fields.length;i++){
				if(i==0){
					sb.append("if(\"");
				}else{
					sb.append("else if(\"");
				}
				sb.append(fields[i])
				.append("\".equals(attr)){this._")
				.append(Integer.toHexString(fields[i].hashCode()));
				if(t==Object.class){
					sb.append("=value;}");
				}else{
					sb.append("=(").append(t.getName()).append(")value;}");
				}
			}
			sb.append('}');
			
			sb.append("public boolean hasAttr(String attr){");
			for(int i=0;i<fields.length;i++){
				if(i==0){
					sb.append("if(\"");
				}else{
					sb.append("else if(\"");
				}
				sb.append(fields[i])
				.append("\".equals(attr)){return true;}");
			}
			sb.append("else{return false;}}}");
			
			CCTESingleCompilerResult<MapBean>result=new CCTESingleCompilerResult<>();
			CCTECompiler.compile(sb, className,result);
			MapBean mb=result.getInstance();
			for(Entry<String,? extends Object>entry:map.entrySet()){
				mb.setAttr(entry.getKey(), entry.getValue());
			}
			return mb;
		}
		return null;
	}
	public static MapBean createMapBeanByMap(Map<String, ? extends Object>map){
		if(map!=null&&!map.isEmpty()){
			String className="MapBean_"+Integer.toHexString(System.identityHashCode(map));
			StringBuilder sb=new StringBuilder();
			sb.append("public class ")
			.append(className)
			.append(" extends ").append(MapBean.class.getName())
			.append('{');
			String[]fields=new String[map.size()];
			map.keySet().toArray(fields);
			//key中可能包含非法字符，因此这里计算key的hashCode的16进制字符串作为属性值
			for(int i=0;i<fields.length;i++){
				sb.append("private Object _")
				.append(Integer.toHexString(fields[i].hashCode()))
				.append(';');
			}
			sb.append("public ").append(MapBean.class.getName()).append(" clone(){")
			.append("return new ").append(className)
			.append("();}");
			sb.append("public Object getAttr(String attr){");
			for(int i=0;i<fields.length;i++){
				if(i==0){
					sb.append("if(\"");
				}else{
					sb.append("else if(\"");
				}
				sb.append(fields[i])
				.append("\".equals(attr)){return this._")
				.append(Integer.toHexString(fields[i].hashCode()))
				.append(";}");
			}
			sb.append("else{return null;}}");
			
			sb.append("public void setAttr(String attr,Object value){");
			for(int i=0;i<fields.length;i++){
				if(i==0){
					sb.append("if(\"");
				}else{
					sb.append("else if(\"");
				}
				sb.append(fields[i])
				.append("\".equals(attr)){this._")
				.append(Integer.toHexString(fields[i].hashCode()));
				sb.append("=value;}");
			}
			sb.append('}');
			
			sb.append("public boolean hasAttr(String attr){");
			for(int i=0;i<fields.length;i++){
				if(i==0){
					sb.append("if(\"");
				}else{
					sb.append("else if(\"");
				}
				sb.append(fields[i])
				.append("\".equals(attr)){return true;}");
			}
			sb.append("else{return false;}}}");
			
			CCTESingleCompilerResult<MapBean>result=new CCTESingleCompilerResult<>();
			CCTECompiler.compile(sb, className,result);
			MapBean mb=result.getInstance();
			for(Entry<String,? extends Object>entry:map.entrySet()){
				mb.setAttr(entry.getKey(), entry.getValue());
			}
			return mb;
		}
		return null;
	}
	public static <T>MapBean createMapBean(Class<T>c){
		String newClassName=c.getSimpleName()+'_'+Integer.toHexString(c.getName().hashCode());
		try{
			Class<?>clazz=CCTECompiler.classLoad.loadClass(newClassName);
			return (MapBean) clazz.newInstance();
		}catch (Exception e) {
			StringBuilder sb=new StringBuilder();
			sb.append("public class ")
			.append(newClassName)
			.append(" extends ").append(MapBean.class.getName())
			.append('{');
			Field[]fields=c.getDeclaredFields();
			for(int i=0;i<fields.length;i++){
				sb.append("private ")
				.append(fields[i].getType().getName())
				.append(' ')
				.append(fields[i].getName())
				.append(';');
			}
			sb.append("public ").append(MapBean.class.getName()).append(" clone(){")
			.append("return new ").append(newClassName)
			.append("();}");
			if(fields.length>0){
				sb.append("public Object getAttr(String attr){");
				for(int i=0;i<fields.length;i++){
					if(i==0){
						sb.append("if(\"");
					}else{
						sb.append("else if(\"");
					}
					sb.append(fields[i].getName())
					.append("\".equals(attr)){return this.")
					.append(fields[i].getName())
					.append(";}");
				}
				sb.append("else{return null;}}");
				
				sb.append("public void setAttr(String attr,Object value){");
				for(int i=0;i<fields.length;i++){
					if(i==0){
						sb.append("if(\"");
					}else{
						sb.append("else if(\"");
					}
					sb.append(fields[i].getName())
					.append("\".equals(attr)){this.")
					.append(fields[i].getName())
					.append("=(")
					.append(fields[i].getType().getName())
					.append(")value;}");
				}
				sb.append('}');
				
				sb.append("public boolean hasAttr(String attr){");
				for(int i=0;i<fields.length;i++){
					if(i==0){
						sb.append("if(\"");
					}else{
						sb.append("else if(\"");
					}
					sb.append(fields[i])
					.append("\".equals(attr)){return true;}");
				}
				sb.append("else{return false;}}");
			}else{
				sb.append("public Object getAttr(String attr){return null;}")
				.append("public void setAttr(String attr,Object value){}");
			}
			sb.append('}');
			CCTESingleCompilerResult<MapBean>result=new CCTESingleCompilerResult<>();
			CCTECompiler.compile(sb, newClassName,result);
			return result.getInstance();
		}
		
	}
	public static void main(String[] args) {
		Map<String, Object>om=new HashMap<>();
		om.put("age", 123);
		MapBean mb=createMapBeanByMap(om, Integer.class);
		System.out.println(mb.getAttr("age"));
	}
}

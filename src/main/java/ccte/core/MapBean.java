package ccte.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
	/**
	 * @名称 createMapBeanByMap
	 * @说明	创建MapBean
	 * @作者 Chenxj
	 * @邮箱 chenios@foxmail.com
	 * @日期 2017年4月13日-下午5:01:02
	 * @param map
	 * @return
	 */
	public static <T>MapBean createMapBeanByMap(Map<String, ? extends Object>map){
		if(map!=null&&!map.isEmpty()){
			String className="MapBean_"+Integer.toHexString(System.identityHashCode(map));
			StringBuilder sb=new StringBuilder();
			sb.append("public class ")
			.append(className)
			.append(" extends ").append(MapBean.class.getName())
			.append('{');
			parserFields(map, sb);
			sb.append("public ").append(MapBean.class.getName()).append(" clone(){")
			.append("return new ").append(className)
			.append("();}}");
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
			parserFields(c, sb);
			sb.append("public ").append(MapBean.class.getName()).append(" clone(){")
			.append("return new ").append(newClassName)
			.append("();}}");
			CCTESingleCompilerResult<MapBean>result=new CCTESingleCompilerResult<>();
			CCTECompiler.compile(sb, newClassName,result);
			return result.getInstance();
		}
		
	}
	public static MapBean createMapBean(Object o){
		MapBean mb;
		String newClassName=o.getClass().getSimpleName()+'_'+Integer.toHexString(o.getClass().getName().hashCode());
		try{
			Class<?>clazz=CCTECompiler.classLoad.loadClass(newClassName);
			mb = (MapBean)clazz.newInstance();
		}catch (Exception e) {
			StringBuilder sb=new StringBuilder();
			sb.append("public class ")
			.append(newClassName)
			.append(" extends ").append(MapBean.class.getName())
			.append('{');
			parserFields(o, sb);
			sb.append("public ").append(MapBean.class.getName()).append(" clone(){")
			.append("return new ").append(newClassName)
			.append("();}}");
			CCTESingleCompilerResult<MapBean>result=new CCTESingleCompilerResult<>();
			CCTECompiler.compile(sb, newClassName,result);
			mb = result.getInstance();
		}
		Field[]fs=o.getClass().getDeclaredFields();
		try{
			for(int i=0;i<fs.length;i++){
				if(!"serialVersionUID".equals(fs[i].getName())&&!"this$0".equals(fs[i].getName())){
					fs[i].setAccessible(true);
					mb.setAttr(fs[i].getName(), fs[i].get(o));
				}
			}
		}catch (Exception e) {}
		return mb;
	}
	private static void parserFields(Object obj,StringBuilder sb){
		int type=-1;
		Field[]fields=null;
		Map<Integer, List<Field>>lfieldsMap=new TreeMap<>();
		if(obj instanceof Class<?>){
			fields=((Class<?>)obj).getDeclaredFields();
			if(fields.length>0){
				type=1;
			}else{
				type=0;
			}
		}else if(obj instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String, Object>m=(Map<String, Object>) obj;
			if(!m.isEmpty()){
				Map<Integer, List<String>>lkeysMap=new HashMap<>();
				for(Entry<String, Object>entry:m.entrySet()){
					Object v=entry.getValue();
					sb.append("private ").append(v==null?"Object":v.getClass().getSimpleName()).append(" _")
					.append(Integer.toHexString(entry.getKey().hashCode())).append(';');
					
					Integer fnl=new Integer(entry.getKey().length());
					if(lkeysMap.containsKey(fnl)){
						lkeysMap.get(fnl).add(entry.getKey());
					}else{
						List<String>ls=new ArrayList<>();
						ls.add(entry.getKey());
						lkeysMap.put(fnl, ls);
					}
				}
				//生成setAttr方法
				sb.append("public void setAttr(String attrName,Object attrValue){switch(attrName.length()){");
				for(Entry<Integer, List<String>> entry:lkeysMap.entrySet()){
					sb.append("case ").append(entry.getKey()).append(":");
					List<String>ls=entry.getValue();
					int lsl=ls.size();
					if(lsl>1){
						for(int i=0;i<lsl;i++){
							String fieldName=ls.get(i);
							if(i==0){
								sb.append("if(\"").append(fieldName).append("\".equals(attrName)){");
							}else{
								sb.append("}else if(\"").append(fieldName).append("\".equals(attrName)){");
							}
							sb.append("this._").append(Integer.toHexString(fieldName.hashCode()));

							Object v=m.get(fieldName);
							sb.append("=(").append(v==null?"Object":v.getClass().getSimpleName()).append(")attrValue;");
						}
						sb.append("}break;");
					}else{
						sb.append("this._").append(Integer.toHexString(ls.get(0).hashCode()));
						Object v=m.get(ls.get(0));
						sb.append("=(").append(v==null?"Object":v.getClass().getSimpleName()).append(")attrValue;");
						sb.append("break;");
					}
				}
				sb.append("default:}}");
				
				//生成getAttr方法
				sb.append("public Object getAttr(String attrName){switch(attrName.length()){");
				for(Entry<Integer, List<String>> entry:lkeysMap.entrySet()){
					sb.append("case ").append(entry.getKey()).append(":");
					List<String>ls=entry.getValue();
					int lsl=ls.size();
					if(lsl>1){
						for(int i=0;i<lsl;i++){
							String fieldName=ls.get(i);
							if(i==0){
								sb.append("if(\"").append(fieldName).append("\".equals(attrName)){");
							}else{
								sb.append("}else if(\"").append(fieldName).append("\".equals(attrName)){");
							}
							sb.append("return _").append(Integer.toHexString(fieldName.hashCode())).append(";");
						}
						sb.append("}");
					}else{
						sb.append("return _").append(Integer.toHexString(ls.get(0).hashCode())).append(";");
					}
				}
				sb.append("default:return null;}}");
			}else{
				type=0;
			}
		}else{
			fields=obj.getClass().getDeclaredFields();
			if(fields.length>0){
				type=2;
			}else{
				type=0;
			}
		}
		if(type==1||type==2){
			for(int i=0;i<fields.length;i++){
				String fieldName=fields[i].getName();
				//this$0 内部类特有属性
				if(!"serialVersionUID".equals(fieldName)&&!"this$0".equals(fieldName)){
					sb.append("private ").append(fields[i].getType().getName()).append(' ').append(fields[i].getName()).append(';');
					Integer fnl=new Integer(fieldName.length());
					if(lfieldsMap.containsKey(fnl)){
						lfieldsMap.get(fnl).add(fields[i]);
					}else{
						List<Field>ls=new ArrayList<>();
						ls.add(fields[i]);
						lfieldsMap.put(fnl, ls);
					}
				}
			}
			//生成setAttr方法
			sb.append("public void setAttr(String attrName,Object attrValue){switch(attrName.length()){");
			for(Entry<Integer, List<Field>> entry:lfieldsMap.entrySet()){
				sb.append("case ").append(entry.getKey()).append(":");
				List<Field>ls=entry.getValue();
				int lsl=ls.size();
				if(lsl>1){
					for(int i=0;i<lsl;i++){
						Field field=ls.get(i);
						if(i==0){
							sb.append("if(\"").append(field.getName()).append("\".equals(attrName)){");
						}else{
							sb.append("}else if(\"").append(field.getName()).append("\".equals(attrName)){");
						}
						sb.append("this.").append(field.getName());
						sb.append("=(").append(field.getType().getSimpleName()).append(")attrValue;");
					}
					sb.append("}break;");
				}else{
					sb.append("this.").append(ls.get(0).getName());
					sb.append("=(").append(ls.get(0).getType().getSimpleName()).append(")attrValue;");
					sb.append("break;");
				}
			}
			sb.append("default:}}");
			
			//生成getAttr方法
			sb.append("public Object getAttr(String attrName){switch(attrName.length()){");
			for(Entry<Integer, List<Field>> entry:lfieldsMap.entrySet()){
				sb.append("case ").append(entry.getKey()).append(":");
				List<Field>ls=entry.getValue();
				int lsl=ls.size();
				if(lsl>1){
					for(int i=0;i<lsl;i++){
						Field field=ls.get(i);
						if(i==0){
							sb.append("if(\"").append(field.getName()).append("\".equals(attrName)){");
						}else{
							sb.append("}else if(\"").append(field.getName()).append("\".equals(attrName)){");
						}
						sb.append("return ").append(field.getName()).append(";");
					}
					sb.append("}");
				}else{
					sb.append("return ").append(ls.get(0).getName()).append(";");
				}
			}
			sb.append("default:return null;}}");
		}else if(type==0){
			sb.append("public Object getAttr(String attr){return null;}")
			.append("public void setAttr(String attr,Object value){}");
		}
	}
	public static void main(String[] args) {
		Map<String, Object>om=new HashMap<>();
		om.put("age", 123);
		om.put("name", "Jack");
		om.put("size", 1234l);
		om.put("emails", null);
		MapBean mb=createMapBeanByMap(om);
		System.out.println(mb.getAttr("age"));
		System.out.println(mb.getAttr("name"));
		System.out.println(mb.getAttr("size"));
		System.out.println(mb.getAttr("emails"));
	}
}

package ccte.core.compiler;
import static ccte.core.util.StringUtil.*;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import ccte.core.CCTEConstant;
import ccte.core.CCTEDocument;
import ccte.core.CCTETemplate;

/**© 2015-2017 Chenxj Copyright
 * 类    名：CCTEParser
 * 类 描 述：
 * 作    者：Chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2017年4月19日-下午9:20:05
 */
public class CCTEParser implements CCTEConstant{
	private static final Random random=new Random();
	private Charset charset;
	public CCTEParser(Charset charset){
		this.charset=charset;
	}
	public StringBuilder document2java(Document document,Set<String>fieldNames,StringBuilder fileHead){
		StringBuilder sb=new StringBuilder();
		nodes2string(document.childNodes(), sb);
		sb=proccess_el(sb);
		String[]codes=sb.toString().split(Character.toString(seprator));
		sb.delete(0, sb.length());
		
		int codesl=codes.length;
		for(int i=0;i<codesl;i++){
			if(!codes[i].isEmpty()){
				byte[]bs=codes[i].getBytes(charset);
				if(bs[0]!=codeflag){
					int k=codes[i].hashCode();
					CCTETemplate.resources.put(k, bs);
					addField(fieldNames,fileHead,k);
					sb.append("out.write(_").append(Integer.toHexString(k)).append(");");
					//TODO 用来查看是否有将java代码将html代码输出的情况
//					System.out.println("htmlCodes[i]\t"+codes[i]);
				}else{
					sb.append(codes[i].substring(1, codes[i].length()));
				}
			}
		}
		return sb;
	}
	private void nodes2string(Collection<Node>nodes,StringBuilder sb){
		for(Node node:nodes){
			node2string(node,sb);
		}
	}
	private void node2string(Node node,StringBuilder sb){
		if(node instanceof Element){
			
			Attributes attrs=node.attributes();
			if(attrs.hasKey(_if_)){
				String _if=attrs.get(_if_);
				attrs.remove(_if_);
				
				sb.append(seprator).append(codeflag).append("if(").append(proccessEQExpress(_if)).append("){").append(seprator);
				process_attrs(node, attrs, sb);
				nodes2string(node.childNodes(),sb);
				sb.append("</").append(node.nodeName()).append('>');
				sb.append(seprator).append(codeflag).append("}").append(seprator);
			}else if(attrs.hasKey(_else_if_)){
				String _else_if=attrs.get(_else_if_);
				attrs.remove(_else_if_);

				sb.append(codeflag).append("else if(").append(proccessEQExpress(_else_if)).append("){").append(seprator);
				process_attrs(node, attrs, sb);
				nodes2string(node.childNodes(),sb);
				sb.append("</").append(node.nodeName()).append('>');
				sb.append(seprator).append(codeflag).append("}").append(seprator);
			}else if(attrs.hasKey(_else_)){
				attrs.remove(_else_);

				sb.append(codeflag).append("else{").append(seprator);
				process_attrs(node, attrs, sb);
				nodes2string(node.childNodes(),sb);
				sb.append("</").append(node.nodeName()).append('>');
				sb.append(seprator).append(codeflag).append("}").append(seprator);
			}else if(attrs.hasKey(_for_)){
				String _for=attrs.get(_for_);
				attrs.remove(_for_);
				
				process_attrs(node, attrs, sb);

				//生成for循环内部代码
				StringBuilder forInner=new StringBuilder();
				forInner.append(seprator);
				nodes2string(node.childNodes(),forInner);
				//处理for循环逻辑
				process_for(_for,sb,forInner.toString());
				
				sb.append("</").append(node.nodeName()).append('>');
			}else if(attrs.hasKey(_repeat_)){
				String _repeat=attrs.get(_repeat_);
				attrs.remove(_repeat_);
				//生成for循环内部代码
				StringBuilder forInner=new StringBuilder();
				forInner.append(seprator);
				node2string(node,forInner);
				//处理for循环逻辑
				process_for(_repeat,sb,forInner.toString());
			}else{
				process_attrs(node, attrs, sb);
				if(!voidTags.contains(node.nodeName())){
					nodes2string(node.childNodes(),sb);
					sb.append("</").append(node.nodeName()).append('>');
				}
			}
		}else{
			String html=node.outerHtml();
			if(!" ".equals(html)){
				sb.append(html);
			}
		}
	}
	private void process_for(String _for,StringBuilder sb,String forInner){
		String[]ss=_for.split(":");
		String[]sss=ss[0].split(" +");
		String _list=Integer.toHexString(random.nextInt());
		String _locali=Integer.toHexString(random.nextInt());
		String _list_=Integer.toHexString(random.nextInt());
		String _locali_=Integer.toHexString(random.nextInt());
		
		sb.append(seprator).append(codeflag)
		.append("Object _").append(_list).append('=');
		if(ss[1].contains(".")){
			sb.append(ss[1].replaceAll("\\.", ".get(\"")).append("\");");
		}else{
			sb.append("model.get(\"").append(ss[1].trim()).append("\");");
		}
		sb.append("switch(listType(_").append(_list)
		.append(")){case list:");
		//生成for循环
		sb.append("List list_").append(_list_).append("=(List)_").append(_list).append(';')
		.append("int _").append(_locali).append("_=list_").append(_list_).append(".size();")
		.append("for(int _").append(_locali_).append("=0;_")
		.append(_locali_).append("<_").append(_locali).append("_;_")
		.append(_locali_).append("++){")
		.append(ss[0]).append("=(").append(sss[0]).append(")list_").append(_list_).append(".get(_").append(_locali_).append(");")
		.append(forInner.replaceAll("\\$index", '_'+_locali_))
		.append(seprator).append(codeflag).append("}break;");
		
		sb.append("case array:");
		_list_=Integer.toHexString(random.nextInt());
		_locali_=Integer.toHexString(random.nextInt());
		sb.append("Object[] array_").append(_list_).append("=(Object[])_").append(_list).append(';')
		.append("for(int _").append(_locali_).append("=0;_")
		.append(_locali_).append("<array_").append(_list_).append(".length;_")
		.append(_locali_).append("++){")
		.append(ss[0]).append("=(").append(sss[0]).append(")array_").append(_list_).append("[_").append(_locali_).append("];")
		.append(forInner.replaceAll("\\$index", '_'+_locali_))
		.append(seprator).append(codeflag).append("}break;");
		
		sb.append("case set:");
		_list_=Integer.toHexString(random.nextInt());
		_locali_=Integer.toHexString(random.nextInt());
		sb.append("Set<").append(sss[0]).append("> set_").append(_list_)
		.append("=(Set").append('<').append(sss[0]).append(">)_").append(_list).append(';')
		.append("int _").append(_locali_).append("=0;")
		.append("for(").append(ss[0]).append(":set_").append(_list_).append("){")
		.append(forInner.replaceAll("\\$index", '_'+_locali_))
		.append(seprator).append(codeflag).append('_').append(_locali_).append("++;}break;");
		
		sb.append("default:");
		_list_=Integer.toHexString(random.nextInt());
		_locali_=Integer.toHexString(random.nextInt());
		sb.append("Object[] array_").append(_list_).append("=list2array(_").append(_list).append(");")
		.append("for(int _").append(_locali_).append("=0;_")
		.append(_locali_).append("<array_").append(_list_).append(".length;_")
		.append(_locali_).append("++){")
		.append(ss[0]).append("=(").append(sss[0]).append(")array_").append(_list_).append("[_").append(_locali_).append("];")
		.append(forInner.replaceAll("\\$index", '_'+_locali_))
		.append(seprator).append(codeflag).append("}break;}").append(seprator);
		
	}
	private void process_attrs(Node node,Attributes attrs,StringBuilder sb){
		if(attrs.size()>0){
//			sb.append('<').append(node.nodeName()).append(' ');
			sb.append('<').append(node.nodeName());
			
			Attributes attrsClone=attrs.clone();
			attrsClone.forEach((attr)->{
				String attrName=attr.getKey();
				if(attrName.startsWith("cc_")){
					attrName=attrName.substring(3, attrName.length());
					String bakAttrs=attrs.get(attrName);
					attrs.remove(attrName);
					
					String attrV=attr.getValue();
					Matcher matcher=ccpattern.matcher(attrV);
					//匹配{}()格式
					if(matcher.matches()){
						attrs.remove(attr.getKey());
						//[{},()]
						List<String>ls=split(attrV, ccpattern_left,2);
						Map<String, String>kvmap=jsonString2map(ls.get(0));
						if(!kvmap.isEmpty()){
							sb.append(" ").append(attrName).append("=\"");
							if(!"".equals(bakAttrs)){
								sb.append(bakAttrs).append(" ");
							}
							//是否使带<=>
							if(eqpattern.matcher(ls.get(1)).matches()){
								sb.append(seprator).append(codeflag)
								.append("if").append(proccessEQExpress(ls.get(1))).append('{').append(seprator);
								if(kvmap.containsKey("true")){
									String value=kvmap.get("true");
									//value为字符串直接输出值
									if(value.startsWith("'")){
										sb.append(value.substring(1, value.length()-1)).append('"');
									}else{
										sb.append(seprator).append(codeflag);
										if(value.contains(".")){
											sb.append("outprint(out,").append(toGetAttr(value)).append(");");
										}else{
											sb.append("outprint(out,").append(value).append(");");
										}
										sb.append(seprator)
										.append('"');
									}
								}
								if(kvmap.containsKey("false")){
									sb.append(seprator).append(codeflag)
									.append("}else{").append(seprator);
									String value=kvmap.get("false");
									//value为字符串直接输出值
									if(value.startsWith("'")){
										sb.append(value.substring(1, value.length()-1)).append('"');
									}else{
										sb.append(seprator).append(codeflag);
										if(value.contains(".")){
											sb.append("outprint(out,").append(toGetAttr(value)).append(");");
										}else{
											sb.append("outprint(out,").append(value).append(");");
										}
										sb.append(seprator)
										.append('"');
									}
								}
							}else{
								int i=0;
								for(Entry<String, String>entry:kvmap.entrySet()){
									String key=entry.getKey().replace('\'', '"');
									String value=entry.getValue();
									sb.append(seprator).append(codeflag);
									String eqexpress=proccessEQExpress(ls.get(1));
									if(i++==0){
										if(key.startsWith("\"")){
											sb.append("if(").append(key).append(".equals").append(eqexpress);
										}else{
											sb.append("if(").append(key).append("==").append(eqexpress.substring(1, eqexpress.length()-1));
										}
									}else{
										if(key.startsWith("\"")){
											sb.append("}else if(").append(key).append(".equals").append(eqexpress);
										}else{
											sb.append("}else if(").append(key).append("==").append(eqexpress.substring(1, eqexpress.length()-1));
										}
									}
									sb.append('{').append(seprator);
									//value为字符串直接输出值
									if(value.startsWith("'")){
										sb.append(value.substring(1, value.length()-1)).append('"');
									}else{
										sb.append(seprator).append(codeflag);
										if(value.contains(".")){
											sb.append("outprint(out,").append(toGetAttr(value)).append(");");
										}else{
											sb.append("outprint(out,").append(value).append(");");
										}
										sb.append(seprator)
										.append('"');
									}
								}
							}
							sb.append(seprator).append(codeflag)
							.append('}').append(seprator);
						}
					}
				}
			});
			sb.append(attrs).append('>');
		}else{
			sb.append('<').append(node.nodeName()).append('>');
		}
	}
	/**统一处理el表达式*/
	private StringBuilder proccess_el(StringBuilder html){
		StringBuilder result=new StringBuilder();
		Matcher matcher=elpattern.matcher(html);
		StringBuffer sbuf=new StringBuffer();
		while(matcher.find()){
			String a=matcher.group().replaceAll("\\$\\{|\\}", "").trim();
			if(a.contains(".")){
				a=toGetAttr(a);
			}else if(!a.startsWith("_")){
				a="model.get(\""+a+"\")";
			}
			sbuf.delete(0, sbuf.length());
			matcher.appendReplacement(sbuf, "");
			if(sbuf.length()!=0){
				result.append(sbuf);
			}
			result.append(seprator).append(codeflag)
			.append("outprint(out,").append(a).append(");")
			.append(seprator);
			
		}
		sbuf.delete(0, sbuf.length());
		matcher.appendTail(sbuf);
		if(sbuf.length()>0){
			result.append(sbuf);
		}
		return result;
	}
	private StringBuilder proccesEQ(String s){
		StringBuilder result=new StringBuilder();
		if(eqpattern.matcher(s).matches()){
			List<String>ls=split(s, cppattern);
			if(ls.get(0).startsWith("'")){
				if(ls.get(1).contains("!")){
					result.append('!');
				}
				result.append(ls.get(0).replace('\'', '"'));
				if(ls.get(2).startsWith("\'")){
					result.append(".equals(").append(ls.get(2).replace('\'', '"')).append(')');
				}else{
					if(ls.get(2).contains(".")&&!isNumeric(ls.get(2))){
						result.append(".equals(").append(ls.get(2).replaceAll("\\.", ".get(\"")).append("\"))");
					}else{
						result.append(".equals(").append(ls.get(2)).append(')');
					}
				}
			}else if(ls.get(2).startsWith("'")){
				if(ls.get(1).contains("!")){
					result.append('!');
				}
				result.append(ls.get(2).replace('\'', '"'));
				if(ls.get(0).contains(".")&&!isNumeric(ls.get(0))){
					result.append(".equals(").append(ls.get(0).replaceAll("\\.", ".get(\"")).append("\"))");
				}else{
					result.append(".equals(").append(ls.get(0)).append(')');
				}
			}else{
				if(ls.get(0).contains(".")&&!isNumeric(ls.get(0))){
					if(isNumeric(ls.get(2))){
						if(ls.get(2).contains(".")){
							result.append('(').append("double").append(')');
						}else{
							result.append('(').append("long").append(')');
						}
					}
					result.append(ls.get(0).replaceAll("\\.", ".get(\"")).append("\")");
				}else{
					result.append(ls.get(0));
				}
				result.append(ls.get(1));
				if(ls.get(2).contains(".")&&!isNumeric(ls.get(2))){
					if(isNumeric(ls.get(0))){
						if(ls.get(0).contains(".")){
							result.append('(').append("double").append(')');
						}else{
							result.append('(').append("long").append(')');
						}
					}
					result.append(ls.get(2).replaceAll("\\.", ".get(\"")).append("\")");
				}else{
					result.append(ls.get(2));
				}
			}
		}else{
			if(s.contains(".")&&!isNumeric(s)){
				result.append(s.replaceAll("\\.", ".get(\"")).append("\")");
			}else{
				result.append(s);
			}
		}
		return result;
	}
	private String proccessEQExpress(String s){
		StringBuilder result=new StringBuilder();
		Matcher matcher=cpmatcher.matcher(s);
		StringBuffer sbuf=new StringBuffer();
		while(matcher.find()){
			sbuf.delete(0, sbuf.length());
			//此方法右边替换参数有限制，在遇上特殊字符（$idnex%2==1）这种会报错
			matcher.appendReplacement(sbuf, "");
			if(sbuf.length()>0){
				result.append(sbuf);
			}
			//不得不使用一个sringBuilder来处理，原因如上
			result.append(proccesEQ(matcher.group()));
		}
		sbuf.delete(0, sbuf.length());
		matcher.appendTail(sbuf);
		if(sbuf.length()>0){
			result.append(sbuf);
		}
		return result.toString();
	}
	private void printJavaCode(StringBuilder sb){
		String s=sb.toString().replace(seprator, '\n').replace(codeflag, '~');
		System.out.println(s);
	}
	private String toGetAttr(String s){
		return s.replaceAll("\\.", ".get(\"")+"\")";
	}
	private void addField(Set<String>fieldNames,StringBuilder fileHead,int k){
		String fieldName="_"+Integer.toHexString(k);
		if(!fieldNames.contains(fieldName)){
			fileHead.append("private static final byte[] ")
			.append(fieldName)
			.append("=getResource(").append(k).append(");");
			fieldNames.add(fieldName);
		}
	}
	
	public static void main(String[] args) {
		Charset charset=Charset.forName("UTF-8");
		CCTEParser parser=new CCTEParser(charset);
		InputStream in=CCTEParser.class.getClassLoader().getResourceAsStream("templates/test.html");
		CCTEDocument cctedoc=new CCTEDocument(in, charset, "templates/test.html");
		Document doc=Jsoup.parse(cctedoc.toString());
		
		Set<String>fieldNames=new HashSet<>();
		StringBuilder fileHead=new StringBuilder();
		StringBuilder javaCode=parser.document2java(doc, fieldNames, fileHead);
		parser.printJavaCode(javaCode);
	}
}

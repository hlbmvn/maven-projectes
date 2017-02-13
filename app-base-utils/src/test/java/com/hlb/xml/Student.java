package com.hlb.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="stu",namespace="http://www.hlb.com")
public class Student {
	
	@XmlElement(name="name",required=true,namespace="http://www.hlb.com")
	private String sname ;
	private String sex;
	@XmlAttribute(name="no")
	private String sno;
	
	
	private List<YW> yws;
	
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public List<YW> getYws() {
		return yws;
	}
	public void setYws(List<YW> yws) {
		this.yws = yws;
	}
}

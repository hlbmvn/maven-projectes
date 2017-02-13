package com.hlb.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class YW {
	private String aa;
	private String bb;
	public String getAa() {
		return aa;
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	public String getBb() {
		return bb;
	}
	public void setBb(String bb) {
		this.bb = bb;
	}
}

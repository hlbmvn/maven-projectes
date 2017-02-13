package com.hlb.base.monitor.model;

import java.util.List;

public class CPUModel {
	private Double combined;//CPU总使用率 百分比
	private List<Double> cpus;//每一块的具体使用率
	
	public Double getCombined() {
		return combined;
	}
	public void setCombined(Double combined) {
		this.combined = combined;
	}
	public List<Double> getCpus() {
		return cpus;
	}
	public void setCpus(List<Double> cpus) {
		this.cpus = cpus;
	}
}

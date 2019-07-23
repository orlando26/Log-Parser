package com.orlando.parser.bean;

public class IpFound {
	
	private String ip;
	
	private Integer status;
	
	private Long count;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "IpFound [ip=" + ip + ", status=" + status + ", count=" + count + "]";
	}
	
}

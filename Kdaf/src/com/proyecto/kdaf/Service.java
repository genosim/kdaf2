package com.proyecto.kdaf;

public class Service {


	private String url;
	private String description;
	private String active;
	private String created_at;
	private String code;
	
	
	
	public Service() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Service(String url, String description, String active,
			String created_at, String code) {
		super();
		this.url = url;
		this.description = description;
		this.active = active;
		this.created_at = created_at;
		this.code = code;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getActive() {
		return active;
	}



	public void setActive(String active) {
		this.active = active;
	}



	public String getCreated_at() {
		return created_at;
	}



	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}

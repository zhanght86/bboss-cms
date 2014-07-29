package com.sany.application.entity;

import org.frameworkset.web.multipart.MultipartFile;

import com.frameworkset.orm.annotation.Column;

public class WfPic {
	private String id;   
	private String name;   
	@Column(type="blobfile")   
	private MultipartFile content;
	public WfPic(){
		
	} 
	
	public WfPic(String id,String name,MultipartFile content){
		this.id = id;
		this.name = name;
		this.content = content;
	} 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MultipartFile getContent() {
		return content;
	}
	public void setContent(MultipartFile content) {
		this.content = content;
	} 
	
}

package com.aieverywhere.backend.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "responses")
public class ResponsesModel {

	  @Id
	  @GeneratedValue(strategy=GenerationType.AUTO)
	  private int response_id;
	  @Column(name="post_id")
	  private int post_id;
	  @Column(name="user_id")
	  private int user_id;
	  @Column(name="content")
	  private String content;
	  @Column(name="sup_rep_id")
	  private int sup_rep_id;
	  @Column(name="created_at")
	  private Date created_at;
	  
	public ResponsesModel(int response_id, int post_id, int user_id, String content, int sup_rep_id, Date created_at) {
		super();
		this.response_id = response_id;
		this.post_id = post_id;
		this.user_id = user_id;
		this.content = content;
		this.sup_rep_id = sup_rep_id;
		this.created_at = created_at;
	}

	public int getResponse_id() {
		return response_id;
	}

	public void setResponse_id(int response_id) {
		this.response_id = response_id;
	}

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSup_rep_id() {
		return sup_rep_id;
	}

	public void setSup_rep_id(int sup_rep_id) {
		this.sup_rep_id = sup_rep_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}

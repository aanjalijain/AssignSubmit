package com.uxpsystems.assignment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_UserDetail")
public class UserDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="userName",nullable=false)
	private String userName;
	
	@Column(name="userPassword",nullable=false)
	private String userPassword;
	
	@Column(name="userStatus")
	private String userStatus;

	
	
	public UserDetail() {
		super();
	}

	public UserDetail(Long userId, String userName, String userPassword, String userStatus) {
		super();
		this.id = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userStatus = userStatus;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	@Override
	public String toString() {
		return "UserDetail [userId=" + id + ", userName=" + userName + ", userPassword=" + userPassword
				+ ", userStatus=" + userStatus + "]";
	}
	
	
}

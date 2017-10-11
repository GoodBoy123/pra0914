package yxy.pra0914.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


public class User implements Serializable{

	private int id;
	private String tel;
	private String nickname;
	private String gender;
	private String city;
	private String headimg;
	private String profession;
	private String company;
	private String job;
	private String personDes;
	private String ageDes;
	private Date regtime;

	// 用户保存的快递员
	private Set<User_express> contacts_express;
	
	// 用户保存的联系人
	private Set<User_user> contacts_user2;
	
	// 已保存该用户为联系人的用户
	private Set<User_user> contacts_user1;
	
	// 用户订单
	private Set<Order> orders;
	
	// 用户动态
	private Set<Development> developments;
	
	// 用户评论
	private Set<Comment> comments;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getPersonDes() {
		return personDes;
	}

	public void setPersonDes(String personDes) {
		this.personDes = personDes;
	}

	public String getAgeDes() {
		return ageDes;
	}

	public void setAgeDes(String ageDes) {
		this.ageDes = ageDes;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public Set<User_express> getContacts_express() {
		return contacts_express;
	}

	public void setContacts_express(Set<User_express> contacts_express) {
		this.contacts_express = contacts_express;
	}

	public Set<User_user> getContacts_user2() {
		return contacts_user2;
	}

	public void setContacts_user2(Set<User_user> contacts_user2) {
		this.contacts_user2 = contacts_user2;
	}

	public Set<User_user> getContacts_user1() {
		return contacts_user1;
	}

	public void setContacts_user1(Set<User_user> contacts_user1) {
		this.contacts_user1 = contacts_user1;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Set<Development> getDevelopments() {
		return developments;
	}

	public void setDevelopments(Set<Development> developments) {
		this.developments = developments;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}

package yxy.pra0914.dto;

import java.util.Date;
import java.util.Set;


public class Express {
	private int id;
	
	private String tel;
	private String nickname;
	private String realname;
	private String password;
	private String idcard;
	private String gender;
	private String headimg;
	private String cardimgone;
	private String cardimgtwo;
	private String profession;
	private String personDes;
	private Date regTime;
	private String company;
	private String job;
	private int status;
	// 保存该快递员为联系人的用户
	private Set<User_express> users;
	// 快递员参与的订单
	private Set<Order> orders;
	// 快递员动态
	private Set<Development> developments;
	// 快递员评论
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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getCardimgone() {
		return cardimgone;
	}

	public void setCardimgone(String cardimgone) {
		this.cardimgone = cardimgone;
	}

	public String getCardimgtwo() {
		return cardimgtwo;
	}

	public void setCardimgtwo(String cardimgtwo) {
		this.cardimgtwo = cardimgtwo;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getPersonDes() {
		return personDes;
	}

	public void setPersonDes(String personDes) {
		this.personDes = personDes;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set<User_express> getUsers() {
		return users;
	}

	public void setUsers(Set<User_express> users) {
		this.users = users;
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

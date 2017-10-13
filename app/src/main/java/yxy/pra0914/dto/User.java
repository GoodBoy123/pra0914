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


}

package de.skysail.server.osgi.um.users;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Pojo representing a user.
 * 
 */
@Entity
@Table(name = "um_users")
public class User implements Serializable {

	/** generated serial identifier */
	private static final long serialVersionUID = -471011284077959617L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column
	private String login;

	public String getLogin() {
		return login;
	}
	public void setLogin(String val) {
		this.login = val;
	}

	@Column
	private String firstname;

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String val) {
		this.firstname = val;
	}

	@Column
	private String lastname;

	public String getLastname() {
		return lastname;
	}
	public void setLastname(String val) {
		this.lastname = val;
	}

}

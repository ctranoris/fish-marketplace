package gr.upatras.ece.nam.baker.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;


@Entity(name = "BakerUser")
public class BakerUser {

	@Id
	@Basic()
	private int id = 0;


	@Basic()
	private String organization = null;

	@Basic()
	private String name = null;
	@Basic()
	private String username = null;
	@Basic()
	private String password = null;

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable()
	private List<BunMetadata> buns = new ArrayList<BunMetadata>();

	
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String newOrganization) {
		organization = newOrganization;
	}

	
	public List<BunMetadata> getBuns() {
		return buns;
	}

	public void addToBuns(BunMetadata bunsValue) {
		if (!buns.contains(bunsValue)) {
			buns.add(bunsValue);
		}
	}

	
	public void removeFromBuns(BunMetadata bunsValue) {
		if (buns.contains(bunsValue)) {
			buns.remove(bunsValue);
		}
	}

	
	public void clearBuns() {
		while (!buns.isEmpty()) {
			removeFromBuns(buns.iterator().next());
		}
	}


	public void setBuns(List<BunMetadata> newBuns) {
		buns = newBuns;
	}

	public int getId() {
		return id;
	}

	
	public void setId(int newId) {
		id = newId;
	}

	@Override
	public String toString() {
		return "BakerUser " + " [organization: " + getOrganization() + "]" + " [id: " + getId() + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

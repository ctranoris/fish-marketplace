package gr.upatras.ece.nam.baker.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;



@Entity(name = "Container")
public class Container {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id = 0;

	@Basic() 
	private String name;
	

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable()
	private List<DeployArtifact> deployArtifacts = new ArrayList<DeployArtifact>();

	

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



	public List<DeployArtifact> getDeployArtifacts() {
		return deployArtifacts;
	}


	public void setDeployArtifacts(List<DeployArtifact> deployArtifacts) {
		this.deployArtifacts = deployArtifacts;
	}
	
}

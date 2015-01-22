/**
 * Copyright 2014 University of Patras 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 */

package gr.upatras.ece.nam.baker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


/**
 * @author ctranoris
 *
 */
@Entity(name = "DeploymentDescriptor")
public class DeploymentDescriptor {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;
	
	@Basic()
	private String name = null;

	@Basic()
	private DeploymentDescriptorStatus status = DeploymentDescriptorStatus.INIT;
	
	

	@Basic()
	private Date dateCreated;



	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn() })
	private ApplicationMetadata baseApplication = null;
	
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn() })
	private BakerUser owner = null;
	
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinTable()
	private List<DeployContainer> deployContainers = new ArrayList<DeployContainer>();
	
	
	public List<DeployContainer> getDeployContainers() {
		return deployContainers;
	}

	public void setDeployContainers(List<DeployContainer> deployContainers) {
		this.deployContainers = deployContainers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public ApplicationMetadata getBaseApplication() {
		return baseApplication;
	}

	public void setBaseApplication(ApplicationMetadata baseApplication) {
		this.baseApplication = baseApplication;
	}

	public BakerUser getOwner() {
		return owner;
	}

	public void setOwner(BakerUser owner) {
		this.owner = owner;
	}


	public DeploymentDescriptorStatus getStatus() {
		return status;
	}

	public void setStatus(DeploymentDescriptorStatus status) {
		this.status = status;
	}

}

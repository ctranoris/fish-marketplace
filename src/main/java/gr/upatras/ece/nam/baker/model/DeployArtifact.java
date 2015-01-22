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
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;


@Entity(name = "DeployArtifact")
public class DeployArtifact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id = 0;
	


	@Basic() 
	private String uuid = null;

	@Basic() 
	private String name = null;

	@Basic() 
	private String artifactURL = null;

	@Basic() 
	private String artifactPackageURL = null;
	
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable()
	private List<ProductExtensionItem> extensions = new ArrayList<ProductExtensionItem>();
	

	@Basic() 
	private InstalledBunStatus status = InstalledBunStatus.INIT;

	public InstalledBunStatus getStatus() {
		return status;
	}

	public void setStatus(InstalledBunStatus status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtifactURL() {
		return artifactURL;
	}

	public void setArtifactURL(String artifactURL) {
		this.artifactURL = artifactURL;
	}

	public String getArtifactPackageURL() {
		return artifactPackageURL;
	}

	public void setArtifactPackageURL(String artifactPackageURL) {
		this.artifactPackageURL = artifactPackageURL;
	}

	public List<ProductExtensionItem> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<ProductExtensionItem> extensions) {
		this.extensions = extensions;
	}
	
	public void addExtensionItem(ProductExtensionItem i){
		if (!this.extensions.contains(i)){
			if (findProductExtensionItemByName(i.getName())==null )
				this.extensions.add(i);
		}
	}
	
	public void removeExtensionItem(ProductExtensionItem i){
		if (this.extensions.contains(i)){
			this.extensions.remove(i);
		}
	}
	
	public void addExtensionItem(String name, String value){
		ProductExtensionItem i = new ProductExtensionItem();
		i.setName(name);
		i.setValue(value);
		this.addExtensionItem(i);
	}
	
	public ProductExtensionItem findProductExtensionItemByName(String name){
		for (ProductExtensionItem p : this.extensions) {
			if (p.getName().equals(name))
				return p;
		}
		return null;
	}
	
	
}

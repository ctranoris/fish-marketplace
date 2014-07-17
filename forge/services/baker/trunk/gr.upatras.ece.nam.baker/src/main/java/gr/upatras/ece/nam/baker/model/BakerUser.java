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
import java.util.Iterator;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnoreType;


@Entity(name = "BakerUser")
@JsonIgnoreProperties(value = { "buns" })
public class BakerUser {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY  )
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

	public void addBun(BunMetadata bunsValue) {
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

	public BunMetadata getBunById(int bunid) {

		for (Iterator iterator = buns.iterator(); iterator.hasNext();) {
			BunMetadata bunMetadata = (BunMetadata) iterator.next();
			if (bunMetadata.getId() == bunid)
				return bunMetadata;
		}
		return null;
	}
}

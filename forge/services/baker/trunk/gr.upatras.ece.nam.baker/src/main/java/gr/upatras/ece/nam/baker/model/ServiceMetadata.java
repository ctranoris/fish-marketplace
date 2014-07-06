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

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity(name = "ServiceMetadata")
public class ServiceMetadata {


	@Id
	@Basic()
	private UUID uuid;	
	@Basic()
	private String name;
	@Basic()
	private String provider;
	private String iconsrc;
	@Basic()
	private String shortDescription;
	private String longDescription;
	@Basic()
	private String version;
	@Basic()
	private String packageLocation;


	public ServiceMetadata() {
	}
	
	public ServiceMetadata(UUID uuid, String name) {
		super();
		this.name = name;
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getIconsrc() {
		return iconsrc;
	}
	public void setIconsrc(String iconsrc) {
		this.iconsrc = iconsrc;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPackageLocation() {
		return packageLocation;
	}
	public void setPackageLocation(String packageLocation) {
		this.packageLocation = packageLocation;
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}


}

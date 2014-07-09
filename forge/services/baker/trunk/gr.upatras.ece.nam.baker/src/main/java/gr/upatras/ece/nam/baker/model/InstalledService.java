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
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.apache.openjpa.persistence.Externalizer;
import org.apache.openjpa.persistence.Factory;
import org.apache.openjpa.persistence.Type;


@Entity(name = "InstalledService")
public class InstalledService {
	


//	@GeneratedValue( strategy = GenerationType.IDENTITY  )
//	private long id = 0;
	

	@Id
	private String uuid;
	

	@Basic()
	private String repoUrl;
	@Basic()
	private String installedVersion;
	
	@Basic()
	private String name;

	@Basic()
	private InstalledServiceStatus status = InstalledServiceStatus.INIT;
	

	@Basic()
	private String packageLocalPath;
	
	/**
	 * this exist only after fetching metadata from repo. Otherwise is null when service is installed.
	 */
	@Embedded
	private ServiceMetadata serviceMetadata; 
	
	
	public InstalledService() {
		super();
	}
	
	public InstalledService(String uuid, String repoUrl) {
		super();
		this.uuid = uuid;
		this.repoUrl = repoUrl;
		this.name = "(pending)";
	}
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getRepoUrl() {
		return repoUrl;
	}
	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}
	public String getInstalledVersion() {
		return installedVersion;
	}
	public void setInstalledVersion(String installedVersion) {
		this.installedVersion = installedVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InstalledServiceStatus getStatus() {
		return status;
	}

	public void setStatus(InstalledServiceStatus status) {
		this.status = status;
	}

	public ServiceMetadata getServiceMetadata() {
		return serviceMetadata;
	}

	public void setServiceMetadata(ServiceMetadata sm) {
		this.serviceMetadata = sm;
	}

	public String getPackageLocalPath() {
		return packageLocalPath;
	}

	public void setPackageLocalPath(String packageLocalPath) {
		this.packageLocalPath = packageLocalPath;
	}

	

}

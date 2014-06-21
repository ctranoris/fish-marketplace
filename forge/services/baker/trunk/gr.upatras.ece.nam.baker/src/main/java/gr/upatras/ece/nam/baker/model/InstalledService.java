package gr.upatras.ece.nam.baker.model;

import java.util.UUID;

public class InstalledService {
	
	private UUID uuid;
	private String repoUrl;
	private String installedVersion;
	private String name;
	private InstalledServiceStatus status = InstalledServiceStatus.INIT;
	
	
	public InstalledService() {
		super();
	}
	
	public InstalledService(UUID uuid, String repoUrl, String installedVersion) {
		super();
		this.uuid = uuid;
		this.repoUrl = repoUrl;
		this.installedVersion = installedVersion;
		this.name = "(pending)";
	}
	
	
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
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

	

}

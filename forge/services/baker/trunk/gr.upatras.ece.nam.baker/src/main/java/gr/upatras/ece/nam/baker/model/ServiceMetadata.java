package gr.upatras.ece.nam.baker.model;

import java.util.UUID;

public class ServiceMetadata {

	private UUID uuid;	
	private String name;
	private String provider;
	private String iconsrc;
	private String shortDescription;
	private String longDescription;
	private String version;
	private String packageLocation;


	
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

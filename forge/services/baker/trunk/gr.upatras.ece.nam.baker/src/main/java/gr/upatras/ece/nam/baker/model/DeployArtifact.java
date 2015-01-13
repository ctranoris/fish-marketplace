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

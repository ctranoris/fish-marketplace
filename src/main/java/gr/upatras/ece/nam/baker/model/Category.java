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
import javax.persistence.ManyToMany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity(name = "Category")
@JsonIgnoreProperties(value = {  "products" }, ignoreUnknown = true )
public class Category {

	private static final transient Log logger = LogFactory.getLog(Category.class.getName());
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;
	
	@Basic()
	private String name=null;



	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private List<Product> products = new ArrayList<Product>();
	
	
	
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public void addProduct(Product product) {
		if (!products.contains(product)) {
			logger.info("ACTUALLY add into category "+ this.getName()+"  product "+product.getId());
			products.add(product);
			//product.addCategory(this);
		}		
	}
	

	public void removeProduct(Product p) {
		if (products.contains(p)) {
			products.remove(p);
			//p.removeCategory(this);
		}		
	}
	
	public int getAppscount() {
		int c = 0;
		for (int i = 0; i < this.products.size(); i++) {
			if (this.products.get(i) instanceof ApplicationMetadata)
				c++;
		}
		return c;
	}
	
	public int getBunscount() {
		int c = 0;
		for (int i = 0; i < this.products.size(); i++) {
			if (this.products.get(i) instanceof BunMetadata)
				c++;
		}
		return c;
	}
	
	
	
	public int getProductsCount() {
		return products.size();
	}
	

}

package de.terrestris.shogun.model;

import java.util.Date;

/**
 * Interface for a base model of a database entity
 */
public interface BaseModelInterface {
	
	public int getId();
	public void setId(int id);
	
	public Date getUpdated_at();
	public void setUpdated_at(Date updated_at);
	
	public Date getCreated_at();
	public void setCreated_at(Date created_at);
	
	public String getApp_user();
	public void setApp_user(String user);
	
	public String info();

}

package org.ongawa.peru.chlorination.persistence.elements;

public class Community {
	
	/**
	 * @author Kiko
	 */
	
	private int communityId;
	private String name;
	private SubBasin subBasin;
	
	public Community(int communityId, String name, SubBasin subBasin) {
		super();
		this.communityId = communityId;
		this.name = name;
		this.subBasin = subBasin;
	}
	
	public Community(String name, SubBasin subBasin) {
		super();
		this.communityId = -1;
		this.name = name;
		this.subBasin = subBasin;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public SubBasin getSubBasin() {
		return subBasin;
	}
	
	public void setSubBasin(SubBasin subBasin) {
		this.subBasin = subBasin;
	}
	
	public int getCommunityId() {
		return communityId;
	}
}

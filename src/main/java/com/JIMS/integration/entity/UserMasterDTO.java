package com.JIMS.integration.entity;

public class UserMasterDTO {
	  private String userId;
	    private String username;
	    private int id;

	    public UserMasterDTO() {}

	    public UserMasterDTO(int id,String userId, String username) {
	        this.id = id;
	    	this.userId = userId;
	        this.username = username;
	    }

	    public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }
}

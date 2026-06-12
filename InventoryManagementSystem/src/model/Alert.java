package model;

public class Alert {
	private int AlertID;
	private String AlertMessage;
	
	public Alert() {}
	public Alert(int AlertID,String AlertMessage) {
		this.AlertID=AlertID;
		this.AlertMessage=AlertMessage;
		
	}
	public int getAlertID() {
		return AlertID;
		
	}
	public void setAlertID(int AlertID) {
		this.AlertID=AlertID;
		
	}
	public String getAlertMessage() {
		return AlertMessage;
		
	}
	public void setAlertMessage(String AlertMessage) {
		this.AlertMessage=AlertMessage;
		
	}
}

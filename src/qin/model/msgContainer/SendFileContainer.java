package qin.model.msgContainer;

import java.io.Serializable;

public class SendFileContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	private int sourceID;
	private int destinationID;
	
	private String fileName = null;
	private long totalSize ;
	
	public SendFileContainer(int s, int d, String file, long size) {
		sourceID = s;
		destinationID = d;
		fileName = file;
		totalSize = size;
	}
	
	public void setSourceID(int id) {
		sourceID = id;
	}
	
	public int getSourceID() {
		return sourceID;
	}
	
	public void setDestinationID(int d) {
		destinationID = d;
	}
	
	public int getDestinationID() {
		return destinationID;
	}
	
	public void setFileName(String name) {
		fileName = name;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setTotalSize(long size) {
		totalSize = size;
	}
	
	public long getTotalSize() {
		return totalSize;
	}
}

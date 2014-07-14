package com.bajicdusko.ajp.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CacheList<T> extends ArrayList<T> implements Serializable{
	private static final long serialVersionUID = 1L;

	public void setClearedList(ArrayList<T> items) {
		this.clear();
		this.addAll(items);
	}
	
	public void setList(T[] items) {		
		
		for (T t : items) {
			this.add(t);
		}		
	}
	
	public void setClearedList(T[] items) {
		this.clear();
		
		for (T t : items) {
			this.add(t);
		}
		
	}

	public List<T> getValues(){
		return this;
	}
}

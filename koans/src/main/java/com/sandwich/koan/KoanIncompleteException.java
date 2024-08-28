package com.sandwich.koan;

import org.testng.SkipException;

public class KoanIncompleteException extends SkipException {

	private static final long serialVersionUID = -4207677767865451508L;

	public KoanIncompleteException(String message){
		super(message);
	}
	
	@Override public String toString(){
		return super.getMessage();
	}
	
}

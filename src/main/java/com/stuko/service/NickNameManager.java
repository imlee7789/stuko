package com.stuko.service;

import java.util.ArrayList;
import java.util.List;

public class NickNameManager {
	private static NickNameManager instance = new NickNameManager();
	
	private List<String> nickNames = new ArrayList<>();
	
	private NickNameManager() {
	}
	
	public static NickNameManager getInstance() {
		return instance;
	}
	
}

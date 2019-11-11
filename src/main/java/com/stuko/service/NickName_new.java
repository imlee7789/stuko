package com.stuko.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NickName_new {

	public static List<String> animalsName = new ArrayList<String>();

	public static String randomNick() throws Exception {

		String filepath = "C:\\app\\spring\\sts-4.3.2.RELEASE\\workspace\\chat01\\animals.txt";

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
		String animal = "";

		while ((animal = in.readLine()) != null) {
			animalsName.add(animal);
		}

		in.close();

		System.out.println(animalsName);

		int index = (int) (Math.random() * animalsName.size());
		String result = animalsName.get(index);

		animalsName.remove(result);
		System.out.println(animalsName);

		return result;

	}

	public static void putNickName(String randomNickName) {
		animalsName.add(randomNickName);
	}

	public static String changeNickName(String oldNickName) {
		animalsName.add(oldNickName);

		int index = (int) (Math.random() * animalsName.size() - 1);
		String newNickName = animalsName.get(index);

		// changeNickName = result;
		animalsName.remove(newNickName);

		return newNickName;
	}
}
package com.stuko.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NickName {

	// FileReader로 변경해야한다.
	public static String[] arr = {
			"토끼", 
			"사자", 
			"강아지", 
			"고양이", 
			"기린", 
			"돌고래", 
			"알파카", 
			"곰", 
			"원숭이", 
			"닭",
			"거북",
			"거위",
			"게",
			"고릴라",
			"고슴도치",
			"고양이",
			"곰",
			"공작",
			"귀뚜라미",
			"기러기",
			"기린",
			"까마귀",
			"까치",
			"꾀꼬리",
			"꿀벌",
			"꿩",
			"나방",
			"나비",
			"낙타",
			"너구리"
			};
	
	
	
	public static List<String> nickNames = new ArrayList<String>(Arrays.asList(arr));
	
	public static int getSize() {
		log.info("NickName::getSize() invoked.");
		
		System.out.println(nickNames.toString());
		
		return nickNames.size();
	}
	
	public static String randomNick() {
		log.info("NickName::randomNick() invoked.");

		int index = (int) (Math.random()*nickNames.size());	
		String result = nickNames.get(index);

		nickNames.remove(result);
		NickName.getSize();
		return result;		

	}
	
	public static void putNickName(String nickName) {
		log.info("NickName::putNickName() invoked.");
		nickNames.add(nickName);
		NickName.getSize();
	}
	
	public static String changeNickName(String oldNickName) {
		log.info("NickName::changeNickName() invoked.");
		nickNames.add(oldNickName);		
		
		int index = (int) (Math.random()*nickNames.size()-1);
		String newNickName = nickNames.get(index);
		
		//changeNickName = result;
		nickNames.remove(newNickName);
		NickName.getSize();
		return newNickName;
	}
	
}

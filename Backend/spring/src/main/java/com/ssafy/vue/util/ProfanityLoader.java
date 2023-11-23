package com.ssafy.vue.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.TreeSet;

public class ProfanityLoader {
    private static ProfanityLoader instance = null;
    private static String[] profanityFilePath = {"resources/profanity.csv"};
	
    private ProfanityLoader() {}
    
    public static ProfanityLoader getInstance() {
    	if(instance == null) {
    		instance = new ProfanityLoader();
    	}
    	
    	return instance;
    }

    //
    public TreeSet<String> loadProfanityList() {
    	/**
    	 * System.getProperty("user.dir"): C:\SSAFY\eclipse
    	 * 현재 경로: C:\SSAFY\eclipse
    	 * 
    	 * 서버와 소스코드 경로 다름 유의
    	 */
    	System.out.println("System.getProperty(\"user.dir\"): " + System.getProperty("user.dir"));
        System.out.println("현재 경로: " + new File("").getAbsolutePath());
    	
		String line;
		TreeSet<String> profanityList = new TreeSet<>();
		
		for(String filePath : profanityFilePath) {
			try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {
	            while((line = br.readLine()) != null) {
	                profanityList.add(line);
	            }
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
		}
		
		return profanityList;
    }
    
    /*
    public static void main(String[] args) {
    	loadProfanityList();
	}
	*/
    
    /*
    public static void main(String[] args) {
    	String line;
    	String profanityFilePath = "resources/profanity.csv";
    	
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(profanityFilePath)))) {
			while((line = br.readLine()) != null) {
				System.out.println("line: " + line);
            }
    	} catch(Exception e) {
    		e.printStackTrace();
		}
	}
	*/
}
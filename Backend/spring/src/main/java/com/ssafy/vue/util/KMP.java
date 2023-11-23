package com.ssafy.vue.util;

import java.util.HashMap;
import java.util.TreeSet;

public class KMP {
	private TreeSet<String> profanitySet = null;
	private HashMap<String, int[]> map = new HashMap<>();
	ProfanityLoader profanityLoader = ProfanityLoader.getInstance();
	private static KMP instance = null;
	
	private KMP() {
		//profanitySet = profanityLoader.loadProfanityList();	//파일에서 욕설 리스트 로드 -> 서버와 프로젝트 폴더 경로 다름 유의
		initSet();												//욕설 리스트 HashSet 추가
		makePiTable();											//욕설 리스트로 pi 테이블 생성
	}
	
	private void initSet() {
		profanitySet = new TreeSet<>();
		
		profanitySet.add("개새끼");
		profanitySet.add("씨발");
		profanitySet.add("미친");
		profanitySet.add("닥쳐");
		profanitySet.add("꺼져");
		profanitySet.add("병신");
		profanitySet.add("지랄");
		profanitySet.add("존나");
		profanitySet.add("염병");
		profanitySet.add("제기랄");
		profanitySet.add("개고생");
	}
	
	public static KMP getInstance() {
		if(instance == null) {
			instance = new KMP();
		}
		
		return instance;
	}
	
	public void makePiTable() {
		for(String P_string : profanitySet) {
			char[] P = P_string.toCharArray();			//문자열 P (전체에서 찾으려는 문자열 패턴)
			int[] pi = new int[P.length];				//부분일치 테이블 배열
			
			//부분일치 테이블 배열 만들기
			int j=0;
			for(int i=1; i<P.length;) {					//전체 문자열의 길이만큼 반복 (i == 0 to P.length 테이블 생성)
				if(P[i] == P[j]) {
					pi[i] = j + 1;
					i++;
					j++;
				} else if(j>0) {						//접두사 포인터 j 위치에서 불일치 발생 -> 직전 (j-1)위치까지는 일치 -> pi[j-1] = k을 이용하여 j=k
					j = pi[j-1];
				} else {								//더 이상 pi 테이블을 당겨와 검사할 수 없으면 i값 증가
					i++;
				}
			}
			
			map.put(P_string, pi);
		}
	}
	
	public int purifyString(StringBuilder originalText) {
		//Null 체크
		if(map == null) {
			System.err.println("Empty KMP Map error");
			
			throw new NullPointerException();
		}
		
		int count = 0;	//순화된 글자의 수
		
		for(String P : map.keySet()) {
			int[] pi = map.get(P);
			int j = 0;
			
			for(int i=0;;) {
				if(originalText.charAt(i) == P.charAt(j)) {
					i++;
					j++;
					
					if(j == P.length()) {
						originalText.replace(i - P.length(), i, "*");
						j = pi[j-1];
						count++;						//순화된 글자의 수 증가
					}
				} else if(j>0) {						//접두사 포인터 j 위치에서 불일치 발생 -> 직전 (j-1)위치까지는 일치 -> pi[j-1] = k을 이용하여 j=k
					j = pi[j-1];
				} else {								//더 이상 pi 테이블을 당겨와 검사할 수 없으면 i값 증가
					i++;
				}
				
				if(i >= originalText.length()) {
					break;
				}
			}
		}
		
		return count;
	}
}

package com.ssafy.vue.map.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(value = "AttractionInfoDto : 시도, 구군정보", description = "시도, 구군의 이름을 나타낸다.")
public class AttractionInfoDto {

	@ApiModelProperty(value = "관광지 고유번호")
	private int contentId;
	@ApiModelProperty(value = "관광지 종류")
	//12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점
	private int contentTypeId;
	@ApiModelProperty(value = "관광지명")
	private String title;
	@ApiModelProperty(value = "주소 1")
	private String addr1;
	@ApiModelProperty(value = "주소 2")
	private String addr2;
	@ApiModelProperty(value = "우편번호")
	private String zipcode;
	@ApiModelProperty(value = "전화번호")
	private String tel;
	@ApiModelProperty(value = "관광지 썸네일 1")
	private String firstImage;
	@ApiModelProperty(value = "관광지 썸네일 2")
	private String firstImage2;
	@ApiModelProperty(value = "조회수")
	private int readcount;
	@ApiModelProperty(value = "시/도 코드")
	//1:서울, 3:대전, 5:광주, 6:부산, 35:경상북도 등
	private int sidoCode;
	@ApiModelProperty(value = "구/군 코드")
	private int gugunCode;
	@ApiModelProperty(value = "위도")
	private double latitude;
	@ApiModelProperty(value = "경도")
	private double longitude;
	@ApiModelProperty(value = "모르겠음")
	private String mlevel;


}

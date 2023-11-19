package com.ssafy.vue.map.model;

import org.springframework.stereotype.Component;

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
@Component
@ApiModel(value = "GugunDtoFromSidoTable : 'gugun테이블'의 구/군 정보", description = "'gugun테이블'의 구/군 정보")
public class GugunDtoFromSidoTable {
	@ApiModelProperty(value = "구/군코드")
	int gugunCode;
	@ApiModelProperty(value = "구군이름")
	String gugunName;
	@ApiModelProperty(value = "시/도코드")
	int sidoCode;
}

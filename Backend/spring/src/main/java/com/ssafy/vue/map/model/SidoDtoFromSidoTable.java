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
@ApiModel(value = "SidoDtoFromSidoTable : 'sido테이블'의 시/도 정보", description = "'sido테이블'의 시/도 정보")
public class SidoDtoFromSidoTable {
	@ApiModelProperty(value = "시/도코드")
	private int sidoCode;
	@ApiModelProperty(value = "시도이름")
	private String sidoName;
	
}

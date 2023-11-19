package com.ssafy.vue.map.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.vue.map.model.AttractionInfoDto;
import com.ssafy.vue.map.model.GugunDtoFromSidoTable;
import com.ssafy.vue.map.model.SidoDtoFromSidoTable;
import com.ssafy.vue.map.model.SidoGugunCodeDto;

public interface MapService {

	List<SidoGugunCodeDto> getSido() throws Exception;
	List<SidoGugunCodeDto> getGugunInSido(String sido) throws Exception;
	List<AttractionInfoDto> getAttraction(Map<String, Integer> map) throws SQLException;
	List<SidoDtoFromSidoTable> getSidoFromSidoTable() throws Exception;
	List<GugunDtoFromSidoTable> getGugunFromGugunTable(int sido) throws Exception;
}

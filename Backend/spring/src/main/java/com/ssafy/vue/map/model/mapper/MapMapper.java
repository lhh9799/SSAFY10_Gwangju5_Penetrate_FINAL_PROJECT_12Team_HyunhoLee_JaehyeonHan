package com.ssafy.vue.map.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.vue.map.model.AttractionInfoDto;
import com.ssafy.vue.map.model.GugunDtoFromSidoTable;
import com.ssafy.vue.map.model.SidoDtoFromSidoTable;
import com.ssafy.vue.map.model.SidoGugunCodeDto;

public interface MapMapper {

	List<SidoGugunCodeDto> getSido() throws SQLException;
	List<SidoGugunCodeDto> getGugunInSido(String sido) throws SQLException;
	List<AttractionInfoDto> getAttraction(Map<String, Integer> map) throws SQLException;
	List<SidoDtoFromSidoTable> getSidoFromSidoTable() throws SQLException;
	List<GugunDtoFromSidoTable> getGugunFromGugunTable(int sido) throws SQLException;
}

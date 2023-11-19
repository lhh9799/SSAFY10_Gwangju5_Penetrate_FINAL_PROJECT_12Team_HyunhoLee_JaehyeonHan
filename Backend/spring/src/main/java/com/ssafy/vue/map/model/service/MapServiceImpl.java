package com.ssafy.vue.map.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.vue.map.model.AttractionInfoDto;
import com.ssafy.vue.map.model.GugunDtoFromSidoTable;
import com.ssafy.vue.map.model.SidoDtoFromSidoTable;
import com.ssafy.vue.map.model.SidoGugunCodeDto;
import com.ssafy.vue.map.model.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService {
	
	private MapMapper mapMapper;

	public MapServiceImpl(MapMapper mapMapper) {
		super();
		this.mapMapper = mapMapper;
	}

	@Override
	public List<SidoGugunCodeDto> getSido() throws Exception {
		return mapMapper.getSido();
	}

	@Override
	public List<SidoGugunCodeDto> getGugunInSido(String sido) throws Exception {
		return mapMapper.getGugunInSido(sido);
	}

	@Override
	public List<AttractionInfoDto> getAttraction(Map<String, Integer> map) throws SQLException {
		return mapMapper.getAttraction(map);
	}

	@Override
	public List<SidoDtoFromSidoTable> getSidoFromSidoTable() throws Exception {
		return mapMapper.getSidoFromSidoTable();
	}

	@Override
	public List<GugunDtoFromSidoTable> getGugunFromGugunTable(int sido) throws Exception {
		return mapMapper.getGugunFromGugunTable(sido);
	}

}

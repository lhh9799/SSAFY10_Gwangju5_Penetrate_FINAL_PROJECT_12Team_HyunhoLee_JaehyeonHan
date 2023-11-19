package com.ssafy.vue.map.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.vue.map.model.AttractionInfoDto;
import com.ssafy.vue.map.model.GugunDtoFromSidoTable;
import com.ssafy.vue.map.model.SidoDtoFromSidoTable;
import com.ssafy.vue.map.model.SidoGugunCodeDto;
import com.ssafy.vue.map.model.service.MapService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/map")
@Api("Map 컨트롤러  API V1")
@Slf4j
public class MapController {
	
	private MapService mapService;

	public MapController(MapService mapService) {
		super();
		this.mapService = mapService;
	}

	@ApiOperation(value = "시도 정보", notes = "전국의 시도를 반환한다.", response = List.class)
	@GetMapping("/sido")
	public ResponseEntity<List<SidoGugunCodeDto>> sido() throws Exception {
		log.info("sido - 호출");
		return new ResponseEntity<List<SidoGugunCodeDto>>(mapService.getSido(), HttpStatus.OK);
	}

	@ApiOperation(value = "구군 정보", notes = "시도에 속한 구군을 반환한다.", response = List.class)
	@GetMapping("/gugun")
	public ResponseEntity<List<SidoGugunCodeDto>> gugun(
			@RequestParam("sido") @ApiParam(value = "시도코드.", required = true) String sido) throws Exception {
		log.info("gugun - 호출");
		return new ResponseEntity<List<SidoGugunCodeDto>>(mapService.getGugunInSido(sido), HttpStatus.OK);
	}
	
	@ApiOperation(value = "관광지 정보", notes = "관광지 유형(예: 여행코스, 레포츠 등)에 맞는 관광지 정보를 반환한다.", response = List.class)
	@GetMapping("/attraction")
	public ResponseEntity<List<AttractionInfoDto>> attraction(
//			@RequestParam @ApiParam(value = "관광지 코드(예: 여행코스, 레포츠 등)", required = true) Map<String, Integer> map) throws Exception {
			@RequestParam @ApiParam(value = "관광지 코드(예: 여행코스, 레포츠 등)", required = true) Map<String, String> map) throws Exception {
//			@RequestParam @ApiParam(value = "관광지 코드(예: 여행코스, 레포츠 등)", required = true) Map<?, ?> map) throws Exception {
		log.info("attraction - 호출");
		Map<String, Integer> newMap = new HashMap<>();
		for(String key : map.keySet()) {
			newMap.put(key, Integer.parseInt(map.get(key)));
		}
		
		System.out.println("mapService.getAttraction(newMap): " + mapService.getAttraction(newMap));
		return new ResponseEntity<List<AttractionInfoDto>>(mapService.getAttraction(newMap), HttpStatus.OK);
//		return new ResponseEntity<List<AttractionInfoDto>>(mapService.getAttraction((Map<String, Integer>) map), HttpStatus.OK);
	}
	
	@ApiOperation(value = "'sido테이블'의 시/도 정보", notes = "'sido테이블'의 시/도 정보를 반환한다.", response = List.class)
	@GetMapping("/sidoDtoFromSidoTable")
	public ResponseEntity<List<SidoDtoFromSidoTable>> sidoDtoFromSidoTable() throws Exception {
		log.info("sidoDtoFromSidoTable - 호출");
		System.out.println("mapService.getSidoFromSidoTable(): " + mapService.getSidoFromSidoTable());
		return new ResponseEntity<List<SidoDtoFromSidoTable>>(mapService.getSidoFromSidoTable(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "'gugun테이블'의 구/군 정보", notes = "'gugun테이블'의 구/군 정보를 반환한다.", response = List.class)
	@GetMapping("/gugunDtoFromSidoTable")
	public ResponseEntity<List<GugunDtoFromSidoTable>> gugunDtoFromSidoTable(
			@RequestParam("sido") @ApiParam(value = "시도코드.", required = true) int sido) throws Exception {
		log.info("gugunDtoFromSidoTable - 호출");
		System.out.println("mapService.getGugunFromGugunTable(sido): " + mapService.getGugunFromGugunTable(sido));
		return new ResponseEntity<List<GugunDtoFromSidoTable>>(mapService.getGugunFromGugunTable(sido), HttpStatus.OK);
	}
}

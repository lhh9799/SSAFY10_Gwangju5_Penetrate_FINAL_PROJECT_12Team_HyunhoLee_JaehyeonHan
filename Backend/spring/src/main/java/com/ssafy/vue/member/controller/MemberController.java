package com.ssafy.vue.member.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.vue.board.model.BoardDto;
import com.ssafy.vue.member.model.IdAndPwdDto;
import com.ssafy.vue.member.model.ItineraryDto;
import com.ssafy.vue.member.model.MemberDto;
import com.ssafy.vue.member.model.NewPwdDto;
import com.ssafy.vue.member.model.service.MemberService;
import com.ssafy.vue.util.JWTUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class MemberController {

	private MemberService memberService;
	private JWTUtil jwtUtil;

	public MemberController(MemberService memberService, JWTUtil jwtUtil) {
		super();
		this.memberService = memberService;
		this.jwtUtil = jwtUtil;
	}

	@ApiOperation(value = "로그인", notes = "아이디와 비밀번호를 이용하여 로그인 처리.")
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto) {
		log.debug("login user : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			MemberDto loginUser = memberService.login(memberDto);
			if (loginUser != null) {
				String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
				String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
				log.debug("access token : {}", accessToken);
				log.debug("refresh token : {}", refreshToken);

//				발급받은 refresh token을 DB에 저장.
				memberService.saveRefreshToken(loginUser.getUserId(), refreshToken);

//				JSON으로 token 전달.
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);

				status = HttpStatus.CREATED;
			} else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해주세요.");
				status = HttpStatus.UNAUTHORIZED;
			}

		} catch (Exception e) {
			log.debug("로그인 에러 발생 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
	@GetMapping("/info/{userId}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userId") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) {
//		logger.debug("userId : {} ", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
			log.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				MemberDto memberDto = memberService.userInfo(userId);
				resultMap.put("userInfo", memberDto);
				status = HttpStatus.OK;
			} catch (Exception e) {
				log.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			log.error("사용 불가능 토큰!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
	@GetMapping("/logout/{userId}")
	public ResponseEntity<?> removeToken(
			@PathVariable("userId") @ApiParam(value = "로그아웃할 회원의 아이디.", required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberService.deleRefreshToken(userId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);

	}

	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody MemberDto memberDto, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refreshToken");
		log.debug("token : {}, memberDto : {}", token, memberDto);
		if (jwtUtil.checkToken(token)) {
			if (token.equals(memberService.getRefreshToken(memberDto.getUserId()))) {
				String accessToken = jwtUtil.createAccessToken(memberDto.getUserId());
				log.debug("token : {}", accessToken);
				log.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			}
		} else {
			log.debug("리프레쉬토큰도 사용불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원가입", notes = "전달받은 아이디와 비밀번호로 회원가입 처리.")
	@PostMapping("/join")
	public ResponseEntity<?> join(
			@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto) {
		log.debug("join user : {}", memberDto);
		System.out.println("전달받은 memberDto: " + memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberService.join(memberDto);
			//이현호 실험
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			log.debug("회원가입 에러 발생 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			//이현호 실험
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
	}
	
	// 한재현 추가
	@ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴시킨다.", response = Map.class)
	@GetMapping("/delete/{userId}")
	public ResponseEntity<?> DeleteUser(@PathVariable ("userId") @ApiParam(value = "로그아웃할 회원의 아이디.", required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberService.delete(userId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("회원탈퇴 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);

	}
	
	// 한재현 추가
	@ApiOperation(value = "비밀번호를 검증한다.", notes = "정보 수정 전 비밀번호 검증", response = Map.class)
	@PostMapping("/check")
	public ResponseEntity<?> checkPwd(
			@RequestBody @ApiParam(value = "내 정보 수정 시 필요한 비밀번호.", required = true) IdAndPwdDto idAndPwd) {
		System.out.println("전달받은 IdAndPwd: " + idAndPwd);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			String userPwd = memberService.getPwdFromId(idAndPwd.getUserId());
			if(idAndPwd.getUserPwd().equals(userPwd)) {
				return new ResponseEntity<Void>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		} catch (Exception e) {
			log.debug("비밀번호 검증 에러 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
	}
	
	// 한재현 추가
	@ApiOperation(value = "비밀번호를 변경한다..", notes = "새로운 비밀번호 변경", response = Map.class)
	@PostMapping("/modify")
	public ResponseEntity<?> modifyPwd(
			@RequestBody @ApiParam(value = "변경할 새로운 비밀번호", required = true) NewPwdDto newPwd) {
		System.out.println("전달받은 newPwd: " + newPwd);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberService.updatePwd(newPwd);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			log.debug("비밀번호 변경 에러 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
	}
	
	//이현호 추가
	@ApiOperation(value = "여행 계획 등록", notes = "여행 계획 등록")
	@PostMapping("/plan")
	public ResponseEntity<Map<String, Object>> registPlan(
			@RequestBody @ApiParam(value = "여행 계획", required = true) List<ItineraryDto> itineraryDtoList) {
		System.out.println("registPlan itineraryDto 받은 매개변수: " + itineraryDtoList);
		log.debug("registPlan ItineraryDto : {}", itineraryDtoList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			String userId = null;
			int day = -1;

			if(!itineraryDtoList.isEmpty()) {
				userId = itineraryDtoList.get(0).getUserId();
				day = itineraryDtoList.get(0).getDay();
				
				Map<String, Object> map = new HashMap<>();
				map.put("userId", userId);
				map.put("day", day);
				
				memberService.deleteOneDayPlan(map);
				for(ItineraryDto itineraryDto : itineraryDtoList) {
					memberService.registPlan(itineraryDto);
				}
				
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.badRequest().build();
			}
		} catch (Exception e) {
			log.debug("여행 계획 등록 에러 발생 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	//이현호 추가
	@ApiOperation(value = "여행 계획 조회", notes = "여행 계획 조회 정보를 담은 Dto을 반환한다.", response = Map.class)
	@GetMapping("/plan/{userId}")
//	public ResponseEntity<Map<String, Object>> getPlan(
	public ResponseEntity<List<ItineraryDto>> getPlan(
			@PathVariable("userId") @ApiParam(value = "여행 계획을 가져올 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) throws SQLException {
		log.debug("getPlan : {} ", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		System.out.println("MemberController - getPlan 결과" + memberService.getPlan(userId));
		
		return new ResponseEntity<List<ItineraryDto>>(memberService.getPlan(userId), HttpStatus.OK);
	}
	
	//이현호 추가
	//여행 계획 등록과 합쳐짐
	@ApiOperation(value = "여행 계획 수정", notes = "수정할 여행 정보를 입력한다.", response = String.class)
	@PutMapping
	public ResponseEntity<String> modifyPlan(
			@RequestBody @ApiParam(value = "수정할 글정보.", required = true) List<ItineraryDto> itineraryDtoList) throws Exception {
		log.info("modifyPlan - 호출 {}", itineraryDtoList);
		
		String userId = null;
		int day = -1;

		if(!itineraryDtoList.isEmpty()) {
			userId = itineraryDtoList.get(0).getUserId();
			day = itineraryDtoList.get(0).getDay();
			
			Map<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("day", day);
			
			memberService.deleteOneDayPlan(map);
			for(ItineraryDto itineraryDto : itineraryDtoList) {
				memberService.registPlan(itineraryDto);
			}
			
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
//	//이현호 추가
//	@ApiOperation(value = "하루의 여행 계획 삭제", notes = "하루의 여행 계획 정보를 삭제한다.", response = String.class)
//	@DeleteMapping("/plan")
//	public ResponseEntity<String> deleteOneDayPlan(
//			@RequestBody @ApiParam(value = "Map (삭제할 회원의 아이디, 일차 (예: 1일차, 2일차))", required = true) Map<String, Object> map) throws Exception {
//		log.info("deleteOneDayPlan - 호출");
//		System.out.println("deleteOneDayPlan map 호출: " + map);
//		memberService.deleteOneDayPlan(map);
//		
//		return ResponseEntity.ok().build();
//	}
	
}

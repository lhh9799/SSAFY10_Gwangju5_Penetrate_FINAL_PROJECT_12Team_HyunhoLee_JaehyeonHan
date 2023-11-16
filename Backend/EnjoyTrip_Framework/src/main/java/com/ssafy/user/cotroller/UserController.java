package com.ssafy.user.cotroller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssafy.user.model.UserDto;
import com.ssafy.user.model.mapper.UserMapper;
import com.ssafy.user.model.service.UserService;
import com.ssafy.util.Hashing;

@Controller
@RequestMapping("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserMapper userMapper;
	private UserService userServiceImpl;
	private Hashing hashing = Hashing.getInstance();

	public UserController(UserMapper userMapper, UserService userService) {
		this.userMapper = userMapper;
		this.userServiceImpl = userService;
	}
	
	@PostMapping("/join")
	private String join(@RequestParam Map<String, String> map, RedirectAttributes redirectAttributes) throws IOException, ServletException {
		UserDto userDto = new UserDto();
		userDto.setUserName(map.get("registerName"));
		userDto.setUserId(map.get("registerId"));
		userDto.setUserPassword(map.get("registerPassword"));
		userDto.setEmailId(map.get("registerEmailId"));
		userDto.setEmailDomain(map.get("registerEmailDomain"));
		
		userServiceImpl.join(userDto);
		redirectAttributes.addFlashAttribute("msg", "가입되었습니다!");
		
		return "redirect:/";
	}
	
//	@GetMapping("/login")
//	private String login() throws ServletException, IOException {
//		return "index";
//	}
	
	@PostMapping("/login")
	private String login(@RequestParam Map<String, String> map, RedirectAttributes redirectAttributes, HttpSession session) throws ServletException, IOException {
		UserDto userDto = userServiceImpl.login(map.get("loginId"), map.get("userPassword"));
		
		if (userDto != null) {
			session.setAttribute("userDto", userDto);
		} else {
			redirectAttributes.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		return "redirect:/";
	}
	
//	@GetMapping("/modifyPwd")
//	public String modifyPwd() {
//		return "user/pwdform";
//	}
	
	@PostMapping("/modifyPwd")
	private String modifyPwd(@RequestParam("inputPassword") String password, @RequestParam("newPassword") String newPassword, HttpSession session, Model model) {
		try {
			if (!isSamePwd(password, session)) {
				model.addAttribute("msg", "기존 비밀번호를 올바르게 입력해주세요.");
				
				return "user/pwdform";
			}

			String userId = ((UserDto) session.getAttribute("userDto")).getUserId();
			String salt = userMapper.selectSalt(userId);
			String hashedNewPassword = hashing.getEncrypt(newPassword, salt);
			
			userServiceImpl.chagePassword(userId, hashedNewPassword);
			
			UserDto userDto = userServiceImpl.select("user_id", userId);
			session.setAttribute("userDto", userDto);
			model.addAttribute("msg", "비밀번호가 변경되었습니다.");
				
			return "/user/userinfo";
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Should never reach here");
		
		return "redirect:/error/error";
	}
	
	//회원 정보 수정 페이지 (user/modifyform.jsp) 이동
	@GetMapping("/modify")
	public String modify() {
		return "user/modifyform";
	}
	
	//회원 정보 수정 페이지 (user/modifyform.jsp) 폼 제출
	@PostMapping("/modify")
	private String modify(@RequestParam Map<String, String> map, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
		UserDto userDto = new UserDto();
		userDto.setUserName(map.get("userName"));
		userDto.setUserId(map.get("userId"));
		userDto.setEmailId(map.get("emailId"));
		userDto.setEmailDomain(map.get("emailDomain"));
		
		try {
			userServiceImpl.modify(userDto);
			userDto = userServiceImpl.select("user_id", map.get("userId"));
			session.setAttribute("userDto", userDto);
			redirectAttributes.addFlashAttribute("msg", "변경되었습니다.");
			
			return "redirect:/user/modifyform";
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		return "error/error";
	}
	
	@GetMapping("/modifyform")
	public String modifyform() {
		return "/user/modifyform";
	}

	@PostMapping("/modifyform")
	private String modifyform(@RequestParam("inputPassword") String password, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			if (!isSamePwd(password, session)) {
				redirectAttributes.addFlashAttribute("msg", "비밀번호를 올바르게 입력해주세요.");
				return "redirect:/user/userInfo";
			}
	
			model.addAttribute("userDto", session.getAttribute("userDto"));
			
			return "redirect:/user/modifyform";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Should never reach here");
		return "";
	}
	
//	@GetMapping("/delete")
//	public String delete() {
//		return "user/delete";
//	}

	@PostMapping("/delete")
	private String delete(@RequestParam("inputPassword") String password, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		try {
			if (!isSamePwd(password, session)) {
				redirectAttributes.addFlashAttribute("msg", "비밀번호를 올바르게 입력해주세요.");
				
				return "redirect:/user/userInfo";
			}
			
			String userId = ((UserDto) session.getAttribute("userDto")).getUserId();
			userServiceImpl.delete(userId);
			
			if (session != null && session.getAttribute("userDto") != null) {
				session.removeAttribute("userDto");
				session.invalidate();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		redirectAttributes.addFlashAttribute("msg", "회원 탈퇴가 완료되었습니다.");
		
		return "redirect:/";
	}
	
	@GetMapping("/pwdform")
	public String pwdForm() {
		return "user/pwdform";
	}
	
	private boolean isSamePwd(String inputPassword, HttpSession session) {
		UserDto userDto = (UserDto) session.getAttribute("userDto");	//로그인 된 계정 정보 가져옴
		
		String loginId = userDto.getUserId();
		String loginPassword = userDto.getUserPassword();
		
		String salt = userMapper.selectSalt(loginId);
		String hashedInputPassword = hashing.getEncrypt(inputPassword, salt);
		
		if(hashedInputPassword.equals(loginPassword)) {
			return true;
		}
		
		return false;
	}
	
	@GetMapping("/userInfo")
	private String userInfo() throws IOException {
		// HttpSession session = request.getSession(false);
		// session.setAttribute("userDto", session.getAttribute("userDto"));
		return "/user/userinfo";
	}

	@GetMapping("/logout")
	private String logout(HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("userDto") != null) {
			session.removeAttribute("userDto");
		}
		session.invalidate();
		return "redirect:/";

	}
	

	

}

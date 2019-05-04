package yang.lin35.networking_service_demo.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

	@RequestMapping("/")
	public String index(HttpServletRequest request) {
		if(request.getSession().getAttribute("username") == null) {
			return "login";
		}
		return "/index";
	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request,Model model,String username,String password) {
		if("admin".equals(username) && "admin".equals(password)) {
			request.getSession().setAttribute("username", username);
		}else {
			model.addAttribute("errorMsg", "用户名或密码错误");
			return "login";
		}
		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("username");
		return "redirect:/";
	}
}

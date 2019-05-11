package yang.lin35.networking_service_demo.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import yang.lin35.networking_service_demo.dao.UserMapper;
import yang.lin35.networking_service_demo.dao.date_entitys.User;

@Controller
public class AppController {
	@Autowired
	UserMapper userMapper;

	@RequestMapping("/")
	public String index(HttpServletRequest request,Model model) {
		if(request.getSession().getAttribute("username") == null) {
			return "login";
		}
		return "/index";
	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request,Model model,String username,String password) {
		User user = userMapper.findUserByUsername(username);
		if(user==null || StringUtils.isEmpty(password)){
			model.addAttribute("errorMsg", "用户名不能为空");
			return "login";
		}
		if(user.getPassword().equals(password)) {
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

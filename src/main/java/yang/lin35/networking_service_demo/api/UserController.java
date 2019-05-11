package yang.lin35.networking_service_demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yang.lin35.networking_service_demo.dao.UserMapper;
import yang.lin35.networking_service_demo.dao.date_entitys.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/info")
    public Map<String,Object> getUserInfo(HttpServletRequest request){
        Map<String,Object> resp = new HashMap<>();
        if(request.getSession().getAttribute("username") !=null){
            User user = userMapper.findUserByUsername((String)request.getSession().getAttribute("username"));
            resp.put("code","0");
            resp.put("data",user);
            return resp;
        }
        resp.put("code","0");
        resp.put("msg","用户没有登录");
        return resp;
    }

    @RequestMapping("/contact_list")
    public Map<String,Object> getConatctList(@RequestBody  List<Long> ids){
        Map<String,Object> resp = new HashMap<>();
        if(ids != null && ids.size() > 0){
            List<User> userList = new ArrayList<>();
            for (Long id:ids){
              User user =  userMapper.findUserById(id);
              if(user != null){
                  userList.add(user);
              }
            }
            resp.put("code","0");
            resp.put("data",userList);
        }
        resp.put("code","0");
        return resp;
    }
}

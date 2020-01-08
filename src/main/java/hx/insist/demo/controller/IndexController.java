package hx.insist.demo.controller;

import com.alibaba.fastjson.JSONObject;
import hx.insist.demo.config.QQConnection;
import hx.insist.demo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class IndexController {
    @Autowired
    private QQConnection qqConnection;

    /*@RequestMapping("/")
    public String index(){
        //第一步 获取QQ登录按钮url 自己拼接，这是只是演示步骤，无实际意义
        qqConnection.getUrl();
        return "index";
    }*/

    @RequestMapping("/login")
    public Object qqLogin(@RequestParam("code")String code , @RequestParam("state") String state , Model model){
        User user = new User();
        //第二步 页面带过来code
        log.info("第二步：获取QQ互联返回的code="+code);
        //第三步 获取access token
        String accessToken = qqConnection.getAccessToken(code);
        //第四步 获取登陆后返回的 openid、appid 以JSON对象形式返回
        JSONObject userInfo = qqConnection.getUserOpenID(accessToken);
        //第五步获取用户有效(昵称、头像等）信息  以JSON对象形式返回
        String oauth_consumer_key = userInfo.getString("client_id");
        String openid = userInfo.getString("openid");
        //得到返回的user信息
        JSONObject userRealInfo = qqConnection.getUserInfo(accessToken,oauth_consumer_key,openid);
        //取出需要的user信息返回给前台
        user.setOpenid(openid);
        user.setNickName(userRealInfo.getString("nickname"));
        user.setAvatar(userRealInfo.getString("figureurl_qq"));
        user.setAvatar(userRealInfo.getString("figureurl_2"));
        model.addAttribute("User",user);
        return "index";
    }
}
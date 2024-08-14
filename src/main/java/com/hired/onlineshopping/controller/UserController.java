package com.hired.onlineshopping.controller;

import com.hired.onlineshopping.model.OnlineShoppingUser;
import com.hired.onlineshopping.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class UserController {
    HashMap<String, OnlineShoppingUser> userMaps = new HashMap<>();

    @Resource(name="lisi")
    OnlineShoppingUser defaultUser;

    @Resource(name="zhangsan")
    OnlineShoppingUser defaultUser2;

    @Resource
    JwtService jwtService;

    @PostMapping("/users")
    public String createUser(@RequestParam("id") String  id,
                             @RequestParam("name") String name,
                             @RequestParam("email") String  email,
                             Map<String, Object> resultMap) {
        //OnlineShoppingUser user = new OnlineShoppingUser(id, name, email);
        OnlineShoppingUser user = OnlineShoppingUser.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
        userMaps.put(id, user);
        String jwt = jwtService.generateToken(user);
        log.info("user: " + jwt);
        resultMap.put("abc", user);
        resultMap.put("jwtToken", jwt);
        return "user_detail";
    }

    @GetMapping("/users/{userId}")
    public String getUser(@PathVariable("userId") String userId, Map<String, Object> resultMap) {
        OnlineShoppingUser onlineShoppingUser = userMaps.getOrDefault(userId, Integer.parseInt(userId) % 2 == 0 ? defaultUser : defaultUser2);
        log.info("user: " + onlineShoppingUser.toString());
        resultMap.put("abc", onlineShoppingUser);
        return "user_detail";
    }

    @GetMapping("/login")
    @ResponseBody
    public String login(@RequestParam("jwt") String jwt) {
        return jwtService.extractAllClaims(jwt).toString();
    }

    @DeleteMapping("/user/{userId}")
    @ResponseBody
    public void deleteUser(@PathVariable("userId") String userId) {
        userMaps.remove(userId);
    }
}

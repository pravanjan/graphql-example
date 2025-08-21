package org.aw.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.aw.gateway.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestController {
    private final UserService userService;

    @GetMapping("/v1/users/{userId}/feed")
    public List<Map<String, Object>> getUserFeed(@PathVariable Long userId, @RequestParam(required = false) String fields) {
        return userService.getUserFeed(userId, fields);
    }

}

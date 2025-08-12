package org.aw.gateway.controller;

import org.aw.gateway.model.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SpringController {

    @GetMapping("/posts")
    public List<Post> getPostsByUserId() {
        return Collections.emptyList();
    }

}

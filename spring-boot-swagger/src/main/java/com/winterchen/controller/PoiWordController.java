package com.winterchen.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
public class PoiWordController {
    @PostMapping(value = "/word")
    public String helloWord(@RequestParam(value = "name") String string) {
        return string;
    }
}

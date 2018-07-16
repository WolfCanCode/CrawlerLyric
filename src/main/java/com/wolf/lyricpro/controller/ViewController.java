package com.wolf.lyricpro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/song")
    public String getSongView() {
        return "song";
    }
}

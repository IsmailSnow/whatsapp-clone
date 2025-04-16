package com.example.whatsappsclone.adapters;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class UserAdapter {

    @GetMapping
    public String me(){
        return "me";
    }
}

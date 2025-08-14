package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class GetContoller {

    @GetMapping("/")
    public String HolaMundo(){
        return "Hola mundo, soy nuevo en spring";
    }

    @PostMapping("/")
    public String postMethodName(@RequestBody String body) {
        System.out.println(body);
        System.out.println("Este es el cuerpo " + body);
        
        return "OK";
    }
    

}

package com.kuzmich.controllers;

import com.kuzmich.service.UserActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserActivationController {

    private final UserActivationService userActivationService;

    @GetMapping("/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        return userActivationService.activateUser(id)
                ? ResponseEntity.ok().body("Registration is completed successfully")
                : ResponseEntity.internalServerError().build();
    }

}

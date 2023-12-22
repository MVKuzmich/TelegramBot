package com.kuzmich.service;

import com.kuzmich.entity.AppUser;

public interface AppUserService {

    String registerUser(AppUser appUser);
    String setEmail(AppUser appUser, String email);
}

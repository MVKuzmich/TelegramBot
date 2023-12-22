package com.kuzmich.service;

import com.kuzmich.entity.AppUser;
import com.kuzmich.repository.AppUserRepository;
import com.kuzmich.utils.CryptoTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserRepository appUserRepository;
    private final CryptoTool cryptoTool;
    @Override
    public boolean activateUser(String cryptoUserId) {
        Long userId = cryptoTool.fromHash(cryptoUserId);
        Optional<AppUser> appUserOptional = appUserRepository.findAppUserById(userId);
        if(appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            appUser.setIsActive(true);
            appUserRepository.save(appUser);
            return true;
        }
        return false;
    }
}

package com.kuzmich.repository;

import com.kuzmich.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findAppUserByTelegramUserId(Long id);
    Optional<AppUser> findAppUserById(Long id);
    Optional<AppUser> findAppUserByEmail(String email);
}

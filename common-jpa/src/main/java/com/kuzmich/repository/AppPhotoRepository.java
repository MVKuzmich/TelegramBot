package com.kuzmich.repository;

import com.kuzmich.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoRepository extends JpaRepository<AppPhoto, Long> {
}

package com.example.demo.repository;

import com.example.demo.entity.GuestCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestCountRepository extends JpaRepository<GuestCountEntity, Long> {
}

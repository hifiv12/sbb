package com.mysite.sbb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.domain.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
  Optional<SiteUser> findByUsername(String username);
}

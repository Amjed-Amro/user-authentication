package com.eCommerce.demo.repository;

import com.eCommerce.demo.intities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository <AppUser,Long> {
    public Optional<AppUser> findAppUserByEmail(String email);

}

package com.eCommerce.demo.repository;

import com.eCommerce.demo.intities.AppUserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TokensRepository extends JpaRepository<AppUserToken, Long> {

    Optional<AppUserToken> findAllByTokenPath(String path);

    void deleteAllByUserEmail(String email);

    Set<AppUserToken> findAllByUserEmail(String email);

    Optional<AppUserToken> findByToken(String token);

}

package com.eCommerce.demo.repository;


import com.eCommerce.demo.intities.LoginIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LoginIpsRepository extends JpaRepository<LoginIp, Long> {


    Set<LoginIp> findAllByIpAddressAndEmail(String ip, String email);

    Set<LoginIp> findAllByEmail(String email);

}

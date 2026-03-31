package com.rex.SecureQRCodeGenerator.repository;


import com.rex.SecureQRCodeGenerator.entity.SecureLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


    public interface SecureLinkRepository extends JpaRepository<SecureLink, Long> {
        Optional<SecureLink> findByToken(String token);
    }


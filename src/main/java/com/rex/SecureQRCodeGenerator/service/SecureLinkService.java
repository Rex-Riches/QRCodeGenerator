package com.rex.SecureQRCodeGenerator.service;


import com.rex.SecureQRCodeGenerator.entity.SecureLink;
import com.rex.SecureQRCodeGenerator.repository.SecureLinkRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class SecureLinkService {

    private final SecureLinkRepository repository;
    private final QrCodeService qrCodeService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecureLinkService(SecureLinkRepository repository,
                             QrCodeService qrCodeService,
                             BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.qrCodeService = qrCodeService;
        this.passwordEncoder = passwordEncoder;
    }

    public SecureLink createSecureLink(String title, String originalUrl, String rawPassword, String baseUrl) throws Exception {
        String token = generateToken();
        String accessUrl = baseUrl + "/access/" + token;

        SecureLink link = new SecureLink();
        link.setTitle(title);
        link.setOriginalUrl(originalUrl);
        link.setToken(token);
        link.setPasswordHash(passwordEncoder.encode(rawPassword));

        String qrPath = qrCodeService.generateQrCode(accessUrl, token);
        link.setQrImagePath(qrPath);

        return repository.save(link);
    }

    public Optional<SecureLink> findByToken(String token) {
        return repository.findByToken(token);
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    private String generateToken() {
        byte[] bytes = new byte[9];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
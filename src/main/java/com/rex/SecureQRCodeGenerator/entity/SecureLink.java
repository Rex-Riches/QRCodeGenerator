package com.rex.SecureQRCodeGenerator.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
    @Table(name = "secure_link")
    public class SecureLink {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String title;

        @Column(name = "original_url", nullable = false, length = 1000)
        private String originalUrl;

        @Column(nullable = false, unique = true)
        private String token;

        @Column(name = "password_hash", nullable = false)
        private String passwordHash;

        @Column(name = "qr_image_path")
        private String qrImagePath;

        @Column(name = "created_at")
        private LocalDateTime createdAt = LocalDateTime.now();

        public SecureLink() {
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        public void setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
        }

        public String getQrImagePath() {
            return qrImagePath;
        }

        public void setQrImagePath(String qrImagePath) {
            this.qrImagePath = qrImagePath;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }


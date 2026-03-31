package com.rex.SecureQRCodeGenerator.controller;

import com.rex.SecureQRCodeGenerator.entity.SecureLink;
import com.rex.SecureQRCodeGenerator.service.QrCodeService;
import com.rex.SecureQRCodeGenerator.service.SecureLinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class HomeController {

    private final SecureLinkService secureLinkService;
    private final QrCodeService qrCodeService;

    public HomeController(SecureLinkService secureLinkService, QrCodeService qrCodeService) {
        this.secureLinkService = secureLinkService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @PostMapping("/generate")
    public String generateQrCode(@RequestParam String title,
                                 @RequestParam String originalUrl,
                                 @RequestParam String password,
                                 HttpServletRequest request,
                                 Model model) {
        try {
            String baseUrl = request.getScheme() + "://" + request.getServerName();
            if (request.getServerPort() != 80 && request.getServerPort() != 443) {
                baseUrl += ":" + request.getServerPort();
            }

            SecureLink secureLink = secureLinkService.createSecureLink(title, originalUrl, password);

            String accessLink = baseUrl + "/access/" + secureLink.getToken();
            String qrCodeBase64 = qrCodeService.generateQrCodeBase64(accessLink);

            model.addAttribute("title", secureLink.getTitle());
            model.addAttribute("accessLink", accessLink);
            model.addAttribute("qrCodeBase64", qrCodeBase64);
            model.addAttribute("token", secureLink.getToken());

            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to generate QR code: " + e.getMessage());
            return "index";
        }
    }

    @GetMapping("/download/{token}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadQrCode(@PathVariable String token,
                                                 HttpServletRequest request) {
        try {
            Optional<SecureLink> optionalLink = secureLinkService.findByToken(token);

            if (optionalLink.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String baseUrl = request.getScheme() + "://" + request.getServerName();
            if (request.getServerPort() != 80 && request.getServerPort() != 443) {
                baseUrl += ":" + request.getServerPort();
            }

            String accessLink = baseUrl + "/access/" + token;
            byte[] qrBytes = qrCodeService.generateQrCodeBytes(accessLink);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"secure-qr-code.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
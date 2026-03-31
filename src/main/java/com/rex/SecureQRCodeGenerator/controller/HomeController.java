package com.rex.SecureQRCodeGenerator.controller;

import com.rex.SecureQRCodeGenerator.entity.SecureLink;
import com.rex.SecureQRCodeGenerator.service.QrCodeService;
import com.rex.SecureQRCodeGenerator.service.SecureLinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to generate QR code: " + e.getMessage());
            return "index";
        }
    }
}
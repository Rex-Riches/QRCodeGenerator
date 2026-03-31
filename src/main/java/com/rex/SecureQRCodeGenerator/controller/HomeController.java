package com.rex.SecureQRCodeGenerator.controller;


import com.rex.SecureQRCodeGenerator.entity.SecureLink;
import com.rex.SecureQRCodeGenerator.service.SecureLinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final SecureLinkService secureLinkService;

    public HomeController(SecureLinkService secureLinkService) {
        this.secureLinkService = secureLinkService;
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
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

            SecureLink secureLink = secureLinkService.createSecureLink(title, originalUrl, password, baseUrl);

            model.addAttribute("title", secureLink.getTitle());
            model.addAttribute("qrImagePath", secureLink.getQrImagePath());
            model.addAttribute("accessLink", baseUrl + "/access/" + secureLink.getToken());

            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to generate QR code: " + e.getMessage());
            return "index";
        }
    }
}
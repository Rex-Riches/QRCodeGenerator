package com.rex.SecureQRCodeGenerator.controller;


import com.rex.SecureQRCodeGenerator.entity.SecureLink;
import com.rex.SecureQRCodeGenerator.service.SecureLinkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

        import java.util.Optional;

@Controller
public class AccessController {

    private final SecureLinkService secureLinkService;

    public AccessController(SecureLinkService secureLinkService) {
        this.secureLinkService = secureLinkService;
    }

    @GetMapping("/access/{token}")
    public String showPasswordPage(@PathVariable String token, Model model) {
        Optional<SecureLink> link = secureLinkService.findByToken(token);

        if (link.isEmpty()) {
            return "not-found";
        }

        model.addAttribute("token", token);
        model.addAttribute("title", link.get().getTitle());
        return "verify";
    }

    @PostMapping("/access/{token}")
    public String verifyPassword(@PathVariable String token,
                                 @RequestParam String password,
                                 Model model) {

        Optional<SecureLink> linkOptional = secureLinkService.findByToken(token);

        if (linkOptional.isEmpty()) {
            return "not-found";
        }

        SecureLink link = linkOptional.get();

        if (secureLinkService.checkPassword(password, link.getPasswordHash())) {
            return "redirect:" + link.getOriginalUrl();
        }

        model.addAttribute("token", token);
        model.addAttribute("title", link.getTitle());
        model.addAttribute("error", "Incorrect password. Please try again.");
        return "verify";
    }
}

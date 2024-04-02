package com.example.smart_report.loginController;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.smart_report.domain.User;
import com.example.smart_report.repos.UserRepository;
import com.example.smart_report.util.WebUtils;

import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "home/index";
    }

    @GetMapping("/user/profile")
    public String getUserprofile(Model m, final RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByEmail(username);

        User user = userOptional.get();
        m.addAttribute("user", user);
        return "home/userProfile";
    }

    @GetMapping("/user")
    public String indexuser() {
        return "home/USER";
    }

    @GetMapping("/healthcare_professional")
    public String healthcare_professional() {
        return "home/HEALTHCARE_PROFESSIONAL";
    }

    @GetMapping("/inventory_manager")
    public String inventory_manager() {
        return "home/INVENTORY_MANAGER";
    }

    @GetMapping("/lab_technician")
    public String lab_technician() {
        return "home/LAB_TECHNICIAN";
    }

    @GetMapping("/admin")
    public String admin() {
        return "home/ADMIN";
    }

    @GetMapping("/meeting")
    public String admi2n() {
        return "home/meeting";
    }

    @Autowired
    private JavaMailSender javaMailSender; // Assuming you have configured JavaMailSender bean

    @GetMapping("/applyForRole")
    public String applyForRole(@RequestParam("role") String role, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (canSendEmail(user)) {
                sendEmailToAdmin(role, user);
                updateLastEmailSentTime(user);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, "Role application email sent successfully.");
            } else {
                redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                        "You have already sent an email within the last 24 hours. Please wait before sending another email.");
            }
        }
        return "redirect:/user";
    }

    // Method to check if the user can send an email (not sent within the last 24
    // hours)
    private boolean canSendEmail(User user) {
        LocalDateTime lastEmailSentTime = user.getLastEmailSentTime();
        if (lastEmailSentTime == null) {
            return true; // No email has been sent before
        }
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.minusHours(24).isAfter(lastEmailSentTime);
    }

    // Method to send email to admin for role application
    private void sendEmailToAdmin(String role, User user) {
        if (user != null) {
            // Create a simple mail message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("luckymourya634@gmail.com"); // Admin's email address
            message.setSubject("Role Application");
            message.setText("User " + user.getUserName() + " with email " + user.getEmail()
                    + " has applied for the role: " + role);
            // Send email
            javaMailSender.send(message);
        }
    }

    // Method to update the last email sent time for the user
    private void updateLastEmailSentTime(User user) {
        user.setLastEmailSentTime(LocalDateTime.now());
        userRepository.save(user);
    }

}

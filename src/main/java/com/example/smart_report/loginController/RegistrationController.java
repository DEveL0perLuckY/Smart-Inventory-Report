package com.example.smart_report.loginController;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.smart_report.domain.Role;
import com.example.smart_report.domain.User;
import com.example.smart_report.model.UserDto;
import com.example.smart_report.repos.RoleRepository;
import com.example.smart_report.repos.UserRepository;
import com.example.smart_report.util.WebUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("null")
@Controller
public class RegistrationController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/unAuthorized")
    public String unAuthorized() {
        return "Pages/unAuthorized";
    }

    @GetMapping("/signup")
    public String register(Model model) {
        UserDto userDTO = new UserDto();
        userDTO.setRoleIdCount(Constant.ROLE_USER);
        model.addAttribute("obj", userDTO);
        return "Pages/SingUp";
    }

    @PostMapping("/signup")
    public String registration(@Valid @ModelAttribute("obj") UserDto userDto,
            final RedirectAttributes redirectAttributes, BindingResult result, Model model) {
        if (userAlreadyRegistered(userDto.getEmail(), result)) {
            return "Pages/SingUp";
        }
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "Pages/SingUp";
        }

        Optional<Role> role = roleRepository.findById(Constant.ROLE_USER);

        Set<Role> roles = new HashSet<>();
        if (role.isPresent()) {
            roles.add(role.get());
        } else {

            Role ROLE_ADMIN = new Role();
            ROLE_ADMIN.setRoleId(Constant.ROLE_ADMIN);
            ROLE_ADMIN.setName("ROLE_ADMIN");

            Role ROLE_HEALTHCARE_PROFESSIONAL = new Role();
            ROLE_HEALTHCARE_PROFESSIONAL.setRoleId(Constant.ROLE_HEALTHCARE_PROFESSIONAL);
            ROLE_HEALTHCARE_PROFESSIONAL.setName("ROLE_HEALTHCARE_PROFESSIONAL");

            Role ROLE_INVENTORY_MANAGER = new Role();
            ROLE_INVENTORY_MANAGER.setRoleId(Constant.ROLE_INVENTORY_MANAGER);
            ROLE_INVENTORY_MANAGER.setName("ROLE_INVENTORY_MANAGER");

            Role ROLE_LAB_TECHNICIAN = new Role();
            ROLE_LAB_TECHNICIAN.setRoleId(Constant.ROLE_LAB_TECHNICIAN);
            ROLE_LAB_TECHNICIAN.setName("ROLE_LAB_TECHNICIAN");

            Role ROLE_USER = new Role();
            ROLE_USER.setRoleId(Constant.ROLE_USER);
            ROLE_USER.setName("ROLE_USER");

            roleRepository.saveAll(Arrays.asList(ROLE_ADMIN, ROLE_HEALTHCARE_PROFESSIONAL, ROLE_INVENTORY_MANAGER,
                    ROLE_USER, ROLE_LAB_TECHNICIAN));
            roles.addAll(Arrays.asList(ROLE_ADMIN, ROLE_HEALTHCARE_PROFESSIONAL, ROLE_INVENTORY_MANAGER,
                    ROLE_USER, ROLE_LAB_TECHNICIAN));
        }
        User user = new User();
        try {
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRoleId(roles);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                    WebUtils.getMessage("Registration successfully!"));
            return "redirect:/login";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage("Registration failed. Please try again."));
            return "redirect:/login?fail";
        }

    }

    private boolean userAlreadyRegistered(String email, BindingResult result) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            result.rejectValue("email", null, "User already registered!");
            return true;
        }
        return false;
    }
}

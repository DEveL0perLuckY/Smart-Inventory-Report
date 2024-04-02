package com.example.smart_report.controller;

import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.smart_report.domain.Role;
import com.example.smart_report.domain.User;
import com.example.smart_report.loginController.Constant;
import com.example.smart_report.model.UserDto;
import com.example.smart_report.repos.RoleRepository;
import com.example.smart_report.repos.UserRepository;
import com.example.smart_report.service.RoleService;
import com.example.smart_report.util.CustomCollectors;
import com.example.smart_report.util.NotFoundException;
import com.example.smart_report.util.WebUtils;

@SuppressWarnings("null")
@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleService roleService;

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleIdValues", roleRepository.findAll(Sort.by("roleId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Role::getRoleId, Role::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable(name = "userId") final Integer userId, final Model model) {
        model.addAttribute("user", get(userId));
        model.addAttribute("roles", roleService.findAll());
        return "user/edit";
    }

    @PostMapping("/edit/{userId}")
    public String edit(@PathVariable(name = "userId") final Integer userId,
            @ModelAttribute("user") @Valid final UserDto userDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {

        User temp = userRepository.findById(userId).get();
        if (userDTO.getRoleId().isEmpty()) {
            Role roleUser = roleRepository.findById(Constant.ROLE_USER)
                    .orElseThrow(() -> new IllegalStateException("User role not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(roleUser);
            temp.setRoleId(roles);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO,
                    WebUtils.getMessage(
                            "Role assigned successfully and user role is given bkz you can't give null role to the user"));

        } else {

            Set<Role> roleSet = userDTO.getRoleId().stream()
                    .map(roleId -> roleRepository.findById(roleId).get())
                    .collect(Collectors.toSet());
            temp.setRoleId(roleSet);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                    WebUtils.getMessage("Role assigned successfully"));
        }
        userRepository.save(temp);
        return "redirect:/admin/users";
    }

    public UserDto get(final Integer userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDto()))
                .orElseThrow(NotFoundException::new);
    }

    private UserDto mapToDTO(final User user, final UserDto userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoleId(user.getRoleId().stream()
                .map(role -> role.getRoleId())
                .toList());
        return userDTO;
    }

}

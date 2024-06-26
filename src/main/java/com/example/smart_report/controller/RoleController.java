package com.example.smart_report.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.smart_report.model.RoleDTO;
import com.example.smart_report.service.RoleService;
import com.example.smart_report.util.WebUtils;

@Controller
@RequestMapping("/admin/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "role/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("role") final RoleDTO roleDTO) {
        return "role/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("role") @Valid final RoleDTO roleDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "role/add";
        }
        roleService.create(roleDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("role.create.success"));
        return "redirect:/admin/roles";
    }

    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable(name = "roleId") final Integer roleId, final Model model) {
        model.addAttribute("role", roleService.get(roleId));
        return "role/edit";
    }

    @PostMapping("/edit/{roleId}")
    public String edit(@PathVariable(name = "roleId") final Integer roleId,
            @ModelAttribute("role") @Valid final RoleDTO roleDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "role/edit";
        }
        roleService.update(roleId, roleDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("role.update.success"));
        return "redirect:/admin/roles";
    }

    @PostMapping("/delete/{roleId}")
    public String delete(@PathVariable(name = "roleId") final Integer roleId,
            final RedirectAttributes redirectAttributes) {
        roleService.delete(roleId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("role.delete.success"));
        return "redirect:/admin/roles";
    }

}

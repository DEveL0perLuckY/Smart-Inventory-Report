package com.example.smart_report.controller;

import com.example.smart_report.model.ContainerDTO;
import com.example.smart_report.service.ContainerService;
import com.example.smart_report.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/containers")
public class ContainerController {

    private final ContainerService containerService;

    public ContainerController(final ContainerService containerService) {
        this.containerService = containerService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("containers", containerService.findAll());
        return "container/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("container") final ContainerDTO containerDTO) {
        return "container/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("container") @Valid final ContainerDTO containerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "container/add";
        }
        containerService.create(containerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("container.create.success"));
        return "redirect:/containers";
    }

    @GetMapping("/edit/{containerId}")
    public String edit(@PathVariable(name = "containerId") final Integer containerId,
            final Model model) {
        model.addAttribute("container", containerService.get(containerId));
        return "container/edit";
    }

    @PostMapping("/edit/{containerId}")
    public String edit(@PathVariable(name = "containerId") final Integer containerId,
            @ModelAttribute("container") @Valid final ContainerDTO containerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "container/edit";
        }
        containerService.update(containerId, containerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("container.update.success"));
        return "redirect:/containers";
    }

    @PostMapping("/delete/{containerId}")
    public String delete(@PathVariable(name = "containerId") final Integer containerId,
            final RedirectAttributes redirectAttributes) {
        containerService.delete(containerId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("container.delete.success"));
        return "redirect:/containers";
    }

}

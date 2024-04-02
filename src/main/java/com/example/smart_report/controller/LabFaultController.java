package com.example.smart_report.controller;

import com.example.smart_report.model.LabFaultDTO;
import com.example.smart_report.service.LabFaultService;
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
@RequestMapping("/labFaults")
public class LabFaultController {

    private final LabFaultService labFaultService;

    public LabFaultController(final LabFaultService labFaultService) {
        this.labFaultService = labFaultService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("labFaults", labFaultService.findAll());
        return "labFault/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("labFault") final LabFaultDTO labFaultDTO) {
        return "labFault/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("labFault") @Valid final LabFaultDTO labFaultDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "labFault/add";
        }
        labFaultService.create(labFaultDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("labFault.create.success"));
        return "redirect:/labFaults";
    }

    @GetMapping("/edit/{faultId}")
    public String edit(@PathVariable(name = "faultId") final Integer faultId, final Model model) {
        model.addAttribute("labFault", labFaultService.get(faultId));
        return "labFault/edit";
    }

    @PostMapping("/edit/{faultId}")
    public String edit(@PathVariable(name = "faultId") final Integer faultId,
            @ModelAttribute("labFault") @Valid final LabFaultDTO labFaultDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "labFault/edit";
        }
        labFaultService.update(faultId, labFaultDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("labFault.update.success"));
        return "redirect:/labFaults";
    }

    @PostMapping("/delete/{faultId}")
    public String delete(@PathVariable(name = "faultId") final Integer faultId,
            final RedirectAttributes redirectAttributes) {
        labFaultService.delete(faultId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("labFault.delete.success"));
        return "redirect:/labFaults";
    }

}

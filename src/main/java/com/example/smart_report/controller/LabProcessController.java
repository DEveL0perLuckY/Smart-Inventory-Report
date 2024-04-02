package com.example.smart_report.controller;

import com.example.smart_report.model.LabProcessDTO;
import com.example.smart_report.service.LabProcessService;
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
@RequestMapping("/labProcesses")
public class LabProcessController {

    private final LabProcessService labProcessService;

    public LabProcessController(final LabProcessService labProcessService) {
        this.labProcessService = labProcessService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("labProcesses", labProcessService.findAll());
        return "labProcess/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("labProcess") final LabProcessDTO labProcessDTO) {
        return "labProcess/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("labProcess") @Valid final LabProcessDTO labProcessDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "labProcess/add";
        }
        labProcessService.create(labProcessDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("labProcess.create.success"));
        return "redirect:/labProcesses";
    }

    @GetMapping("/edit/{processId}")
    public String edit(@PathVariable(name = "processId") final Integer processId,
            final Model model) {
        model.addAttribute("labProcess", labProcessService.get(processId));
        return "labProcess/edit";
    }

    @PostMapping("/edit/{processId}")
    public String edit(@PathVariable(name = "processId") final Integer processId,
            @ModelAttribute("labProcess") @Valid final LabProcessDTO labProcessDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "labProcess/edit";
        }
        labProcessService.update(processId, labProcessDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("labProcess.update.success"));
        return "redirect:/labProcesses";
    }

    @PostMapping("/delete/{processId}")
    public String delete(@PathVariable(name = "processId") final Integer processId,
            final RedirectAttributes redirectAttributes) {
        labProcessService.delete(processId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("labProcess.delete.success"));
        return "redirect:/labProcesses";
    }

}

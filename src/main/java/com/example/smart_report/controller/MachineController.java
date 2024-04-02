package com.example.smart_report.controller;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.model.MachineDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.service.MachineService;
import com.example.smart_report.util.CustomCollectors;
import com.example.smart_report.util.WebUtils;
import jakarta.validation.Valid;
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


@Controller
@RequestMapping("/machines")
public class MachineController {

    private final MachineService machineService;
    private final HealthTestRepository healthTestRepository;

    public MachineController(final MachineService machineService,
            final HealthTestRepository healthTestRepository) {
        this.machineService = machineService;
        this.healthTestRepository = healthTestRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("testValues", healthTestRepository.findAll(Sort.by("testId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(HealthTest::getTestId, HealthTest::getTestName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("machines", machineService.findAll());
        return "machine/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("machine") final MachineDTO machineDTO) {
        return "machine/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("machine") @Valid final MachineDTO machineDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "machine/add";
        }
        machineService.create(machineDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("machine.create.success"));
        return "redirect:/machines";
    }

    @GetMapping("/edit/{machineId}")
    public String edit(@PathVariable(name = "machineId") final Integer machineId,
            final Model model) {
        model.addAttribute("machine", machineService.get(machineId));
        return "machine/edit";
    }

    @PostMapping("/edit/{machineId}")
    public String edit(@PathVariable(name = "machineId") final Integer machineId,
            @ModelAttribute("machine") @Valid final MachineDTO machineDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "machine/edit";
        }
        machineService.update(machineId, machineDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("machine.update.success"));
        return "redirect:/machines";
    }

    @PostMapping("/delete/{machineId}")
    public String delete(@PathVariable(name = "machineId") final Integer machineId,
            final RedirectAttributes redirectAttributes) {
        machineService.delete(machineId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("machine.delete.success"));
        return "redirect:/machines";
    }

}

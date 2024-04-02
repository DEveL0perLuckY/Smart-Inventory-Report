package com.example.smart_report.controller;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.model.HealthParameterDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.service.HealthParameterService;
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
@RequestMapping("/healthParameters")
public class HealthParameterController {

    private final HealthParameterService healthParameterService;
    private final HealthTestRepository healthTestRepository;

    public HealthParameterController(final HealthParameterService healthParameterService,
            final HealthTestRepository healthTestRepository) {
        this.healthParameterService = healthParameterService;
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
        model.addAttribute("healthParameters", healthParameterService.findAll());
        return "healthParameter/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("healthParameter") final HealthParameterDTO healthParameterDTO) {
        return "healthParameter/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("healthParameter") @Valid final HealthParameterDTO healthParameterDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "healthParameter/add";
        }
        healthParameterService.create(healthParameterDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("healthParameter.create.success"));
        return "redirect:/healthParameters";
    }

    @GetMapping("/edit/{parameterId}")
    public String edit(@PathVariable(name = "parameterId") final Integer parameterId,
            final Model model) {
        model.addAttribute("healthParameter", healthParameterService.get(parameterId));
        return "healthParameter/edit";
    }

    @PostMapping("/edit/{parameterId}")
    public String edit(@PathVariable(name = "parameterId") final Integer parameterId,
            @ModelAttribute("healthParameter") @Valid final HealthParameterDTO healthParameterDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "healthParameter/edit";
        }
        healthParameterService.update(parameterId, healthParameterDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("healthParameter.update.success"));
        return "redirect:/healthParameters";
    }

    @PostMapping("/delete/{parameterId}")
    public String delete(@PathVariable(name = "parameterId") final Integer parameterId,
            final RedirectAttributes redirectAttributes) {
        healthParameterService.delete(parameterId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("healthParameter.delete.success"));
        return "redirect:/healthParameters";
    }

}

package com.example.smart_report.controller;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.model.ReagentDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.service.ReagentService;
import com.example.smart_report.util.CustomCollectors;
import com.example.smart_report.util.ReferencedWarning;
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
@RequestMapping("/reagents")
public class ReagentController {

    private final ReagentService reagentService;
    private final HealthTestRepository healthTestRepository;

    public ReagentController(final ReagentService reagentService,
            final HealthTestRepository healthTestRepository) {
        this.reagentService = reagentService;
        this.healthTestRepository = healthTestRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("reagentTestMappingHealthTestsValues", healthTestRepository.findAll(Sort.by("testId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(HealthTest::getTestId, HealthTest::getTestName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reagents", reagentService.findAll());
        return "reagent/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reagent") final ReagentDTO reagentDTO) {
        return "reagent/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("reagent") @Valid final ReagentDTO reagentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reagent/add";
        }
        reagentService.create(reagentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reagent.create.success"));
        return "redirect:/reagents";
    }

    @GetMapping("/edit/{reagentId}")
    public String edit(@PathVariable(name = "reagentId") final Integer reagentId,
            final Model model) {
        model.addAttribute("reagent", reagentService.get(reagentId));
        return "reagent/edit";
    }

    @PostMapping("/edit/{reagentId}")
    public String edit(@PathVariable(name = "reagentId") final Integer reagentId,
            @ModelAttribute("reagent") @Valid final ReagentDTO reagentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reagent/edit";
        }
        reagentService.update(reagentId, reagentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reagent.update.success"));
        return "redirect:/reagents";
    }

    @PostMapping("/delete/{reagentId}")
    public String delete(@PathVariable(name = "reagentId") final Integer reagentId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = reagentService.getReferencedWarning(reagentId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            reagentService.delete(reagentId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reagent.delete.success"));
        }
        return "redirect:/reagents";
    }

}

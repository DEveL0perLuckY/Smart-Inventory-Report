package com.example.smart_report.controller;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.model.TestLabMappingDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.service.TestLabMappingService;
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
@RequestMapping("/testLabMappings")
public class TestLabMappingController {

    private final TestLabMappingService testLabMappingService;
    private final HealthTestRepository healthTestRepository;

    public TestLabMappingController(final TestLabMappingService testLabMappingService,
            final HealthTestRepository healthTestRepository) {
        this.testLabMappingService = testLabMappingService;
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
        model.addAttribute("testLabMappings", testLabMappingService.findAll());
        return "testLabMapping/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("testLabMapping") final TestLabMappingDTO testLabMappingDTO) {
        return "testLabMapping/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("testLabMapping") @Valid final TestLabMappingDTO testLabMappingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "testLabMapping/add";
        }
        testLabMappingService.create(testLabMappingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("testLabMapping.create.success"));
        return "redirect:/testLabMappings";
    }

    @GetMapping("/edit/{labId}")
    public String edit(@PathVariable(name = "labId") final Integer labId, final Model model) {
        model.addAttribute("testLabMapping", testLabMappingService.get(labId));
        return "testLabMapping/edit";
    }

    @PostMapping("/edit/{labId}")
    public String edit(@PathVariable(name = "labId") final Integer labId,
            @ModelAttribute("testLabMapping") @Valid final TestLabMappingDTO testLabMappingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "testLabMapping/edit";
        }
        testLabMappingService.update(labId, testLabMappingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("testLabMapping.update.success"));
        return "redirect:/testLabMappings";
    }

    @PostMapping("/delete/{labId}")
    public String delete(@PathVariable(name = "labId") final Integer labId,
            final RedirectAttributes redirectAttributes) {
        testLabMappingService.delete(labId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("testLabMapping.delete.success"));
        return "redirect:/testLabMappings";
    }

}

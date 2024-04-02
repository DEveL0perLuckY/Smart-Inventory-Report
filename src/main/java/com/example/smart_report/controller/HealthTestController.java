package com.example.smart_report.controller;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Machine;
import com.example.smart_report.model.HealthTestDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.repos.MachineRepository;
import com.example.smart_report.service.HealthTestService;
import com.example.smart_report.util.CustomCollectors;
import com.example.smart_report.util.ReferencedWarning;
import com.example.smart_report.util.WebUtils;
import jakarta.validation.Valid;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@SuppressWarnings("null")
@RequestMapping("/healthTests")
public class HealthTestController {

    private final HealthTestService healthTestService;
    private final MachineRepository machineRepository;

    public HealthTestController(final HealthTestService healthTestService,
            final MachineRepository machineRepository) {
        this.healthTestService = healthTestService;
        this.machineRepository = machineRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("testMachineMappingMachinesValues", machineRepository.findAll(Sort.by("machineId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Machine::getMachineId, Machine::getMachineName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("healthTests", healthTestService.findAll());
        return "healthTest/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("healthTest") final HealthTestDTO healthTestDTO) {
        return "healthTest/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("healthTest") @Valid final HealthTestDTO healthTestDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "healthTest/add";
        }
        healthTestService.create(healthTestDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("healthTest.create.success"));
        return "redirect:/healthTests";
    }

    @GetMapping("/edit/{testId}")
    public String edit(@PathVariable(name = "testId") final Integer testId, final Model model) {
        model.addAttribute("healthTest", healthTestService.get(testId));
        return "healthTest/edit";
    }

    @PostMapping("/edit/{testId}")
    public String edit(@PathVariable(name = "testId") final Integer testId,
            @ModelAttribute("healthTest") @Valid final HealthTestDTO healthTestDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "healthTest/edit";
        }
        healthTestService.update(testId, healthTestDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("healthTest.update.success"));
        return "redirect:/healthTests";
    }

    @PostMapping("/delete/{testId}")
    public String delete(@PathVariable(name = "testId") final Integer testId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = healthTestService.getReferencedWarning(testId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            healthTestService.delete(testId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("healthTest.delete.success"));
        }
        return "redirect:/healthTests";
    }

    @Autowired
    HealthTestRepository healthTestRepository;

    @GetMapping("/downloadPdf/{testId}")
    public ResponseEntity<ByteArrayResource> downloadPdfReport(@PathVariable Integer testId) {
        HealthTest healthTest = healthTestRepository.findById(testId).orElse(null);
        if (healthTest == null) {
            // Return a response indicating that the requested health test was not found
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] pdfBytes = healthTestService.generatePdfForHealthTest(healthTest);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=HealthTestReport.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(resource);
        } catch (IOException e) {
            // Log the exception or handle it appropriately
            e.printStackTrace(); // Example: Printing the stack trace
            // Return a response indicating that there was an error generating the PDF
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

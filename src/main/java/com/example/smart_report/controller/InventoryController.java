package com.example.smart_report.controller;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.model.InventoryDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.service.InventoryService;
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
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryService inventoryService;
    private final HealthTestRepository healthTestRepository;

    public InventoryController(final InventoryService inventoryService,
            final HealthTestRepository healthTestRepository) {
        this.inventoryService = inventoryService;
        this.healthTestRepository = healthTestRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("itemValues", healthTestRepository.findAll(Sort.by("testId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(HealthTest::getTestId, HealthTest::getTestName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("inventories", inventoryService.findAll());
        return "inventory/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("inventory") final InventoryDTO inventoryDTO) {
        return "inventory/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("inventory") @Valid final InventoryDTO inventoryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inventory/add";
        }
        inventoryService.create(inventoryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("inventory.create.success"));
        return "redirect:/inventories";
    }

    @GetMapping("/edit/{inventoryId}")
    public String edit(@PathVariable(name = "inventoryId") final Integer inventoryId,
            final Model model) {
        model.addAttribute("inventory", inventoryService.get(inventoryId));
        return "inventory/edit";
    }

    @PostMapping("/edit/{inventoryId}")
    public String edit(@PathVariable(name = "inventoryId") final Integer inventoryId,
            @ModelAttribute("inventory") @Valid final InventoryDTO inventoryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inventory/edit";
        }
        inventoryService.update(inventoryId, inventoryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("inventory.update.success"));
        return "redirect:/inventories";
    }

    @PostMapping("/delete/{inventoryId}")
    public String delete(@PathVariable(name = "inventoryId") final Integer inventoryId,
            final RedirectAttributes redirectAttributes) {
        inventoryService.delete(inventoryId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("inventory.delete.success"));
        return "redirect:/inventories";
    }

}

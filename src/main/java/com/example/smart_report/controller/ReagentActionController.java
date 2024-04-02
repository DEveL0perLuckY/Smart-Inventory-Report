package com.example.smart_report.controller;

import com.example.smart_report.domain.Reagent;
import com.example.smart_report.model.ReagentActionDTO;
import com.example.smart_report.repos.ReagentRepository;
import com.example.smart_report.service.ReagentActionService;
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
@RequestMapping("/reagentActions")
public class ReagentActionController {

    private final ReagentActionService reagentActionService;
    private final ReagentRepository reagentRepository;

    public ReagentActionController(final ReagentActionService reagentActionService,
            final ReagentRepository reagentRepository) {
        this.reagentActionService = reagentActionService;
        this.reagentRepository = reagentRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("reagentValues", reagentRepository.findAll(Sort.by("reagentId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Reagent::getReagentId, Reagent::getReagentName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reagentActions", reagentActionService.findAll());
        return "reagentAction/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reagentAction") final ReagentActionDTO reagentActionDTO) {
        return "reagentAction/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("reagentAction") @Valid final ReagentActionDTO reagentActionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reagentAction/add";
        }
        reagentActionService.create(reagentActionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reagentAction.create.success"));
        return "redirect:/reagentActions";
    }

    @GetMapping("/edit/{actionId}")
    public String edit(@PathVariable(name = "actionId") final Integer actionId, final Model model) {
        model.addAttribute("reagentAction", reagentActionService.get(actionId));
        return "reagentAction/edit";
    }

    @PostMapping("/edit/{actionId}")
    public String edit(@PathVariable(name = "actionId") final Integer actionId,
            @ModelAttribute("reagentAction") @Valid final ReagentActionDTO reagentActionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reagentAction/edit";
        }
        reagentActionService.update(actionId, reagentActionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reagentAction.update.success"));
        return "redirect:/reagentActions";
    }

    @PostMapping("/delete/{actionId}")
    public String delete(@PathVariable(name = "actionId") final Integer actionId,
            final RedirectAttributes redirectAttributes) {
        reagentActionService.delete(actionId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reagentAction.delete.success"));
        return "redirect:/reagentActions";
    }

}

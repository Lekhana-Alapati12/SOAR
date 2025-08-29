package com.example.SOARSpringBoot.controllers;

import com.example.SOARSpringBoot.entity.Inventory;
import com.example.SOARSpringBoot.entity.User;
import com.example.SOARSpringBoot.exception.InventoryAlreadyExistsException;
import com.example.SOARSpringBoot.exception.InventoryNotFoundException;
import com.example.SOARSpringBoot.repository.InventoryRepository;
import com.example.SOARSpringBoot.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    public InventoryService inventoryService;
    public ArrayList<String> typeList;
    @ModelAttribute
    public void preLoad(Model model)
    {
        typeList=new ArrayList<>();
        typeList.add("Laptop");
        typeList.add("Desktop");
        typeList.add("Monitor");
        typeList.add("Connecting cables");
        typeList.add("Keyboard");
        typeList.add("Mouse");
        typeList.add("Docking port");
    }
    @GetMapping("/invt-maintenance")
    public String viewInvtMainPage(Model model)
    {
        List<Inventory> inventoryList=inventoryService.getInventoryList();
        model.addAttribute("inventoryList",inventoryList);
        //model.addAttribute("typeList",typeList);
        return "invt-main-page";
    }

    @GetMapping("/invt-maintenance/invt/edit/{id}")
    public String viewUserDetails(@PathVariable(name="id") Long id, RedirectAttributes redirectAttributes, Model model)
    {
        try {
            Inventory inventory = inventoryService.getInventory(id);
            model.addAttribute("typeList", typeList);
            model.addAttribute("inventory", inventory);
            return "inventory-edit-form";
        }
        catch(InventoryNotFoundException ex)
        {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/invt-maintenance";
        }
    }
    @GetMapping("/add-inventory")
    public String showInventoryForm(Model model)
    {
        model.addAttribute("inventory",new Inventory());
        model.addAttribute("typeList", typeList);
        return "inventory-add-form";
    }
    @PostMapping("/invt-maintenance/invt/add")
    public String addInventory(final @Valid Inventory inventory,final BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model)
    {
        try{
            model.addAttribute("typeList", typeList);
            if(bindingResult.hasErrors()){
                model.addAttribute("inventoryform", inventory);
                return "inventory-add";
            }
            Inventory res=inventoryService.addInventory(inventory);
            redirectAttributes.addFlashAttribute("message","Inventory added");
            return "redirect:/invt-maintenance";
        }
        catch(InventoryAlreadyExistsException e)
        {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/invt-maintenance/invt/add";
        }

    }


    @PostMapping("/invt-maintenance/invt/edit/{id}")
    public String editUserDetails(@PathVariable(name="id") Long id, Inventory inventory,RedirectAttributes redirectAttributes,Model model)
    {
        try {
            Inventory editable=inventoryService.getInventory(id);
            Inventory updated=inventoryService.updateInventory(inventory,editable);
            inventoryRepository.save(updated);
            return "redirect:/invt-maintenance";
        } catch (InventoryNotFoundException | InventoryAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/invt-maintenance/invt/edit/"+id;
        }
    }

}

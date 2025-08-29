package com.example.SOARSpringBoot.controllers;

import com.example.SOARSpringBoot.entity.CardItem;
import com.example.SOARSpringBoot.entity.Inventory;
import com.example.SOARSpringBoot.entity.RequestApprove;
import com.example.SOARSpringBoot.entity.User;
import com.example.SOARSpringBoot.exception.CannotAddInventoryException;
import com.example.SOARSpringBoot.exception.InventoryAlreadyExistsException;
import com.example.SOARSpringBoot.repository.CardItemRepository;
import com.example.SOARSpringBoot.repository.InventoryRepository;
import com.example.SOARSpringBoot.repository.UserRepository;
import com.example.SOARSpringBoot.service.CardItemService;
import com.example.SOARSpringBoot.service.InventoryService;
import com.example.SOARSpringBoot.service.RequestApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;

@Controller
public class InventoryRequestController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardItemService cardItemService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CardItemRepository cardItemRepository;
    @Autowired
    private RequestApproveService requestApproveService;
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

    @GetMapping("/invt-request")
    public String viewReqPage(@AuthenticationPrincipal UserDetails loggedUser, Model model)
    {

        String email= loggedUser.getUsername();

        User logged=userRepository.findByEmailId(email);
        List<Inventory> inventoryList=cardItemService.getAvailableInventories(logged);
        //List<Inventory> inventoryList=inventoryRepository.findAll();
        model.addAttribute("inventoryList",inventoryList);
        return "invt-req-page";
    }

    @GetMapping("/invt-request/cart")
    public String showCard(@AuthenticationPrincipal UserDetails loggedUser,Model model,RedirectAttributes redirectAttributes)
    {
        String email= loggedUser.getUsername();
        User logged=userRepository.findByEmailId(email);
        RequestApprove requestApprove=new RequestApprove();
        List<CardItem> cardItemList=cardItemService.listCardItems(logged);
        requestApprove.setCardItems(cardItemList);
        if(cardItemList.size()==0 ||cardItemList==null)
        {
            redirectAttributes.addFlashAttribute("message","Cart is empty");
            return "redirect:/invt-request";
        }
        model.addAttribute("cardItemList",cardItemList);
        model.addAttribute("requestApprove",requestApprove);
        return "view-cart";
    }

    @GetMapping("/invt-request/add/{id}")
    public String addProductToCard(@PathVariable(name="id") Long id,@AuthenticationPrincipal UserDetails loggedUser,RedirectAttributes redirectAttributes )
    {
        String email= loggedUser.getUsername();
        User logged=userRepository.findByEmailId(email);
        try {
            Inventory inventory=cardItemService.addInventory(id,logged);
            redirectAttributes.addFlashAttribute("message",inventory.getName()+" added to the cart");
            return "redirect:/invt-request";
        } catch (InventoryAlreadyExistsException | CannotAddInventoryException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/invt-request";
        }

    }
    @GetMapping("/invt-request/cart/delete/{id}")
    public String removeProductFromCard(@PathVariable(name="id") Long id,@AuthenticationPrincipal UserDetails loggedUser,RedirectAttributes redirectAttributes)
    {
        String email= loggedUser.getUsername();
        User logged=userRepository.findByEmailId(email);
        cardItemService.removeInventory(id,logged);
        return "redirect:/invt-request/cart";
    }

    @PostMapping("/invt-request/cart/submit")
    public String requestInventories(@AuthenticationPrincipal UserDetails loggedUser, RequestApprove requestApprove,Model model)
    {
        String email= loggedUser.getUsername();
        User logged=userRepository.findByEmailId(email);
        List<CardItem> items=cardItemService.listCardItems(logged);
        RequestApprove req_app=requestApproveService.saveRequest(logged,requestApprove,items);
        return "redirect:/invt-request";
    }
    @GetMapping("/invt-request/history")
    public String requestHistory(@AuthenticationPrincipal UserDetails loggedUser,Model model)
    {
        String email= loggedUser.getUsername();
        User logged=userRepository.findByEmailId(email);
        List<RequestApprove> itemsList=cardItemService.listCardItemsWithRequestApprove(logged);
        model.addAttribute("itemsList",itemsList);
        return "req-history";
    }
}

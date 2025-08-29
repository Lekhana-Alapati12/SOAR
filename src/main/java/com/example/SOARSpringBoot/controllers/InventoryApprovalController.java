package com.example.SOARSpringBoot.controllers;

import com.example.SOARSpringBoot.entity.RequestApprove;
import com.example.SOARSpringBoot.repository.RequestApproveRepository;
import com.example.SOARSpringBoot.service.RequestApproveService;
import com.example.SOARSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class InventoryApprovalController {

    @Autowired
    private RequestApproveRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestApproveService service;
    @GetMapping("/req-approval")
    public String viewReqPage(Model model)
    {
        List<RequestApprove> totalList=service.findRequestsWithNoStatus();
        model.addAttribute("totalList",totalList);
        return "req-approval-page";
    }

    @GetMapping("/req-approval/approve/{id}")
    public String approveRequest(@PathVariable(name="id") Long id,Model model)
    {
        RequestApprove requestApprove=repository.findById(id).get();
        model.addAttribute("requestApprove",requestApprove);
        model.addAttribute("title","Approve Request");
        model.addAttribute("submit","/req-approval/approve/");
        model.addAttribute("cancel","/req-approval");
        return "req-approve-process";
    }

    @PostMapping("/req-approval/approve/{id}")
    public String approveRequestProcess(@PathVariable(name="id") Long id,RequestApprove requestApprove,Model model)
    {
        RequestApprove reqApp=service.saveRequestApprove(requestApprove,id);
        return "redirect:/req-approval";

    }

    @GetMapping("/req-approval/reject/{id}")
    public String rejectRequest(@PathVariable(name="id") Long id,Model model)
    {
        RequestApprove requestApprove=repository.findById(id).get();
        model.addAttribute("requestApprove",requestApprove);
        model.addAttribute("title","Reject Request");
        model.addAttribute("submit","/req-approval/reject/");
        model.addAttribute("cancel","/req-approval");
        return "req-approve-process";
    }
    @PostMapping("/req-approval/reject/{id}")
    public String rejectRequestProcess(@PathVariable(name="id") Long id,RequestApprove requestApprove,Model model)
    {
        RequestApprove reqApp=service.saveRequestReject(requestApprove,id);
        return "redirect:/req-approval";
    }

    @GetMapping("/req-approval/sort/date")
    public String sortRequestsByDate(Model model)
    {
        List<RequestApprove> totalList=repository.findByStatusOrderByDateAsc("Pending");
        model.addAttribute("totalList",totalList);
        return "req-approval-page";
    }
    @GetMapping("/req-approval/sort/cost")
    public String sortRequestsByCost(Model model)
    {
        List<RequestApprove> totalList=repository.findByStatusOrderByTotalCostAsc("pending");
        model.addAttribute("totalList",totalList);
        return "req-approval-page";
    }

    @GetMapping("/req-approval/sort/count")
    public String sortRequestsByCount(Model model)
    {
        List<RequestApprove> totalList=repository.findByStatusOrderByTotalItemsAsc("Pending");
        model.addAttribute("totalList",totalList);
        return "req-approval-page";
    }
    @GetMapping("/req-approval/history")
    public String requestHistory(Model model)
    {
        List<RequestApprove> list=service.requestHistory();
        Integer approveCnt=service.findApprovalCnt();
        Integer rejectCnt= service.findByRejectCnt();
        Long approveCost= service.findApprovalCost();
        Long rejectCost= service.findRejectedCost();
        model.addAttribute("list",list);
        model.addAttribute("approveCnt",approveCnt);
        model.addAttribute("rejectCnt",rejectCnt);
        model.addAttribute("approveCost",approveCost);
        model.addAttribute("rejectCost",rejectCost);
        return "approve-history";
    }

}

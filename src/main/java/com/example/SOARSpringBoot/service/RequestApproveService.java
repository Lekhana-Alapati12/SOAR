package com.example.SOARSpringBoot.service;

import com.example.SOARSpringBoot.entity.CardItem;
import com.example.SOARSpringBoot.entity.RequestApprove;
import com.example.SOARSpringBoot.entity.User;
import com.example.SOARSpringBoot.repository.RequestApproveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RequestApproveService {
    @Autowired
    private RequestApproveRepository requestApproveRepository;


    public RequestApprove saveRequest(User user, RequestApprove requestApprove, List<CardItem> items)
    {
        long sum=0;
        requestApprove.setDate(new Date());
        requestApprove.setStatus("Pending");
        for(CardItem item:items)
        {
            sum=sum+item.getInventory().getCost();
            item.setRequestApprove(requestApprove);
        }
        requestApprove.setCardItems(items);
        requestApprove.setUser(user);
        requestApprove.setTotalItems(items.size());
        requestApprove.setTotalCost(sum);
        RequestApprove req=requestApproveRepository.save(requestApprove);
        return  req;
    }

    public List<RequestApprove> findRequestsWithNoStatus()
    {
        return requestApproveRepository.findByStatus("Pending");
    }

    public RequestApprove saveRequestApprove(RequestApprove requestApprove,Long id)
    {
        RequestApprove reqApp=requestApproveRepository.findById(id).get();
        if(requestApprove.getRemarks_manager()!=null || !requestApprove.getRemarks_manager().isEmpty()) {
            reqApp.setRemarks_manager(requestApprove.getRemarks_manager());
        }
        boolean b = true;
        for (CardItem c : reqApp.getCardItems()) {
            if (c.getInventory().getCount() > 0) {
                c.getInventory().setCount(c.getInventory().getCount() - 1);
            } else {
                b = false;
                break;
            }

        }
        if (b) {
            reqApp.setStatus("Approved");
        } else {
            reqApp.setRemarks_manager("Out Of Stock");
            reqApp.setStatus("Rejected");
        }
        RequestApprove requestApprove1=requestApproveRepository.save(reqApp);
        return requestApprove1;
    }

    public RequestApprove saveRequestReject(RequestApprove requestApprove,Long id)
    {
        RequestApprove reqApp=requestApproveRepository.findById(id).get();
        if(requestApprove.getRemarks_manager()!=null || !requestApprove.getRemarks_manager().isEmpty()) {
            reqApp.setRemarks_manager(requestApprove.getRemarks_manager());
        }
        reqApp.setStatus("Rejected");
        RequestApprove requestApprove1=requestApproveRepository.save(reqApp);
        return requestApprove1;
    }

    public List<RequestApprove> requestHistory()
    {
        List<RequestApprove> list=requestApproveRepository.findAll();
        return list;
    }
    public Integer findApprovalCnt()
    {
        return requestApproveRepository.findByStatus("Approved").size();
    }
    public Integer findByRejectCnt()
    {
        return requestApproveRepository.findByStatus("Rejected").size();
    }
    public Long findApprovalCost()
    {
        List<RequestApprove> list=requestApproveRepository.findByStatus("Approved");
        long cost=0;
        for(RequestApprove req:list)
        {
            cost+=req.getTotalCost();
        }
        return cost;
    }
    public Long findRejectedCost()
    {
        List<RequestApprove> list=requestApproveRepository.findByStatus("Rejected");
        long cost=0;
        for(RequestApprove req:list)
        {
            cost+=req.getTotalCost();
        }
        return cost;
    }

}

package com.example.SOARSpringBoot.repository;

import com.example.SOARSpringBoot.entity.RequestApprove;
import com.example.SOARSpringBoot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestApproveRepository extends JpaRepository<RequestApprove,Long> {

    //List<RequestApprove> findByCardItems(List<CardItem> cardItems);
   // List<RequestApprove> findById(List<Long> ids);
    List<RequestApprove> findByUser(User user);
    List<RequestApprove> findByStatus(String status);
    List<RequestApprove> findByStatusOrderByDateAsc(String status);
    List<RequestApprove> findByStatusOrderByTotalCostAsc(String status);
    List<RequestApprove> findByStatusOrderByTotalItemsAsc(String status);
    List<RequestApprove> findByUserAndStatus(User user,String status);
}

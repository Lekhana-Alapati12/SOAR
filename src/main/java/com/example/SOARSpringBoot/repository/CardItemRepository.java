package com.example.SOARSpringBoot.repository;

import com.example.SOARSpringBoot.entity.CardItem;
import com.example.SOARSpringBoot.entity.Inventory;
import com.example.SOARSpringBoot.entity.RequestApprove;
import com.example.SOARSpringBoot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CardItemRepository extends JpaRepository<CardItem,Long> {

    public List<CardItem> findByUser(User user);

    public CardItem findByUserAndInventoryAndRequestApprove(User user, Inventory inventory, RequestApprove requestApprove);

    public CardItem findByUserAndInventory(User user,Inventory inventory);

    public List<CardItem> findByUserAndRequestApprove(User user,RequestApprove requestApprove);

    public List<CardItem> findByUserAndRequestApproveIsNotNull(User user);

    @Query("DELETE FROM CardItem c WHERE c.user.id = ?1 AND c.inventory.id = ?2")
    @Modifying
    public void deleteByUserAndInventory(Long userId,Long inventoryId);
}

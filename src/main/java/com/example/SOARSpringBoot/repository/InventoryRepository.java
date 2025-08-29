package com.example.SOARSpringBoot.repository;

import com.example.SOARSpringBoot.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    public Inventory findByName(String name);
}

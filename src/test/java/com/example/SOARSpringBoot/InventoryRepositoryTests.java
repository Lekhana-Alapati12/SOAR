package com.example.SOARSpringBoot;

import com.example.SOARSpringBoot.entity.Inventory;
import com.example.SOARSpringBoot.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class InventoryRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InventoryRepository inventoryRepository;


    @Test
    public void testCreateInventory()
    {
        Inventory inventory=new Inventory();
        inventory.setName("LenovaMouse");
        inventory.setType("Mouse");
        inventory.setCost(850l);
        inventory.setCount(18);
        Inventory savedInventory=inventoryRepository.save(inventory);
        Inventory existsInventory=entityManager.find(Inventory.class,savedInventory.getId());
        assertEquals(inventory.getName(),existsInventory.getName());
    }
}

package com.example.SOARSpringBoot;

import com.example.SOARSpringBoot.entity.CardItem;
import com.example.SOARSpringBoot.entity.Inventory;
import com.example.SOARSpringBoot.entity.User;
import com.example.SOARSpringBoot.repository.CardItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ShoppingCardTests {
    @Autowired
    private CardItemRepository cardItemRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testAddOnecardItem()
    {
       Inventory inventory= entityManager.find(Inventory.class,3l);
       User user=entityManager.find(User.class,11l);
       CardItem item=new CardItem();
       item.setInventory(inventory);
       item.setUser(user);
       item.setQuantity(1);
       CardItem cardItem=cardItemRepository.save(item);
        assertTrue(cardItem.getId()>0);
    }

    @Test
    public void testGetCardItemsByUser()
    {
        User user=entityManager.find(User.class,11l);
        List<CardItem> itemList=cardItemRepository.findByUser(user);
        assertEquals(2,itemList.size());
    }

    @Test
    public void testGetCardItemByUserAndInventory()
    {
        Inventory inventory= entityManager.find(Inventory.class,3l);
        User user=entityManager.find(User.class,11l);
        CardItem cardItem= cardItemRepository.findByUserAndInventory(user,inventory);
        assertTrue(cardItem.getId()==2);
    }

    @Test
    public void testDeleteCardByUserAndInventory()
    {

    }
}

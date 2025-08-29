package com.example.SOARSpringBoot.service;

import com.example.SOARSpringBoot.entity.CardItem;
import com.example.SOARSpringBoot.entity.Inventory;
import com.example.SOARSpringBoot.entity.RequestApprove;
import com.example.SOARSpringBoot.entity.User;
import com.example.SOARSpringBoot.exception.CannotAddInventoryException;
import com.example.SOARSpringBoot.exception.InventoryAlreadyExistsException;
import com.example.SOARSpringBoot.repository.CardItemRepository;
import com.example.SOARSpringBoot.repository.InventoryRepository;
import com.example.SOARSpringBoot.repository.RequestApproveRepository;
import com.example.SOARSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardItemService {

    @Autowired
    private CardItemRepository cardItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestApproveRepository requestApproveRepository;



    public List<CardItem> listCardItems(User user)
    {
        return cardItemRepository.findByUserAndRequestApprove(user,null);
    }

    public List<CardItem> listUserCardItems(User user)
    {
        return cardItemRepository.findByUser(user);
    }

    public List<RequestApprove> listCardItemsWithRequestApprove(User user)
    {
//        List<CardItem> list=cardItemRepository.findByUserAndRequestApproveIsNotNull(user);
//        List<RequestApprove> reqlist=new ArrayList<RequestApprove>();
//        List<Long> ids=new ArrayList<Long>();
//        for(CardItem c:list)
//        {
//            Long l=c.getRequestApprove().getId();
//            if(!ids.contains(l))
//            {
//                ids.add(c.getRequestApprove().getId());
//                reqlist.add(requestApproveRepository.findById(l).get());
//            }
//        }
        return requestApproveRepository.findByUser(user);

    }

    private boolean isTobeAdded(Inventory toBeAdded, User user)
    {
       // Inventory toBeAdded= cardItem.getInventory();
        List<CardItem> cardItemList=listUserCardItems(user);
        for(CardItem item:cardItemList)
        {
            Inventory inventory=item.getInventory();
            if(toBeAdded.getType().equalsIgnoreCase(inventory.getType()))
            {
                return false;
            }
            else if((toBeAdded.getType().equalsIgnoreCase("Desktop"))
                    && ((inventory.getType().equalsIgnoreCase("Laptop"))
                    ||(inventory.getType().equalsIgnoreCase("Docking port"))))
            {
                return false;
            }
            else if((toBeAdded.getType().equalsIgnoreCase("Laptop")) &&
                    (inventory.getType().equalsIgnoreCase("Desktop")))
            {
                return false;
            }
            else if((toBeAdded.getType().equalsIgnoreCase("Docking port"))
                    && (inventory.getType().equalsIgnoreCase("Desktop")))
            {
                return false;
            }
        }
        return true;
    }
    public Inventory addInventory(Long id, User user) throws InventoryAlreadyExistsException, CannotAddInventoryException {
        Inventory inventory=inventoryRepository.findById(id).get();
        CardItem card=cardItemRepository.findByUserAndInventory(user,inventory);
        if(card==null)
        {
            if(isTobeAdded(inventory,user))
            {
                card=new CardItem();
                card.setUser(user);
                card.setInventory(inventory);
                card.setQuantity(1);
                cardItemRepository.save(card);
                return inventory;
            }
            else
            {
                throw new CannotAddInventoryException("Inventory Cannot be added");
            }
        }
        else
        {
            throw new InventoryAlreadyExistsException("Inventory Cannot be added to the cart");
        }

    }

    public void removeInventory(Long inventoryId,User user)
    {
        cardItemRepository.deleteByUserAndInventory(user.getId(),inventoryId);
    }
    public List<Inventory> getAvailableInventories(User user)
    {
        List<Inventory> list=new ArrayList<>();
        List<Inventory> inventories=inventoryRepository.findAll();
        for(Inventory in:inventories)
        {
            CardItem cardItem=cardItemRepository.findByUserAndInventory(user,in);
            if(cardItem==null)
            {
                list.add(in);
            }

        }
//        List<RequestApprove> requestApproveList=requestApproveRepository.findByUserAndStatus(user,"Rejected");
//        for(RequestApprove requestApprove:requestApproveList)
//        {
//            for(CardItem cardItem:requestApprove.getCardItems())
//            {
//                list.add(cardItem.getInventory());
//            }
//        }
        return list;
    }
}

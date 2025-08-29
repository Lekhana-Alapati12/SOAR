package com.example.SOARSpringBoot.service;

import com.example.SOARSpringBoot.entity.Inventory;
import com.example.SOARSpringBoot.exception.InventoryAlreadyExistsException;
import com.example.SOARSpringBoot.exception.InventoryNotFoundException;
import com.example.SOARSpringBoot.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getInventoryList()
    {
        return inventoryRepository.findAll();
    }

    public Inventory getInventory(Long id) throws InventoryNotFoundException {
        try
        {
            return inventoryRepository.findById(id).get();
        }
        catch(NoSuchElementException ex)
        {
            throw new InventoryNotFoundException("Cound not find user with Id"+id);
        }
    }
    public boolean isNameUnique(Long id,String name)
    {
        Inventory inventoryByName=inventoryRepository.findByName(name);
        if(inventoryByName==null)
            return true;
        boolean isCreatingNew=(id==null);
        if(isCreatingNew)
        {
            if(inventoryByName!=null) return false;
        }
        else
        {
            if(inventoryByName.getId()!=id)
            {
                return false;
            }
        }
        return true;
    }

    public Inventory updateInventory(Inventory inventoryInForm,Inventory ori) throws InventoryAlreadyExistsException {
        Inventory inventory=inventoryRepository.findById(ori.getId()).get();
        if (isNameUnique(ori.getId(), inventoryInForm.getName()))
        {
            inventory.setName(inventoryInForm.getName());
        }
        else {
            throw new InventoryAlreadyExistsException("Inventory with given name already exists");
        }
        if(!ori.getType().equalsIgnoreCase(inventoryInForm.getType()))
        {
                inventory.setType(inventoryInForm.getType());
        }
        if(inventoryInForm.getCost()!= ori.getCost())
        {
            if(inventoryInForm.getCost()>0 && inventoryInForm.getCost()<=999999)
            {
                inventory.setCost(inventoryInForm.getCost());
            }
        }
        if(inventoryInForm.getCount()!= ori.getCount())
        {
            if(inventoryInForm.getCount()>0)
            {
                inventory.setCount(inventoryInForm.getCount());
            }
        }
        return inventory;
    }
    public Inventory addInventory(Inventory inventory) throws InventoryAlreadyExistsException
    {
        if(isNameUnique(null,inventory.getName()))
        {
            Inventory res=inventoryRepository.save(inventory);
            return res;
        }
        else
        {
            throw new InventoryAlreadyExistsException("Inventory with given name already exists");
        }

    }

}

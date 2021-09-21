package com.ucproject.lists.services;

import com.ucproject.lists.entity.Item;
import com.ucproject.lists.repository.ItemRepository;
import com.ucproject.lists.repository.ListsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    ListsRepository listsRepo;

    @Autowired
    ItemRepository itemRepo;

    public void editItem(Long id, String name){
        Item editedItem = itemRepo.findById(id).get();

        editedItem.setName(name);
        itemRepo.save(editedItem);
    }
}

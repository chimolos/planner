package com.ucproject.lists.controllers;

import com.ucproject.lists.entity.Item;
import com.ucproject.lists.entity.Lists;
import com.ucproject.lists.repository.ItemRepository;
import com.ucproject.lists.services.ItemService;
import com.ucproject.lists.services.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    ListService listService;

    @Autowired
    ItemRepository itemRepo;

    @PutMapping("/item/{id}/edit")
    public String editItem(@PathVariable Long id, @RequestParam("name") String name) {
        itemService.editItem(id, name);
        return "Item successfully edited";
    }

    @Transactional
    @DeleteMapping("/{listId}/item/{id}/delete")
    public void deleteItem(@PathVariable Long listId, @PathVariable Long id) {
        Lists editedList = listService.getListPerId(listId);
        Item item = itemRepo.findByListAndId(editedList, id);
        editedList.getItem().remove(item);
        itemRepo.delete(item);
    }
}

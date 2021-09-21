package com.ucproject.lists.services;

import com.ucproject.lists.entity.Item;
import com.ucproject.lists.entity.Lists;
import com.ucproject.lists.repository.ItemRepository;
import com.ucproject.lists.repository.ListsRepository;
import com.ucproject.lists.request.ListRequest;
import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListService {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    ListsRepository listsRepo;

    @Autowired
    ItemRepository itemRepo;

    public void addList(ListRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Lists add = new Lists();
        add.setUser(user);
        add.setTitle(request.getTitle());
        add.setDescription(request.getDescription());

        List<String> items = request.getItem();
        List<Item> item = new ArrayList<>();
        items.forEach(name -> {
            Item i = new Item();
            i.setName(name);
//            itemRepo.save(i);
            item.add(i);
        });

        add.getItem().addAll(item);
        listsRepo.save(add);
    }

    public void updateList(Long id, ListRequest request) {
        Lists editedList = getListPerId(id);

        editedList.setTitle(request.getTitle());
        editedList.setDescription(request.getDescription());
        List<String> items = request.getItem();
        List<Item> item = new ArrayList<>();
        items.forEach(name -> {
            List<String> l = editedList.getItem().stream().map(Item::getName).collect(Collectors.toList());
            if (!l.contains(name)) {
                Item i = new Item();
                i.setName(name);
                i.setList(editedList);
//                itemRepo.save(i);
                item.add(i);
            } else {
                Item item1 = itemRepo.findByListIdAndName(id, name);
                item.add(item1);
            }
        });

        editedList.setItem(item);
        listsRepo.save(editedList);
    }

    public List<Lists> getAllListsPerUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return listsRepo.findAllByUser(user);
    }

    public Lists getListPerId(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        return listsRepo.findByIdAndUser(id, user);
    }


}

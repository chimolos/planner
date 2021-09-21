package com.ucproject.lists.controllers;

import com.ucproject.lists.entity.Lists;
import com.ucproject.lists.repository.ListsRepository;
import com.ucproject.lists.request.ListRequest;
import com.ucproject.lists.services.ListService;
import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
public class ListController {

    @Autowired
    ListService listService;

    @Autowired
    ListsRepository listsRepo;

    @Autowired
    UsersRepository usersRepo;

    @PostMapping("/addList")
    public String addList(@RequestBody ListRequest request) {
        listService.addList(request);
        return "List added successfully";
    }

    @GetMapping("/lists")
    public List<Lists> getAllListsPerUser() {
        return listService.getAllListsPerUser();
    }

    @GetMapping("/list/{id}")
    public Lists getListById(@PathVariable Long id) {
        return listService.getListPerId(id);
    }

    @PutMapping("/list/{id}/edit")
    public String editList(@PathVariable Long id, @RequestBody ListRequest request) {
        listService.updateList(id, request);
        return "List successfully updated";
    }

    @Transactional
    @DeleteMapping("/list/{id}/delete")
    public void deleteList(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        listsRepo.deleteByUserAndId(user, id);
    }
}

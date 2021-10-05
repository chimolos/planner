package com.ucproject.lists.services;

import com.ucproject.family.Family;
import com.ucproject.family.FamilyRepository;
import com.ucproject.groups.Group;
import com.ucproject.groups.GroupRepository;
import com.ucproject.lists.entity.Item;
import com.ucproject.lists.entity.Lists;
import com.ucproject.lists.repository.ItemRepository;
import com.ucproject.lists.repository.ListsRepository;
import com.ucproject.lists.request.ListRequest;
import com.ucproject.teams.Team;
import com.ucproject.teams.TeamRepository;
import com.ucproject.users.models.Users;
import com.ucproject.users.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ListService {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    TeamRepository teamRepo;

    @Autowired
    GroupRepository groupRepo;

    @Autowired
    FamilyRepository familyRepo;

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

    public void shareList(Long id, String classify, String name) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersRepo.findByUsername(username);

        Lists sharedList = getListPerId(id);

        switch (classify) {
            case "user":
                Users users = usersRepo.findByEmail(name);
                if (users == null) {
                    log.error("User not found in the database");
                    throw new UsernameNotFoundException("User not found in the database");
                }
                users.getSharedLists().add(sharedList);
                usersRepo.save(users);

                break;
            case "team":
                Team team = teamRepo.findByNameAndUser(name, user);
                if (team == null) {
                    log.error("Team not found in the database");
                    throw new UsernameNotFoundException("Team not found in the database");
                }
                team.getSharedLists().add(sharedList);
                teamRepo.save(team);

                break;
            case "group":
                Group group = groupRepo.findByNameAndUser(name, user);
                if (group == null) {
                    log.error("Group not found in the database");
                    throw new UsernameNotFoundException("Group not found in the database");
                }
                group.getSharedLists().add(sharedList);
                groupRepo.save(group);

                break;
            case "family":
                Family family = familyRepo.findByNameAndUser(name, user);
                if (family == null) {
                    log.error("Family not found in the database");
                    throw new UsernameNotFoundException("Family not found in the database");
                }
                family.getSharedLists().add(sharedList);
                familyRepo.save(family);

                break;
        }

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

package com.ucproject.lists.repository;

import com.ucproject.goals.entity.Goals;
import com.ucproject.goals.entity.Steps;
import com.ucproject.lists.entity.Item;
import com.ucproject.lists.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByListIdAndName(Long listId, String name);

    Item findByListAndId(Lists lists, Long id);

}

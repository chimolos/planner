package com.ucproject.lists.repository;

import com.ucproject.lists.entity.Lists;
import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ListsRepository extends JpaRepository<Lists, Long> {

    List<Lists> findAllByUser(Users user);
    Lists findByIdAndUser(Long id, Users user);
    void deleteByUserAndId(Users users, Long id);

}

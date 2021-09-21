package com.ucproject.outlines;

import com.ucproject.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Activity findByUserAndId(Users users, Long id);

    List<Activity> findAllByUserAndDayIgnoreCase(Users users, String day);

    void deleteByUserAndId(Users users, Long id);
}

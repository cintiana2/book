package com.singular.book.repository;

import com.singular.book.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    boolean existsByLogin(String login);

    Optional<UserApp> findByLogin(String login);
}
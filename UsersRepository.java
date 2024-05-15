package com.example.pcRoom.repository;

import com.example.pcRoom.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUsername(String currentUserName);

//    Optional<Object> findById();

    Users findByUserNo(Long userNo);
}

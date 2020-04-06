package com.uxpsystems.assignment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uxpsystems.assignment.model.UserDetail;

@Repository
public interface UserRepository
        extends JpaRepository<UserDetail, Long> {
 
}

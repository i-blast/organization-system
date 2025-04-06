package com.pii.user_service.repo;

import com.pii.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByCompanyId(Long companyId);

}

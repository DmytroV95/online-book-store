package com.varukha.onlinebookstore.repository.role;

import com.varukha.onlinebookstore.model.Role;
import com.varukha.onlinebookstore.model.Role.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}

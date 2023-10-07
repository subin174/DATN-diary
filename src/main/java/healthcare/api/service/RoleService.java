package healthcare.api.service;

import healthcare.entity.Role;
import healthcare.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService  {
    final RoleRepository roleRepository;

}

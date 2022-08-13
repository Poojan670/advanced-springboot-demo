package advanced.com.demo.springboot.backend.user.service;

import advanced.com.demo.springboot.backend.exception.CustomNotFoundException;
import advanced.com.demo.springboot.backend.helper.CustomOffsetPagination;
import advanced.com.demo.springboot.backend.user.model.Role;
import advanced.com.demo.springboot.backend.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;
    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new CustomNotFoundException("Role with this name: " + name + " not found"));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Page<Role> getRoles(Integer offset, Integer limit, String sortBy, String search) {
        if(search==null || search.equals("")){
            Pageable pageable;
            if (sortBy.startsWith("-")){
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy.substring(1)).descending());
            }else{
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy));
            }
            return roleRepository.findAll(pageable);
        }else{
            Pageable pageable;
            if (sortBy.startsWith("-")){
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy.substring(1)).descending());
            }else{
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy));
            }
            return roleRepository.search(search, pageable);
        }
    }

}

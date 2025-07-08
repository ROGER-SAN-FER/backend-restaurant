package backend_restaurant.security.service;

import backend_restaurant.security.dto.UserRegisterDTO;
import backend_restaurant.security.model.RoleEntity;
import backend_restaurant.security.model.RoleEnum;
import backend_restaurant.security.model.UserEntity;
import backend_restaurant.security.repository.RoleRepository;
import backend_restaurant.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRolesList().forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
        );

        userEntity.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));


        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNonExpired(),
                userEntity.isCredentialsNonExpired(),
                userEntity.isAccountNonLocked(),
                authorityList
        );
    }


    public UserEntity registerNewUser(UserRegisterDTO dto) {

        RoleEnum roleEnum;
        try {
            roleEnum = RoleEnum.valueOf(dto.getRole().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Rol \"" + dto.getRole() + "\" no existe como enum");
        }

        RoleEntity roleEntity = roleRepository
                .findByRoleEnum(roleEnum)
                .orElseThrow(() -> new RuntimeException("Rol \"" + roleEnum + "\" no encontrado en la base de datos"));

        UserEntity newUser = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .rolesList(Set.of(roleEntity))
                .build();

        return userRepository.save(newUser);
    }
}

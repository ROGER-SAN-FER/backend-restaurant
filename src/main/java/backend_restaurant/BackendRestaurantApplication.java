package backend_restaurant;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class BackendRestaurantApplication {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(BackendRestaurantApplication.class, args);
    }

    @PreDestroy
    public void close() throws Exception {
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            hikariDataSource.close(); // Cierra conexiones al apagar el app
        }
    }

//    @Bean
//    CommandLineRunner init(UserRepository userRepository) {
//        return args -> {
//            /* Create PERMISSIONS */
//            PermissionEntity createPermission = PermissionEntity.builder()
//                    .name("CREATE")
//                    .build();
//
//            PermissionEntity readPermission = PermissionEntity.builder()
//                    .name("READ")
//                    .build();
//
//            PermissionEntity updatePermission = PermissionEntity.builder()
//                    .name("UPDATE")
//                    .build();
//
//            PermissionEntity deletePermission = PermissionEntity.builder()
//                    .name("DELETE")
//                    .build();
//
//            PermissionEntity refactorPermission = PermissionEntity.builder()
//                    .name("REFACTOR")
//                    .build();
//
//            /* Create ROLES */
//            RoleEntity roleAdmin = RoleEntity.builder()
//                    .roleEnum(RoleEnum.ADMIN)
//                    .permissionsList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
//                    .build();
//
//            RoleEntity roleUser = RoleEntity.builder()
//                    .roleEnum(RoleEnum.USER)
//                    .permissionsList(Set.of(createPermission, readPermission, refactorPermission))
//                    .build();
//
//            RoleEntity roleInvited = RoleEntity.builder()
//                    .roleEnum(RoleEnum.INVITED)
//                    .permissionsList(Set.of(readPermission))
//                    .build();
//
//            RoleEntity roleDeveloper = RoleEntity.builder()
//                    .roleEnum(RoleEnum.DEVELOPER)
//                    .permissionsList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
//                    .build();
//
//            /* CREATE USERS */
//            UserEntity userRoger = UserEntity.builder()
//                    .username("admin")
//                    .password("$2a$10$zxz57PNKQetdgmh44vDBHuXNMj4YL7/Zd0NKu/288tynVBinL1FGm")
//                    .enabled(true)
//                    .accountNonExpired(true)
//                    .accountNonLocked(true)
//                    .credentialsNonExpired(true)
//                    .rolesList(Set.of(roleAdmin))
//                    .build();
//
//
//            userRepository.saveAll(List.of(userRoger));
//        };
//    }
}

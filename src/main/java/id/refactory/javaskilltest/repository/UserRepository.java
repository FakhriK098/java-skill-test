package id.refactory.javaskilltest.repository;

import id.refactory.javaskilltest.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity getUserByUsername(String username);
}

package id.refactory.javaskilltest.repository;

import id.refactory.javaskilltest.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {
    UserData getUserByUsername(String username);
}

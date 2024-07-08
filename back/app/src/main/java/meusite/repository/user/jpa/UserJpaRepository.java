package meusite.repository.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByPassword(String password);
    Optional<UserJpaEntity> findByEmailAndPassword(String email, String password);
}

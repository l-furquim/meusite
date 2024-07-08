package meusite.repository.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByPassword(String password);
    Optional<UserJpaEntity> findByEmailAndPassword(String email, String password);

    @Transactional
    @Modifying
    @Query("UPDATE UserJpaEntity e SET e.password = :novaSenha WHERE e.email = :email")
    void changeUserPassword(@Param("email") String email, @Param("novaSenha") String novaSenha);

}

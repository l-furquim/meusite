package meusite.repository.verifier.jpa;

import meusite.repository.user.jpa.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserVerifierRepository extends JpaRepository<UserVerifierJpaEntity,Long> {

    @Query("SELECT uv FROM UserVerifierJpaEntity uv WHERE uv.code = :code")
    Optional<UserVerifierJpaEntity> getUserVerifierByCode(@Param("code") String code);

    @Query("SELECT uv FROM UserVerifierJpaEntity uv WHERE uv.userEmail = :email")
    Optional<UserVerifierJpaEntity> getUserVerifierByUserEmail(@Param("email")String email);

}

package meusite.domain.gateway;

import meusite.repository.verifier.jpa.UserVerifierJpaEntity;

import java.util.Optional;

public interface UserVerifierGateway {

    Optional<UserVerifierJpaEntity> getUserVerifierByCode(String code);

    Optional<UserVerifierJpaEntity> getUserVerifierByUserEmail(String email);

    void delete(UserVerifierJpaEntity userVerifierJpaEntity);

    void save(UserVerifierJpaEntity userVerifierJpaEntity);

}

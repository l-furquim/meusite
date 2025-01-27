package meusite.service.verifier;

import meusite.repository.verifier.jpa.UserVerifierJpaEntity;

import java.util.Optional;

public interface UserVerifierService {

    void delete(UserVerifierJpaEntity userVerifierJpaEntity);

    Optional<UserVerifierJpaEntity> getUserVerifierByCode(String code);

    Optional<UserVerifierJpaEntity> getUserVerifierByUserEmail(String email);

    void save(UserVerifierJpaEntity userVerifierJpa);
}

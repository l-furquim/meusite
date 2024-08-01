package meusite.repository.verifier;

import meusite.domain.gateway.UserVerifierGateway;
import meusite.repository.verifier.jpa.UserVerifierJpaEntity;
import meusite.repository.verifier.jpa.UserVerifierRepository;

import java.util.Optional;

public class UserVerifierJpaGateway implements UserVerifierGateway {

    private UserVerifierRepository userVerifierRepository;

    private UserVerifierJpaGateway(UserVerifierRepository userVerifierRepository){
        this.userVerifierRepository = userVerifierRepository;
    }
    public static UserVerifierJpaGateway build(UserVerifierRepository userVerifierRepository){
        return new UserVerifierJpaGateway(
                userVerifierRepository
        );
    }


    @Override
    public Optional<UserVerifierJpaEntity> getUserVerifierByCode(String code) {
         return userVerifierRepository.getUserVerifierByCode(code);
    }

    @Override
    public Optional<UserVerifierJpaEntity> getUserVerifierByUserEmail(String email) {
        return userVerifierRepository.getUserVerifierByUserEmail(email);
    }

    @Override
    public void delete(UserVerifierJpaEntity userVerifierJpaEntity) {
            userVerifierRepository.delete(userVerifierJpaEntity);
    }

    @Override
    public void save(UserVerifierJpaEntity userVerifierJpaEntity) {
        userVerifierRepository.save(userVerifierJpaEntity);
    }
}

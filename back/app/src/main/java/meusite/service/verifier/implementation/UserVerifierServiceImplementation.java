package meusite.service.verifier.implementation;

import meusite.repository.verifier.UserVerifierJpaGateway;
import meusite.repository.verifier.jpa.UserVerifierJpaEntity;
import meusite.service.verifier.UserVerifierService;

import java.util.Optional;


public class UserVerifierServiceImplementation implements UserVerifierService {


    private UserVerifierJpaGateway userVerifierJpaGateway;

    private UserVerifierServiceImplementation(UserVerifierJpaGateway userVerifierJpaGateway){
        this.userVerifierJpaGateway = userVerifierJpaGateway;
    }

    public static UserVerifierServiceImplementation build( UserVerifierJpaGateway userVerifierJpaGateway){
        return new UserVerifierServiceImplementation(
                userVerifierJpaGateway
        );
    }

    public void delete(UserVerifierJpaEntity userVerifierJpaEntity){
        this.userVerifierJpaGateway.delete(userVerifierJpaEntity);
    }

    public Optional<UserVerifierJpaEntity> getUserVerifierByCode(String code){
        return this.userVerifierJpaGateway.getUserVerifierByCode(code);
    }

    public Optional<UserVerifierJpaEntity> getUserVerifierByUserEmail(String email){
        return this.userVerifierJpaGateway.getUserVerifierByUserEmail(email);
    }


    public void save(UserVerifierJpaEntity userVerifierJpa){
        this.userVerifierJpaGateway.save(userVerifierJpa);
    }

}

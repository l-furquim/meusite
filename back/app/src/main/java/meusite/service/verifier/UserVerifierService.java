package meusite.service.verifier;

import meusite.repository.verifier.jpa.UserVerifierJpaEntity;
import meusite.repository.verifier.jpa.UserVerifierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserVerifierService {

    @Autowired
    UserVerifierRepository userVerifierRepository;


    public void delete(UserVerifierJpaEntity userVerifierJpaEntity){
        this.userVerifierRepository.delete(userVerifierJpaEntity);
    }

    public Optional<UserVerifierJpaEntity> getUserVerifierByCode(String code){
        return this.userVerifierRepository.getUserVerifierByCode(code);
    }

    public Optional<UserVerifierJpaEntity> getUserVerifierByUserEmail(String email){
        return this.userVerifierRepository.getUserVerifierByUserEmail(email);
    }


    public void save(UserVerifierJpaEntity userVerifierJpa){
        this.userVerifierRepository.save(userVerifierJpa);
    }

}

package meusite.domain.gateway;

import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.coments.jpa.ComentsJpaRepository;

import java.util.Optional;


public interface ComentsGateway {

    void save(ComentsJpaEntity comentsJpaEntity);

    void updateLikes(Integer likes, Long comentId);

    Optional<ComentsJpaEntity> findById(Long id);

}

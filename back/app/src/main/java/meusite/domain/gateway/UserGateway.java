package meusite.domain.gateway;

import meusite.repository.user.jpa.UserJpaEntity;

import java.util.Optional;

public interface UserGateway {

    void save(UserJpaEntity userJpaEntity);

    Optional<UserJpaEntity> findByPassword(String password);

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findById(String id);

    void changePassword(String email, String newPassword);

    void changeId(String email, String newId);

    void delete(UserJpaEntity userJpaEntity);

    void updateFollowers(Integer followers, String id);

    void updateFollowing(Integer following, String id);
}

package meusite.repository.coments.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ComentsJpaRepository extends JpaRepository<ComentsJpaEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ComentsJpaEntity c SET c.likes = :likes WHERE c.coment_id = :comentId")
    void updateComents(@Param("likes") Integer likes, @Param("comentId") Long comentId);
}

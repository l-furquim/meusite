package meusite.service.coments.implementation;

import meusite.controller.comments.dto.ComentPostRequestDto;
import meusite.domain.coments.Coments;
import meusite.domain.gateway.ComentsGateway;

import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.post.exception.PostException;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.exception.UserException;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.service.coments.ComentsService;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

public class ComentsServiceImplementation implements ComentsService {

    private ComentsGateway comentsGateway;

    private ComentsServiceImplementation(ComentsGateway comentsGateway){
        this.comentsGateway = comentsGateway;
    }
    public static ComentsServiceImplementation build(ComentsGateway comentsGateway){
        return new ComentsServiceImplementation(
                comentsGateway
        );
    }

    @Override
    public void comentPost(ComentPostRequestDto comentPostRequestDto, Optional<PostJpaEntity> post, Optional<UserJpaEntity> userJpaEntity) {
        if(comentPostRequestDto.content().isEmpty()){
            throw new PostException("Voce nao pode comentar nada vazio!");
        }
        if(post.isEmpty()){
            throw new PostException("Não foi possivel comentar, post nao encontrado");
        }

        Coments coment = new Coments(
                comentPostRequestDto.content(),
                0,userJpaEntity.get(),post.get()
        );

        ComentsJpaEntity comentsJpaEntity = ComentsJpaEntity.from(coment);

        try{
            this.comentsGateway.save(comentsJpaEntity);
        }catch (IllegalArgumentException e){
            throw new PostException(e.getMessage());
        }catch(OptimisticLockingFailureException e) {
            throw new UserException((e.getMessage()));
        }


    }
}

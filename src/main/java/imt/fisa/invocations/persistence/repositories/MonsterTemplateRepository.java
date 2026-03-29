package imt.fisa.invocations.persistence.repositories;

import imt.fisa.invocations.persistence.dto.MonsterTemplateEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonsterTemplateRepository extends MongoRepository<MonsterTemplateEntity, String> {
    List<MonsterTemplateEntity> findAll();
}

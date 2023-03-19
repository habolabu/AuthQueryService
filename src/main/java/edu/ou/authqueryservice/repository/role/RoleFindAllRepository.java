package edu.ou.authqueryservice.repository.role;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.RoleDocument;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleFindAllRepository extends BaseRepository<Query, List<RoleDocument>> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate input
     *
     * @param query input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Query query) {
        if (validValidation.isInValid(query)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "role query statement"
            );
        }
    }

    /**
     * Find all role
     *
     * @param query query
     * @return list of role
     * @author Nguyen Trung Kien - OU
     */
    @Override
    public List<RoleDocument> doExecute(Query query) {
        return mongoTemplate.find(
                query,
                RoleDocument.class
        );
    }

    @Override
    protected void postExecute(Query input) {
        // do nothing
    }
}

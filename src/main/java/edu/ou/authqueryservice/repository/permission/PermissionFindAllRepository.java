package edu.ou.authqueryservice.repository.permission;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.PermissionDocument;
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
public class PermissionFindAllRepository extends BaseRepository<Query, List<PermissionDocument>> {
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
                    "permission query statement"
            );
        }
    }

    /**
     * Find all permission
     *
     * @param query query
     * @return list of permission
     * @author Nguyen Trung Kien - OU
     */
    @Override
    public List<PermissionDocument> doExecute(Query query) {
        return mongoTemplate.find(
                query,
                PermissionDocument.class
        );
    }

    @Override
    protected void postExecute(Query input) {
        // do nothing
    }
}

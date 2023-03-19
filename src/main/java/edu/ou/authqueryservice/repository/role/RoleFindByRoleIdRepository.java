package edu.ou.authqueryservice.repository.role;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.RoleDocument;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleFindByRoleIdRepository extends BaseRepository<Integer, RoleDocument> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate roleId
     *
     * @param roleId roleId
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer roleId) {
        if (validValidation.isInValid(roleId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "role identity"
            );
        }
    }

    /**
     * Find account by roleId
     *
     * @param roleId roleId
     * @return account
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected RoleDocument doExecute(Integer roleId) {
        return mongoTemplate.findOne(
                new Query(
                        Criteria.where("oId")
                                .is(roleId)
                ),
                RoleDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}

package edu.ou.authqueryservice.repository.permission;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.PermissionDocument;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.constant.PermissionType;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionFindByPermissionIdsRepository extends BaseRepository<List<Integer>, List<PermissionDocument>> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate permission ids
     *
     * @param permissionIds permission ids
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(List<Integer> permissionIds) {
        if (validValidation.isInValid(permissionIds)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "permisision identities"
            );
        }
    }

    /**
     * Find permission by accountId
     *
     * @param permissionIds accountId
     * @return permission
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected List<PermissionDocument> doExecute(List<Integer> permissionIds) {
        return mongoTemplate.find(
                new Query()
                        .addCriteria(Criteria.where("oId").in(permissionIds))
                        .addCriteria(Criteria.where("type").is(PermissionType.PERMISSION)),
                PermissionDocument.class
        );
    }

    @Override
    protected void postExecute(List<Integer> input) {
        // do nothing
    }
}

package edu.ou.authqueryservice.repository.role;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.AccountSettingDocument;
import edu.ou.authqueryservice.data.entity.RoleDocument;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.coreservice.repository.base.IBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoleFindByAccountIdRepository extends BaseRepository<Integer, List<RoleDocument>> {
    private final IBaseRepository<Integer, List<AccountSettingDocument>> accountSettingFindByAccountIdRepository;
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;


    /**
     * Validate accountId
     *
     * @param accountId accountId
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer accountId) {
        if (validValidation.isInValid(accountId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "account identity"
            );
        }
    }

    /**
     * Find role by accountId
     *
     * @param accountId accountId
     * @return account
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected List<RoleDocument> doExecute(Integer accountId) {
        final Set<Integer> roleIds = accountSettingFindByAccountIdRepository
                .execute(accountId)
                .stream()
                .filter(AccountSettingDocument::isStatus)
                .map(AccountSettingDocument::getRoleId)
                .collect(Collectors.toSet());

        return mongoTemplate.find(
                new Query().addCriteria(Criteria.where("oId").in(roleIds)),
                RoleDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}

package edu.ou.authqueryservice.repository.accountSetting;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.AccountSettingDocument;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountSettingFindByAccountIdRepository extends BaseRepository<Integer, List<AccountSettingDocument>> {
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
     * Find account setting by accountId
     *
     * @param accountId accountId
     * @return account setting
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected List<AccountSettingDocument> doExecute(Integer accountId) {
        return mongoTemplate.find(
                new Query().addCriteria(Criteria.where("accountId").is(accountId)),
                AccountSettingDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}

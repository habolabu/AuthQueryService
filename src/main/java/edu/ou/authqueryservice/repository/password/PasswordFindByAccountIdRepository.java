package edu.ou.authqueryservice.repository.password;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.PasswordDocument;
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
public class PasswordFindByAccountIdRepository extends BaseRepository<Integer, PasswordDocument> {
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
     * Find account by accountId
     *
     * @param accountId accountId
     * @return account
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected PasswordDocument doExecute(Integer accountId) {
        return mongoTemplate.findOne(
                new Query(
                        Criteria.where("accountId")
                                .is(accountId)
                ),
                PasswordDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}

package edu.ou.authqueryservice.repository.account;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.AccountDocument;
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
public class AccountFindByUserIdRepository extends BaseRepository<Integer, AccountDocument> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate user id
     *
     * @param userId user id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer userId) {
        if (validValidation.isInValid(userId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "user identity"
            );
        }
    }

    /**
     * Find account by userId
     *
     * @param userId userId
     * @return account
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected AccountDocument doExecute(Integer userId) {
        return mongoTemplate.findOne(
                new Query(
                        Criteria.where("userId")
                                .is(userId)
                ),
                AccountDocument.class
        );
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }
}

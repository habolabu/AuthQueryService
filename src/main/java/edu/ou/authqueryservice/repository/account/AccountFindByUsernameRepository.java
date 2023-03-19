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
public class AccountFindByUsernameRepository extends BaseRepository<String, AccountDocument> {
    private final MongoTemplate mongoTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate username
     *
     * @param username username
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String username) {
        if (validValidation.isInValid(username)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "username"
            );
        }
    }

    /**
     * Find account by username
     *
     * @param username username
     * @return account
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected AccountDocument doExecute(String username) {
        return mongoTemplate.findOne(
                new Query(
                        Criteria.where("username")
                                .is(username)
                ),
                AccountDocument.class
        );
    }

    @Override
    protected void postExecute(String input) {
        // do nothing
    }
}

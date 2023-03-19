package edu.ou.authqueryservice.listener.account;

import edu.ou.authqueryservice.data.entity.AccountDocument;
import edu.ou.authqueryservice.data.entity.AccountSettingDocument;
import edu.ou.authqueryservice.data.entity.PasswordDocument;
import edu.ou.authqueryservice.data.entity.PermissionDocument;
import edu.ou.coreservice.listener.IBaseListener;
import edu.ou.coreservice.queue.auth.external.account.AccountGetDetailQueueE;
import edu.ou.coreservice.repository.base.IBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountGetDetailListener implements IBaseListener<String, Map<String, Object>> {
    private final IBaseRepository<String, AccountDocument> accountFindByUsernameRepository;
    private final IBaseRepository<Integer, PasswordDocument> passwordFindByAccountIdRepository;
    private final IBaseRepository<List<Integer>, List<PermissionDocument>> permissionFindByPermissionIdsRepository;
    private final IBaseRepository<Integer, List<AccountSettingDocument>> accountSettingFindByAccountIdRepository;
    /**
     * Get detail of account
     *
     * @param username username
     * @return account detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    @RabbitListener(queues = AccountGetDetailQueueE.QUEUE)
    public Map<String, Object> execute(String username) {
        final AccountDocument accountDocument = accountFindByUsernameRepository.execute(username);
        if (Objects.isNull(accountDocument)) {
            return null;
        }
        final PasswordDocument passwordDocument = passwordFindByAccountIdRepository.execute(accountDocument.getOId());

        if (Objects.isNull(passwordDocument)) {
            return null;
        }
        final List<Integer> permissionIds = accountSettingFindByAccountIdRepository
                .execute(accountDocument.getOId())
                .stream()
                .filter(AccountSettingDocument::isStatus)
                .map(AccountSettingDocument::getPermissionId)
                .collect(Collectors.toSet())
                .stream()
                .toList();

        final Set<String> permissions = permissionFindByPermissionIdsRepository
                .execute(permissionIds)
                .stream()
                .map(PermissionDocument::getName)
                .collect(Collectors.toSet());

        return new HashMap<>() {
            {
                put("username", accountDocument.getUsername());
                put("userId", String.valueOf(accountDocument.getUserId()));
                put("password", passwordDocument.getPassword());
                put("permissions", new ArrayList<>(permissions));
            }
        };
    }
}

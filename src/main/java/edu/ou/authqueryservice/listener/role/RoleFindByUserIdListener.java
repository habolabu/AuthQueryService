package edu.ou.authqueryservice.listener.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.AccountDocument;
import edu.ou.authqueryservice.data.entity.RoleDocument;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.listener.IBaseListener;
import edu.ou.coreservice.queue.auth.external.role.RoleFindByUserIdQueueE;
import edu.ou.coreservice.repository.base.IBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleFindByUserIdListener implements IBaseListener<Integer, Object> {
    private final IBaseRepository<Integer, List<RoleDocument>> roleFindByAccountIdRepository;
    private final IBaseRepository<Integer, AccountDocument> accountFindByUserIdRepository;
    private final ObjectMapper objectMapper;

    /**
     * Find role by user id
     *
     * @param userId user id
     * @return role information
     * @author Nguyen Trung Kien - OU
     */
    @Override
    @RabbitListener(queues = RoleFindByUserIdQueueE.QUEUE)
    public Object execute(Integer userId) {
        final AccountDocument account = accountFindByUserIdRepository.execute(userId);

        if (Objects.isNull(account)) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "role",
                    "user identity",
                    userId.toString()
            );
        }

        return roleFindByAccountIdRepository
                .execute(account.getOId())
                .stream()
                .map(roleDocument -> objectMapper.convertValue(roleDocument, Map.class))
                .collect(Collectors.toList());
    }
}
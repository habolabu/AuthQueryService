package edu.ou.authqueryservice.service.permission;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.AccountSettingDocument;
import edu.ou.authqueryservice.data.entity.PermissionDocument;
import edu.ou.authqueryservice.data.pojo.request.permission.PermissionFindByAccountIdRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionFindByAccountIdService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<List<Integer>, List<PermissionDocument>> permissionFindByPermissionIdsRepository;
    private final IBaseRepository<Integer, List<AccountSettingDocument>> accountSettingFindByAccountIdRepository;
    private final ValidValidation validValidation;

    /**
     * Validate input
     *
     * @param request request
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, PermissionFindByAccountIdRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "account identity"
            );
        }
    }

    /**
     * Find all permission of specific account
     *
     * @param request request
     * @return list of permission
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final PermissionFindByAccountIdRequest permissionFindByAccountIdRequest =
                (PermissionFindByAccountIdRequest) request;
        final List<AccountSettingDocument> accountSettingDocuments = accountSettingFindByAccountIdRepository
                .execute(permissionFindByAccountIdRequest.getAccountId());

        final List<Integer> permissionIds = accountSettingDocuments
                .stream()
                .map(AccountSettingDocument::getPermissionId)
                .collect(Collectors.toSet())
                .stream()
                .toList();

        final List<PermissionDocument> permissionDocuments =
                permissionFindByPermissionIdsRepository.execute(permissionIds);

        final Map<Integer, Boolean> settingsMap = accountSettingDocuments
                .stream()
                .collect(Collectors.toMap(
                        AccountSettingDocument::getPermissionId,
                        AccountSettingDocument::isStatus));

        permissionDocuments.forEach(permissionDocument ->
                permissionDocument.setStatus(settingsMap.get(permissionDocument.getOId())));

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        permissionDocuments,
                        CodeStatus.SUCCESS,
                        Message.Success.SUCCESSFUL
                )
        );
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}

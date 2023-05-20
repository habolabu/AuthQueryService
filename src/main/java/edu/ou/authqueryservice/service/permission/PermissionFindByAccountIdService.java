package edu.ou.authqueryservice.service.permission;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.AccountSettingDocument;
import edu.ou.authqueryservice.data.entity.PermissionDocument;
import edu.ou.authqueryservice.data.entity.RoleDocument;
import edu.ou.authqueryservice.data.pojo.request.permission.PermissionFindByAccountIdRequest;
import edu.ou.authqueryservice.data.pojo.response.permission.PermissionFindByAccountIdResponse;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.constant.PermissionType;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionFindByAccountIdService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<List<Integer>, List<PermissionDocument>> permissionFindByPermissionIdsRepository;
    private final IBaseRepository<Integer, List<AccountSettingDocument>> accountSettingFindByAccountIdRepository;
    private final IBaseRepository<Integer, RoleDocument> roleFindByRoleIdRepository;
    private final IBaseRepository<Query, List<PermissionDocument>> permissionFindAllRepository;

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

        final Map<Integer, List<Integer>> roles = accountSettingDocuments
                .stream()
                .collect(Collectors.groupingBy(AccountSettingDocument::getRoleId))
                .entrySet()
                .stream()
                .map(e ->
                        Map.entry(
                                e.getKey(),
                                e.getValue()
                                        .stream()
                                        .map(AccountSettingDocument::getPermissionId)
                                        .collect(Collectors.toList())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        final List<PermissionFindByAccountIdResponse> permissionFindByAccountIdResponses = new ArrayList<>();

        roles.forEach((key, value) -> {

            final RoleDocument roleDocument = roleFindByRoleIdRepository.execute(key);
            final List<PermissionDocument> permissionDocuments = permissionFindByPermissionIdsRepository.execute(value);

            final Map<Integer, Boolean> settingsMap = accountSettingDocuments
                    .stream()
                    .collect(Collectors.toMap(
                            AccountSettingDocument::getPermissionId,
                            AccountSettingDocument::isStatus));

            permissionDocuments.forEach(permissionDocument ->
                    permissionDocument.setStatus(settingsMap.get(permissionDocument.getOId())));

            final List<PermissionDocument> originalPermissionDocuments  = permissionFindAllRepository.execute(new Query());

            originalPermissionDocuments.forEach(originalPermissionDocument -> {
                if (permissionDocuments.stream()
                        .filter(permissionDocument -> permissionDocument.getOId() == originalPermissionDocument.getOId())
                        .toList()
                        .size() > 0) {
                    originalPermissionDocument.setStatus(true);
                }
            });

            final List<PermissionDocument> labelPermissionDocuments = originalPermissionDocuments
                    .stream()
                    .filter(permissionDocument -> PermissionType.LABEL.equals(permissionDocument.getType()))
                    .toList();

            labelPermissionDocuments.forEach(labelPermissionDocument ->
                    labelPermissionDocument
                        .setPermissionDocuments(originalPermissionDocuments
                            .stream()
                            .filter(permissionDocument ->
                                    labelPermissionDocument.getOId() == permissionDocument.getParentId())
                            .toList())
                        .setStatus(true));

            permissionFindByAccountIdResponses.add(
                    new PermissionFindByAccountIdResponse()
                            .setRoleId(roleDocument.getOId())
                            .setRoleName(roleDocument.getName())
                            .setRoleDisplay(roleDocument.getDisplay())
                            .setPermissions(labelPermissionDocuments));
        });

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        permissionFindByAccountIdResponses,
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

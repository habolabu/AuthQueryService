package edu.ou.authqueryservice.service.permission;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.PermissionDocument;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.constant.PermissionType;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionFindAllWithoutPaginationService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Query, List<PermissionDocument>> permissionFindAllRepository;

    @Override
    protected void preExecute(IBaseRequest request) {
        // do nothing
    }

    /**
     * Find all permission
     *
     * @param request request
     * @return permission list
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final List<PermissionDocument> permissionDocuments = permissionFindAllRepository.execute(new Query());
        final List<PermissionDocument> labelPermissionDocuments = permissionDocuments
                .stream()
                .filter(permissionDocument -> PermissionType.LABEL.equals(permissionDocument.getType()))
                .toList();

        labelPermissionDocuments.forEach(labelPermissionDocument ->
                labelPermissionDocument.setPermissionDocuments(permissionDocuments
                        .stream()
                        .filter(permissionDocument ->
                                labelPermissionDocument.getOId() == permissionDocument.getParentId())
                        .toList()));

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        labelPermissionDocuments,
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

package edu.ou.authqueryservice.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndPoint {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SignIn {
        public static final String BASE = "/sign-in";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Role {
        public static final String BASE = "/role";
        public static final String ALL = "/all";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Permission {
        public static final String BASE = "/permission";
        public static final String ALL = "/all";
        public static final String FIND_BY_ACCOUNT = "/{accountId}";
    }
}

package edu.ou.authqueryservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.coreservice.common.constant.ClaimType;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.config.JwtTokenProvider;
import edu.ou.coreservice.data.pojo.request.impl.token.RefreshTokenRequest;
import edu.ou.coreservice.data.pojo.response.impl.RefreshTokenResponse;
import edu.ou.coreservice.queue.auth.external.token.TokenCheckValidQueueE;
import edu.ou.coreservice.queue.auth.external.token.TokenRefreshQueueE;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class JwtAccessTokenProvider {
    @Value("${security.jwt.tokenGeneration}")
    private String tokenGeneration;
    @Value("${security.jwt.tokenRefresh}")
    private String tokenRefresh;
    @Value("${security.jwt.clientId}")
    private String clientId;
    @Value("${security.jwt.clientSecret}")
    private String clientSecret;
    @Value("${security.jwt.grantTypeGenerate}")
    private String grantTypeGenerate;
    @Value("${security.jwt.grantTypeRefresh}")
    private String grantTypeRefresh;
    @Value("${security.jwt.audience}")
    private String audience;
    @Value("${security.jwt.authorizedParties}")
    private String authorizedParties;
    @Value("${security.jwt.issuer}")
    private String issuer;
    @Autowired
    private MessageConverter messageConverter;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * generate jwt token
     *
     * @param username username information
     * @param password password information
     * @return jwt token
     * @author Nguyen Trung Kien - OU
     */
    public Map<String, String> generateToken(String username, String password)
            throws URISyntaxException,
            ExecutionException,
            InterruptedException {
        final Map<String, String> params = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "username", username,
                "password", password,
                "grant_type", grantTypeGenerate
        );

        final CompletableFuture<Map<String, String>> response = this.sendUrlEncodedRequest(
                tokenGeneration,
                params
        );

        final Map<String, String> data = response.get();
        if (Objects.isNull(data.get("access_token"))) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "user",
                    "username",
                    username
            );
        }
        return data;

    }

    /**
     * validate token
     *
     * @param authToken jwt token
     * @return valid status
     * @author Nguyen Trung Kien - OU
     */
    @RabbitListener(queues = {TokenCheckValidQueueE.QUEUE})
    public boolean validateToken(String authToken) {
        return authorizedParties.equals(
                jwtTokenProvider.getClaimValueFromJWT(
                        authToken,
                        ClaimType.AUTHORIZED_PARTIES
                ))
                && audience.equals(
                jwtTokenProvider.getClaimValueFromJWT(
                        authToken,
                        ClaimType.AUDIENCE
                ))
                && issuer.equals(
                jwtTokenProvider.getClaimValueFromJWT(
                        authToken,
                        ClaimType.ISSUER
                ));
    }

    /**
     * refresh token
     *
     * @param input refresh request (username, role, oldToken)
     * @return jwt token
     * @author Nguyen Trung Kien - OU
     */
    @RabbitListener(queues = {TokenRefreshQueueE.QUEUE})
    public RefreshTokenResponse refreshToken(Object input)
            throws ExecutionException,
            InterruptedException,
            URISyntaxException {
        final Object message = messageConverter.fromMessage((org.springframework.amqp.core.Message) input);
        final RefreshTokenRequest refreshTokenRequest = objectMapper.convertValue(
                message,
                new TypeReference<>() {
                });
        final RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse()
                .setNewToken(refreshTokenRequest.getToken())
                .setNewRefreshToken(refreshTokenRequest.getRefreshToken());

        final String expiredDate = jwtTokenProvider.getClaimValueFromJWT(
                refreshTokenRequest.getToken(),
                ClaimType.EXPIRED
        );

        if (new Date(Calendar.getInstance().getTimeInMillis())
                .after(new Date(Long.parseLong(expiredDate) * 1000))) {
            final Map<String, String> params = Map.of(
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "grant_type", grantTypeRefresh,
                    "refresh_token", refreshTokenRequest.getRefreshToken()
            );
            final CompletableFuture<Map<String, String>> response = this.sendUrlEncodedRequest(
                    tokenRefresh,
                    params
            );

            final Map<String, String> data = response.get();
            if (Objects.isNull(data.get("access_token"))) {
                return null;
            }
            refreshTokenResponse.setNewToken(data.get("access_token"))
                    .setNewRefreshToken(data.get("refresh_token"));

        }
        return refreshTokenResponse;
    }

    /**
     * Encode params
     *
     * @param parameters params
     * @return body of request
     */
    private HttpRequest.BodyPublisher getParamsUrlEncoded(Map<String, String> parameters) {
        final String urlEncoded = parameters
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(urlEncoded);
    }

    /**
     * Send urlencoded request
     *
     * @param url    url
     * @param params body of request
     * @return response
     * @throws URISyntaxException invalid uri
     */
    private CompletableFuture<Map<String, String>> sendUrlEncodedRequest(
            String url,
            Map<String, String> params
    ) throws URISyntaxException {
        return HttpClient
                .newHttpClient()
                .sendAsync(
                        HttpRequest.newBuilder()
                                .uri(new URI(url))
                                .headers("Content-Type", "application/x-www-form-urlencoded")
                                .POST(getParamsUrlEncoded(params))
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                )
                .thenApply(HttpResponse::body)
                .thenApply(content -> {
                    try {
                        return objectMapper.readValue(content, new TypeReference<>() {
                        });
                    } catch (JsonProcessingException e) {
                        throw new BusinessException(
                                CodeStatus.SERVER_ERROR,
                                Message.Error.UN_KNOWN
                        );
                    }
                });
    }
}

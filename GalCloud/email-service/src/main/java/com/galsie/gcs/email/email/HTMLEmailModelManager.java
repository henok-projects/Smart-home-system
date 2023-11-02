package com.galsie.gcs.email.email;

import com.galsie.gcs.email.configuration.email.GCSEmailConfiguration;
import com.galsie.gcs.microservicecommon.lib.email.data.discrete.EmailType;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Bean created in {@link GCSEmailConfiguration}
 */
@Slf4j
public class HTMLEmailModelManager {
    static final long CACHE_SIZE_LIMIT = 1000;
    static final long EXPIRE_AFTER_HOURS = 6;
    private final Cache<EmailType, HTMLEmailModel> htmlEmailModelCache = Caffeine.newBuilder().maximumSize(CACHE_SIZE_LIMIT).expireAfterAccess(EXPIRE_AFTER_HOURS, TimeUnit.HOURS).build();

    public Optional<HTMLEmailModel> getEmailModelFor(EmailType emailType){
        var pathOpt = getFilePathFor(emailType);
        if (pathOpt.isEmpty()){
            log.info("Failed to get the HTMLEmailModel for the email type " + emailType.name() + " reason: Html model path is not set");
            return Optional.empty();
        }
        try {
            return Optional.of(HTMLEmailModel.fromHtmlFile(pathOpt.get()));
        }catch (Exception ex){
            log.info("Failed to get the HTMLEmailModel for the email type " + emailType.name() + " reason: " + ex.getLocalizedMessage());
            return Optional.empty();
        }
    }


    private static Optional<String> getFilePathFor(EmailType emailType){
        var path = switch (emailType){
            case USER_EMAIL_VERIFICATION -> "verifyUserEmail.html";
//            new code
            case EMAIL_BASED_USER_JOINED_NOTIFICATION -> "notifyUserJoined.html";
//            new code
            case EMAIL_BASED_INVITATION_NOTIFICATION -> "notifyInviteUser.html";
            default -> null;
        };
        return Optional.ofNullable(path);
    }
}

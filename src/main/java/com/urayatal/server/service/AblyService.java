package com.urayatal.server.service;

import com.google.gson.Gson;
import com.urayatal.server.dto.MessageDto;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.rest.Auth;
import io.ably.lib.rest.Channel;
import io.ably.lib.types.PaginatedResult;
import io.ably.lib.types.Param;
import io.ably.lib.types.PresenceMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
class AblyService {
    @Value("${apiKey}")
    private String apiKey;

    public boolean getPresence(String userName) {
        log.info("Entered AblyService:getPresence");
        try (AblyRest rest = new AblyRest(apiKey)) {
            Channel channel = rest.channels.get("Presence");
            Param param = new Param("clientId",userName);
            PaginatedResult<PresenceMessage> membersPage = channel.presence.get(new Param[]{param});
            log.info(" AblyService:getPresence -> Verifying the presence  ");
            if(  membersPage.items().length >0 )
            {
                log.info("Leaving AblyService:getPresence");
                return true;
            }else{
                log.info("Leaving AblyService:getPresence");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Leaving AblyService:getPresence -> Exception occurred: \n {}",e.getMessage());
            return false;
        }

    }

    public String generateToken(String userName) {
        log.info("Entering AblyService:generateToken");
        try (AblyRest rest = new AblyRest(apiKey)) {
            Auth.TokenParams tokenParams = new Auth.TokenParams();
            String uuid = UUID.nameUUIDFromBytes(userName.getBytes()).toString();
            String channelId = "MessageChannel_"+uuid;
            log.info("channelId -> {}",channelId);
            tokenParams.clientId = userName ;
            tokenParams.capability = "{\""+channelId+"\":[\"publish\",\"subscribe\"]," +
                    "\"Presence\":[\"presence\"]}";
            Auth.TokenDetails tokenDetails = rest.auth.requestToken(tokenParams, null);
            log.info("tokenDetails -> {}",tokenDetails.asJson());
            log.info("Leaving AblyService:generateToken");
            return tokenDetails.token;
        } catch (Exception e) {
            log.error("Exception:",e);
            log.error("Leaving AblyService:getPresence -> Exception occurred");
            return "Error requesting token: " + e.getMessage();
        }
    }

    public void publishMessageToUser(MessageDto messageDto) {
        log.info("Entering AblyService:publishMessageToUser");
        try (AblyRest rest = new AblyRest(apiKey)) {
            String channelId ="MessageChannel_"+ UUID.nameUUIDFromBytes(messageDto.getMessageTo().getBytes());
            log.info("channelId -> {}",channelId);
            Channel channel = rest.channels.get(channelId);
            Gson g = new Gson();
            channel.publish("NEWMESSAGE", g.toJson(messageDto));
            log.info("Message Published");
            log.info("Leaving AblyService:publishMessageToUser");
        } catch (Exception e) {
            log.error("Leaving AblyService:getPresence -> Exception occurred");
            log.error("Exception:",e);
        }
    }
}

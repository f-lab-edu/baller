package com.baller.common.config;

import com.baller.infrastructure.redis.RedisChannels;
import com.baller.infrastructure.redis.ScoreUpdateSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
        return new StringRedisTemplate(cf);
    }

    @Bean
    public RedisMessageListenerContainer messageListenerContainer(
            RedisConnectionFactory cf,
            ScoreUpdateSubscriber subscriber
    ) {
        var c = new RedisMessageListenerContainer();
        c.setConnectionFactory(cf);
        // 모든 게임 채널 패턴 구독
        c.addMessageListener(subscriber, new PatternTopic(RedisChannels.patternAllGames()));
        return c;
    }

}

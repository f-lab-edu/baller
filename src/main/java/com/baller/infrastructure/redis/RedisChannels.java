package com.baller.infrastructure.redis;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class RedisChannels {

    public static String gameChannel(Long gameId) {
        return "pubsub:game:" + gameId;
    }
    public static String patternAllGames() {
        return "pubsub:game:*";
    }

}

package dev.slne.surf.shrieker.core.common

import dev.slne.surf.api.core.util.requiredService
import dev.slne.surf.rabbitmq.api.RabbitMQApi
import dev.slne.surf.redis.RedisApi

private val instance = requiredService<ShriekerInstance>()

interface ShriekerInstance {
    val rabbitApi: RabbitMQApi
    val redisApi: RedisApi

    companion object : ShriekerInstance by instance {
        val INSTANCE get() = instance
    }
}
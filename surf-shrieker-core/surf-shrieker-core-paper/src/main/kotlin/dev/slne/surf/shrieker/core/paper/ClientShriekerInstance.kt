package dev.slne.surf.shrieker.core.paper

import dev.slne.surf.rabbitmq.api.ClientRabbitMQApi
import dev.slne.surf.redis.RedisApi
import dev.slne.surf.shrieker.core.common.ShriekerInstance

interface ClientShriekerInstance : ShriekerInstance {
    val clientLoader: ClientLoader

    override val rabbitApi: ClientRabbitMQApi get() = clientLoader.rabbitApi
    override val redisApi: RedisApi get() = clientLoader.redisApi

    companion object :
        ClientShriekerInstance by ShriekerInstance.INSTANCE as ClientShriekerInstance {
        val INSTANCE get() = ShriekerInstance.INSTANCE
    }
}
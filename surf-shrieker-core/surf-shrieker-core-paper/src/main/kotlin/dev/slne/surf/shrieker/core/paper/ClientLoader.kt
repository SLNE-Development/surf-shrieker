package dev.slne.surf.shrieker.core.paper

import dev.slne.surf.rabbitmq.api.ClientRabbitMQApi
import dev.slne.surf.redis.RedisApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Path

class ClientLoader(
    dataPath: Path
) {
    val rabbitApi = ClientRabbitMQApi.create("surf-shrieker", dataPath)
    val redisApi = RedisApi.create()

    suspend fun onLoad() {
        rabbitApi.freezeAndConnect()
    }

    suspend fun onEnable() = withContext(Dispatchers.IO) {
        redisApi.freezeAndConnect()
    }

    suspend fun onDisable() = withContext(Dispatchers.IO) {
        rabbitApi.disconnect()
        redisApi.disconnect()
    }
}

val redisApi get() = ClientShriekerInstance.redisApi
val rabbiApi get() = ClientShriekerInstance.rabbitApi
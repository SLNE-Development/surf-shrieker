package dev.slne.surf.shrieker.microservice

import com.google.auto.service.AutoService
import dev.slne.surf.database.DatabaseApi
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.microservice.api.microservice.Microservice
import dev.slne.surf.rabbitmq.api.ServerRabbitMQApi
import dev.slne.surf.shrieker.microservice.service.ServerReportBanService
import dev.slne.surf.shrieker.microservice.service.ServerReportService
import dev.slne.surf.shrieker.microservice.service.ServerVoiceLogService
import dev.slne.surf.shrieker.microservice.table.ReportBansTable
import dev.slne.surf.shrieker.microservice.table.ReportDataTable
import dev.slne.surf.shrieker.microservice.table.ReportsTable
import dev.slne.surf.shrieker.microservice.table.VoiceChatLogTable
import kotlin.io.path.Path

@AutoService(Microservice::class)
class ShriekerMicroservice : Microservice() {
    override val dataPath = Path("config")
    private val databaseApi = DatabaseApi.create(dataPath)
    private val rabbitApi = ServerRabbitMQApi.create("surf-shrieker", dataPath)

    override suspend fun onBootstrap(args: List<String>) {
        suspendTransaction {
            SchemaUtils.create(
                ReportsTable,
                ReportDataTable,
                ReportBansTable,
                VoiceChatLogTable
            )
        }

        rabbitApi.registerRpcService<ServerReportService>(ServerReportService)
        rabbitApi.registerRpcService<ServerReportBanService>(ServerReportBanService)
        rabbitApi.registerRpcService<ServerVoiceLogService>(ServerVoiceLogService)

        rabbitApi.freezeAndConnect()
    }

    override suspend fun onDisable() {
        rabbitApi.disconnect()
        databaseApi.shutdown()
    }
}
package dev.slne.surf.shrieker.paper

import com.google.auto.service.AutoService
import dev.slne.surf.shrieker.core.common.ShriekerInstance
import dev.slne.surf.shrieker.core.paper.ClientLoader
import dev.slne.surf.shrieker.core.paper.ClientShriekerInstance
import net.kyori.adventure.util.Services

@AutoService(ShriekerInstance::class)
class PaperShriekerInstance : ClientShriekerInstance, Services.Fallback {
    override val clientLoader = ClientLoader(plugin.dataPath)
}
package command.handler

import command.Command
import command.CommandResponse
import command.CommandService
import player.PlayerManager
import kotlin.random.Random

object ModerWarfare2 : CommandHandler {
    private val commands = listOf(
        Command("next", "Get a random MW2 map.")
    )

    val baseMaps = listOf(
        "Afghan", "Derail", "Estate", "Favela", "Highrise", "Invasion",
        "Karachi", "Quarry", "Rundown", "Rust", "Scrapyard", "Skidrow",
        "Sub Base", "Terminal", "Underpass", "Wasteland"
    )
    private val dlcMaps = listOf(
        "Strike", "Bailout", "Absturz", "Verwuchert", "Salvage", "Storm",
        "Carnival", "Fuel", "Trailerpark", "Vacant"
    )
    private val allMaps = baseMaps + dlcMaps
    private val playedMaps = mutableListOf<String>()

    private fun getRandomMap(includeDLC: Boolean = false): String {
        val maps = if (includeDLC) allMaps else baseMaps
        var validMaps = maps.filterNot { playedMaps.contains(it) }
        if (validMaps.isEmpty()) {
            playedMaps.clear()
            validMaps = maps
        }

        val map = validMaps[Random.nextInt(validMaps.size)]
        playedMaps.add(map)

        return map
    }

    override suspend fun handleInteractionEvent(
        eventMsg: String,
        commandService: CommandService
    ): CommandResponse {
        val msgBuilder = StringBuilder()
        PlayerManager.players.forEach {
            val index = Random.nextInt(364966)
            val url = "https://mw2.rcg.io/class/$index"
            msgBuilder.append("$it: $url\n")
        }
        msgBuilder.append("----------")

        return CommandResponse(msgBuilder.toString(), commandService.distributeCommand("!mw2-map-dlc"))
    }

    override fun handleCommand(msg: String): String? {
        return when (msg) {
            "map" -> return "Next map is: ${getRandomMap()}"
            "map-dlc" -> getRandomMap(true)
            else -> null
        }
    }

    override fun canHandle(command: Command): Boolean {
        commands.forEach { if (it.name == command.name) return true }
        return false
    }

    override fun prefix() = "mw2"
}

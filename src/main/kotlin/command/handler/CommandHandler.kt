package command.handler

import command.Command
import command.CommandResponse
import command.CommandService

interface CommandHandler {
    fun handleCommand(msg: String): String?

    suspend fun handleInteractionEvent(
        eventMsg: String,
        commandService: CommandService
    ): CommandResponse

    fun canHandle(command: Command): Boolean

    fun prefix(): String

    fun commands(): List<Command>
}
package kord

import command.CommandService
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent

// TODO naming
class KordMaster(private val commandService: CommandService) {
    suspend fun initializeCommands(kord: Kord) {
        kord.createGlobalChatInputCommand(
            "next",
            "Get a random MW2 map."
        )
    }

    suspend fun handleInteractionEvent(event: ChatInputCommandInteractionCreateEvent) {
        println("Received Command: ${event.interaction.command.rootName}")
        val commandString = event.interaction.command.rootName

        val response = commandService.distributeEventCommand(commandString)
        event.interaction.deferPublicResponse().respond {
            content = response.responseMsg
        }

        if (response.ttsMsg != null) event.interaction.channel.createMessage {
            tts = true
            content = response.ttsMsg
        }
    }

    suspend fun handleMessageEvent(event: MessageCreateEvent) {
        // ignore other bots, even ourselves. We only serve humans here!
        if (event.message.author?.isBot != false) return
        println("Received Message: '${event.message.content}'")

        val returnMsg = commandService.distributeCommand(event.message.content) ?: return
        event.message.channel.createMessage {
            tts = commandService.isTTSCommand(event.message.content)
            content = returnMsg
        }
    }

}
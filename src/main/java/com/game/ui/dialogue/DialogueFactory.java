package com.game.ui.dialogue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DialogueFactory {
    private void addIntroNode(Conversation conv) {
        List<Conversation.DialogueOption> options = List.of(
                createOption("qúe dices?", "VIELLO_CELTA_RESPONSE", null),
                createOption("AJJAJAJAJAJAJJAJAJA", "VIELLO_TEIS_OPINION_PROMPT",
                        List.of("SET_FLAG:LAUGHED"))
        );

        conv.addNode(createNode("VIELLO_INTRO",
                "sabes o que din dos pimentitos de padrón? JAJJAJA \n\no sabes o qué??? CONTESTA XA!11!!!",
                options, null));
    }

    private void addCeltaResponseNode(Conversation conv) {
        List<Conversation.DialogueOption> options = List.of(
                createOption("Hueles raro", "VIELLO_OFFENDED_RESPONSE",
                        List.of("SET_FLAG:OFFENDED")),
                createOption("la verdad que el celta es el amor de mi vida",
                        "VIELLO_TEIS_OPINION_PROMPT", null)
        );

        conv.addNode(createNode("VIELLO_CELTA_RESPONSE",
                "Deus deume o peor dos destinos deste mundo, \nser do Celta.",
                options, null));
    }

    // Métodos similares para los demás nodos (addTeisOpinionNode, etc.)

    // Métodos factory para crear nodos y opciones
    private Conversation.ConversationNode createNode(String id, String text,
                                                     List<Conversation.DialogueOption> options, Map<String, Boolean> flags) {
        return new Conversation.ConversationNode(id, text, options, flags);
    }

    private Conversation.DialogueOption createOption(String text, String nextNode,
                                                     List<String> actions) {
        return new Conversation.DialogueOption(text, nextNode, actions);
    }

    private void addOffendedResponseNode(Conversation conv) {
        conv.addNode(createNode("VIELLO_OFFENDED_RESPONSE",
                "Tira pralá' co teu flow manin",
                Collections.emptyList(),
                Map.of("OFFENDED", true)));
    }

    private void addRiddleNode(Conversation conv) {
        List<Conversation.DialogueOption> options = Arrays.asList(
                createOption("diselo manin, que te quiero", "VIELLO_REWARD",
                        List.of("GIVE_REWARD")),
                createOption("diselo manin, que non sei", "VIELLO_INTRO", null)
        );

        conv.addNode(createNode("VIELLO_RIDDLE",
                "Diselo manin, diselo manin...",
                options,
                Map.of("LAUGHED", true)));
    }
}

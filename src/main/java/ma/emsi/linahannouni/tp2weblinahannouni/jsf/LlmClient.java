package ma.emsi.linahannouni.tp2weblinahannouni.jsf;



import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;

/**
 * Gère l'interface avec l'API de Gemini.
 * Son rôle est essentiellement de lancer une requête à chaque nouvelle
 * question qu'on veut envoyer à l'API.
 *
 * De portée dependent pour réinitialiser la conversation à chaque fois que
 * l'instance qui l'utilise est renouvelée.
 * Par exemple, si l'instance qui l'utilise est de portée View, la conversation est
 * réunitialisée à chaque fois que l'utilisateur quitte la page en cours.
 */
@Dependent
public class LlmClient implements Serializable {

    private final ChatMemory chatMemory;
    private final Assistant assistant;
    // Clé pour l'API du LLM
    private final String key;


    public LlmClientPourGemini() {
        // Récupère la clé secrète pour travailler avec l'API du LLM, mise dans une variable d'environnement
        // du système d'exploitation.
        this.key = System.getenv("GEMINI_KEY");



        if (this.key == null || this.key.isEmpty()) {
            throw new IllegalStateException(" Variable d'environnement GEMINI_API_KEY non définie !");
        }

        ChatLanguageModel model = GoogleAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .build();

        this.chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        this.assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();
        public void setSystemRole(String roleSysteme) {
            chatMemory.clear();
            chatMemory.add(new SystemMessage(roleSysteme));
        }

        /**
         * Envoie une requête au LLM et renvoie sa réponse.
         */
        public String envoyerQuestion(String question) {
            return assistant.chat(question);
        }

    }

}



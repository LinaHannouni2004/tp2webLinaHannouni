package ma.emsi.linahannouni.tp2weblinahannouni.jsf;



import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.client.*;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import java.io.Serializable;
import dev.langchain4j.data.message.SystemMessage;



@Dependent
public class LlmClient implements Serializable {

    private final ChatMemory chatMemory;
    private final Assistant assistant;
    // Clé pour l'API du LLM
    private final String key;


    public LlmClient() {

        this.key = System.getenv("GEMINI_KEY");



        if (this.key == null || this.key.isEmpty()) {
            throw new IllegalStateException(" Variable d'environnement GEMINI_API_KEY non définie !");
        }

        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(key)
                .modelName("gemini-2.5-flash")
                .build();

        this.chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        this.assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();
        }

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




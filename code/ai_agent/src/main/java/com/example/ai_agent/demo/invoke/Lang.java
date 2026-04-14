package com.example.ai_agent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class Lang {

    public static void main(String[] args) {
        // QwenChatModel 本身就是一个聊天模型，直接使用
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-plus")
                .build();
        
        // 调用模型 - 使用 chat 方法
        String response = qwenChatModel.chat("你是谁？");
        System.out.println("LangChain4j 响应：");
        System.out.println(response);
    }
}

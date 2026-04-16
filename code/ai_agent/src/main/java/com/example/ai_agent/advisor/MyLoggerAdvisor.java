package com.example.ai_agent.advisor;






import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;

@Slf4j
public class MyLoggerAdvisor implements CallAdvisor, StreamAdvisor {

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        logRequest(chatClientRequest);

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        logResponse(chatClientResponse);

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
                                                 StreamAdvisorChain streamAdvisorChain) {
        logRequest(chatClientRequest);

        Flux<ChatClientResponse> chatClientResponses = streamAdvisorChain.nextStream(chatClientRequest);

        return new ChatClientMessageAggregator().aggregateChatClientResponse(chatClientResponses, this::logResponse);
    }

    protected void logRequest(ChatClientRequest request) {
        log.debug("=== 请求层级输出 ===");
        log.debug("request.prompt().toString(): {}", request.prompt().getUserMessage().getText());
        
        // 读取共享数据
        Object sharedValue = request.context().get("shared_key");
        log.debug("共享数据 shared_key: {}", sharedValue);
    }

    protected void logResponse(ChatClientResponse chatClientResponse) {
        log.debug("=== 响应层级输出 ===");
//        log.debug("1. chatClientResponse 对象: {}", chatClientResponse);
//        log.debug("2. chatClientResponse.chatResponse(): {}", chatClientResponse.chatResponse());
//        log.debug("3. chatClientResponse.chatResponse().getResult(): {}", chatClientResponse.chatResponse().getResult());
//        log.debug("4. chatClientResponse.chatResponse().getResult().getOutput(): {}", chatClientResponse.chatResponse().getResult().getOutput());
        log.debug("chatClientResponse.chatResponse().getResult().getOutput().getText(): {}", chatClientResponse.chatResponse().getResult().getOutput().getText());
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }





}

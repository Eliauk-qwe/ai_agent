package com.example.ai_agent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用 Hutool 调用阿里云通义千问 API 示例
 */
public class HutoolHttpExample {

    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    public static void main(String[] args) {
        // 从环境变量获取 API Key
        String apiKey = TestApiKey.API_KEY;
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("请设置环境变量 DASHSCOPE_API_KEY");
            return;
        }

        System.out.println("HTTP 响应：");

        String response = callDashScopeApi(apiKey, "你是谁？");
        System.out.println("完整响应结果：");
        System.out.println(response);
        
        // 提取并打印助手回复内容
        String content = extractAssistantMessage(response);
        System.out.println("\n助手回复：");
        System.out.println(content);
    }
    
    /**
     * 调用通义千问 API
     * 
     * @param apiKey API密钥
     * @param userMessage 用户消息
     * @return API响应结果
     */
    public static String callDashScopeApi(String apiKey, String userMessage) {
        // 构建请求体
        Map<String, Object> requestBody = buildRequestBody(userMessage);
        
        // 发送 POST 请求，使用 try-with-resources 自动关闭资源
        try (HttpResponse response = HttpRequest.post(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(requestBody))
                .execute()) {
            
            return response.body();
        }
    }
    
    /**
     * 构建请求体
     * 
     * @param userMessage 用户消息
     * @return 请求体 Map
     */
    private static Map<String, Object> buildRequestBody(String userMessage) {
        // 构建 input
        Map<String, Object> input = buildInput(userMessage);
        
        // 构建 parameters
        Map<String, String> parameters = new HashMap<>();
        parameters.put("result_format", "message");
        
        // 构建完整请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-plus");
        requestBody.put("input", input);
        requestBody.put("parameters", parameters);
        
        return requestBody;
    }
    
    /**
     * 构建 input 部分
     * 
     * @param userMessage 用户消息
     * @return input Map
     */
    private static Map<String, Object> buildInput(String userMessage) {
        // 构建 messages 列表
        List<Map<String, String>> messages = new ArrayList<>();
        
        // 添加 system 消息
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");
        messages.add(systemMessage);
        
        // 添加 user 消息
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);
        
        // 构建 input
        Map<String, Object> input = new HashMap<>();
        input.put("messages", messages);
        
        return input;
    }
    
    /**
     * 解析响应并提取助手回复内容
     * 
     * @param responseBody API响应体
     * @return 助手回复的文本内容
     */
    public static String extractAssistantMessage(String responseBody) {
        JSONObject jsonResponse = JSONUtil.parseObj(responseBody);
        
        // 提取 output.choices[0].message.content
        return jsonResponse.getByPath("output.choices[0].message.content", String.class);
    }
}

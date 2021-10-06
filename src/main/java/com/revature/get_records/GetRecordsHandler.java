package com.revature.get_records;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRecordsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

        // Setting Cors headers to bypass API Gateway Issue
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization");
        headers.put("Access-Control-Allow-Origin", "*");
        responseEvent.setHeaders(headers);

        LambdaLogger logger = context.getLogger();
        logger.log("RECEIVED EVENT: " + requestEvent);

        try {
            List<String> gameRecords = mapper.fromJson(requestEvent.getBody(), ArrayList.class);

            if (gameRecords == null) {
                responseEvent.setStatusCode(HttpStatusCode.BAD_REQUEST);
                responseEvent.setBody("Missing body in request.");
                return responseEvent;
            }

            responseEvent.setStatusCode(HttpStatusCode.ACCEPTED);
            responseEvent.setBody("It works!");
            return responseEvent;

        }catch (JsonSyntaxException e){
            responseEvent.setStatusCode(HttpStatusCode.BAD_REQUEST);
            responseEvent.setBody("Invalid data provided.");
            return responseEvent;
        }
    }


}

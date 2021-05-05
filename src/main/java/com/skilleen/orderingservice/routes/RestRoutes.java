package com.skilleen.orderingservice.routes;

import com.skilleen.orderingservice.dto.Order;
import com.skilleen.orderingservice.services.OrderService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestRoutes extends RouteBuilder {

    @Autowired
    private OrderService orderService;

    @Override
    public void configure() {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json);

        rest().get("/hello-world").produces(MediaType.APPLICATION_JSON_VALUE)
                .route().setBody(constant("Hello World From the Order Service!"));

        rest().get("/get-orders")
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .route()
                .setBody(() -> orderService.getOrders());

        rest().post("add-order")
                .type(Order.class)
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .to("direct:add-order");

        from("direct:add-order")
                .multicast()
                .to("direct:insert-new-order")
                .to("direct:create-shipping-request");


       rest().post("publish-message")
                .type(String.class)
                .to("direct:publish");

    }

}

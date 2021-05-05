package com.skilleen.orderingservice.routes;

import com.skilleen.orderingservice.entities.OrderEntity;
import com.skilleen.orderingservice.services.OrderAdapter;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DatabaseRoutes extends RouteBuilder {

    @Autowired
    private OrderAdapter orderAdapter;

    @Override
    public void configure() {
        from("direct:insert-new-order")
                .bean(orderAdapter, "adaptToOrderEntity")
                .log("Saving Order to Database: ")
                .to("jpa:" + OrderEntity.class.getName() + "?useExecuteUpdate=true");
    }
}

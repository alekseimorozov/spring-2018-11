package ru.otus.training.alekseimorozov.otuschef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import ru.otus.training.alekseimorozov.otuschef.chefservice.ChefService;
import ru.otus.training.alekseimorozov.otuschef.domen.Meal;
import ru.otus.training.alekseimorozov.otuschef.domen.Product;

import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        DirectChannel productChannel = context.getBean(DirectChannel.class);
        PollableChannel orderChannel = context.getBean("orderChannel", PollableChannel.class);
        ChefService service = context.getBean(ChefService.class);

        productChannel.subscribe(message -> orderChannel.send(
                MessageBuilder.withPayload(service.cook((Product) message.getPayload())).build()
        ));

        System.out.println("OTUS cafe is opened");

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                Product product = getRundomProduct();
                System.out.printf("%s was sent to cheff \n", product.getName());
                productChannel.send(MessageBuilder.withPayload(product).build());
            }
        }).start();

        for (int i = 0; i < 30; i++) {
            Meal meal = (Meal) orderChannel.receive().getPayload();
            System.out.printf("We got from chef %s \n", meal.getName());
            Thread.sleep(1000);
        }

        System.out.println("OTUS cafe is closed");
    }

    @Bean
    public DirectChannel productChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public PollableChannel orderChannel() {
        return MessageChannels.queue(10).get();
    }

    private static Product getRundomProduct() {
        String[] productStore = new String[]{
                "meat", "eggs", "fish", "tomato", "potato", "mushroom", "chicken", "onion"
        };
        int productIndex = new Random().nextInt(productStore.length);
        return new Product(productStore[productIndex]);
    }
}
package ecommerce.api;

import ecommerce.api.entity.Cart;
import ecommerce.api.entity.User;
import ecommerce.api.repository.CartRepository;
import ecommerce.api.repository.UserRepository;
import ecommerce.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class EcommerceApplication {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("");
            }
        };
    }


    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        if(userRepository.findByEmail("admin@email.com") == null) {
            User user = new User();
            user.setPassword("$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu");
            user.setPhone("123");
            user.setName("Admin");
            user.setAddress("test");
            user.setActive(true);
            user.setRole("ROLE_ADMIN");
            user.setEmail("admin@email.com");
            User savedUser= userRepository.save(user);

            cartRepository.save(new Cart(savedUser));
        }
    }

}


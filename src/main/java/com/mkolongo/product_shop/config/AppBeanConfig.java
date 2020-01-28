package com.mkolongo.product_shop.config;

import com.mkolongo.product_shop.domain.entities.*;
import com.mkolongo.product_shop.domain.models.service.OrderServiceModel;
import com.mkolongo.product_shop.domain.models.service.ProductServiceModel;
import com.mkolongo.product_shop.domain.models.view.OrderDetailsModel;
import com.mkolongo.product_shop.domain.models.view.OrderViewModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class AppBeanConfig {

    @Bean
    @SuppressWarnings("unchecked")
    public ModelMapper mapper() {
        final ModelMapper mapper = new ModelMapper();

        Converter<Set<Category>, Set<String>> getCategoryName = ctx -> ctx.getSource()
                .stream()
                .map(NamedEntity::getName)
                .collect(Collectors.toSet());

        Converter<LocalDateTime, String> dateFormatter = ctx -> ctx.getSource()
                .format(DateTimeFormatter.ofPattern("EE yyyy-MMM-dd hh:mm a"));

        mapper.createTypeMap(Product.class, ProductServiceModel.class)
                .addMappings(m -> m.using(getCategoryName)
                        .map(Product::getCategories,
                                (destination, value) -> destination.setCategories(((Set<String>) value))));

        mapper.createTypeMap(Order.class, OrderServiceModel.class)
                .addMappings(m -> {
                    m.map(source -> source.getUser().getUsername(),
                            (destination, value) -> destination.setCustomerName(((String) value)));
                    m.map(source -> source.getProduct().getImageUrl(),
                            (destination, value) -> destination.setImageUrl(((String) value)));
                    m.using(dateFormatter).map(Order::getOrderDate,
                            (destination, value) -> destination.setOrderDate(((String) value)));
                    m.map(source -> source.getProduct().getName(),
                            (destination, value) -> destination.setProductName(((String) value)));
                    m.map(source -> source.getProduct().getDescription(),
                            (destination, value) -> destination.setProductDescription(((String) value)));
                });

        mapper.createTypeMap(Order.class, OrderDetailsModel.class)
                .addMappings(m -> {
                    m.map(source -> source.getProduct().getImageUrl(),
                            (destination, value) -> destination.setImageUrl(((String) value)));
                    m.map(source -> source.getProduct().getName(),
                            (destination, value) -> destination.setProductName(((String) value)));
                    m.map(source -> source.getProduct().getDescription(),
                            (destination, value) -> destination.setProductDescription(((String) value)));
                    m.map(source -> source.getUser().getUsername(),
                            (destination, value) -> destination.setCustomerName(((String) value)));
                    m.using(dateFormatter).map(Order::getOrderDate,
                            (destination, value) -> destination.setOrderDate(((String) value)));
                });

        return mapper;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(String.format("%s > %s > %s > %s",
                Role.ROLE_ROOT,
                Role.ROLE_ADMIN,
                Role.ROLE_MODERATOR,
                Role.ROLE_USER));

        return roleHierarchy;
    }
}

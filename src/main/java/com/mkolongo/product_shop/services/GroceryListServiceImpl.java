package com.mkolongo.product_shop.services;

import com.mkolongo.product_shop.domain.entities.GroceryList;
import com.mkolongo.product_shop.domain.entities.Product;
import com.mkolongo.product_shop.domain.entities.User;
import com.mkolongo.product_shop.domain.models.service.OrderServiceModel;
import com.mkolongo.product_shop.domain.models.view.OrderDetailsModel;
import com.mkolongo.product_shop.exception.OrderNotFoundException;
import com.mkolongo.product_shop.exception.ProductNotFoundException;
import com.mkolongo.product_shop.repositories.GroceryListRepository;
import com.mkolongo.product_shop.repositories.ProductRepository;
import com.mkolongo.product_shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroceryListServiceImpl implements GroceryListService {

    private final GroceryListRepository groceryListRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public void addProductToList(String productId, int quantity, String customerName) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id <u>" + productId + "</u> does not exist!"));

        final User user = userRepository.findUserByUsername(customerName)
                .orElseThrow(() -> new UsernameNotFoundException("User with username <u>" + customerName + "</u> does not exist!"));

        GroceryList groceryList = new GroceryList();
        groceryList.setCreatedAt(LocalDateTime.now());
        groceryList.getProducts().add(product);
        groceryList.setCompleted(false);
        groceryList.setUser(user);

        groceryListRepository.save(groceryList);
    }

    @Override
    public OrderDetailsModel getById(String id) {
        return groceryListRepository.findById(id)
                .map(groceryList -> mapper.map(groceryList, OrderDetailsModel.class))
                .orElseThrow(() -> {
                    throw new OrderNotFoundException("Order with id <u>" + id + "</u> does not exist!");
                });
    }

    @Override
    public Set<OrderServiceModel> getOrdersByCustomerName(String customerName) {
        return mapper.map(groceryListRepository.findByUser_Email(customerName),
                new TypeToken<Set<OrderServiceModel>>() {}.getType());
    }

    @Override
    public Set<OrderServiceModel> getAllOrders() {
        return mapper.map(groceryListRepository.findAll(),
                new TypeToken<Set<OrderServiceModel>>() {}.getType());
    }

    @Override
    public void delete(String id) {
        groceryListRepository.deleteById(id);
    }
}

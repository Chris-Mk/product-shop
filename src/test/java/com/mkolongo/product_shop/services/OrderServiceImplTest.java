package com.mkolongo.product_shop.services;

import com.mkolongo.product_shop.domain.entities.Order;
import com.mkolongo.product_shop.domain.entities.Product;
import com.mkolongo.product_shop.domain.entities.User;
import com.mkolongo.product_shop.domain.models.service.OrderServiceModel;
import com.mkolongo.product_shop.domain.models.view.OrderDetailsModel;
import com.mkolongo.product_shop.exception.OrderNotFoundException;
import com.mkolongo.product_shop.exception.ProductNotFoundException;
import com.mkolongo.product_shop.repositories.OrderRepository;
import com.mkolongo.product_shop.repositories.ProductRepository;
import com.mkolongo.product_shop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    ProductRepository mockProductRepository;
    @Mock
    OrderRepository mockOrderRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    ModelMapper mockMapper;

    @InjectMocks
    OrderServiceImpl mockOrderService;

    Order mockOrder;
    OrderServiceModel mockServiceModel;

    Product mockProduct;
    User mockUser;

    @BeforeEach
    void setUp() {
        mockProduct = new Product() {{
            setId("test id");
            setPrice(BigDecimal.TEN);
        }};

        mockUser = new User() {{
            setUsername("test username");
        }};

        mockOrder = new Order() {{
            setId("test id");
            setQuantity(12);
            setTotalPrice(BigDecimal.TEN);
            setUser(new User());
            setProduct(mockProduct);
        }};

        mockServiceModel = new OrderServiceModel() {{
            setImageUrl("test url");
            setCustomerName("test username");
            setTotalPrice(BigDecimal.TEN);
        }};
    }

    @Test
    void addOrder_withValidInputData_workFine() {
        when(mockProductRepository.findById(mockProduct.getId())).thenReturn(Optional.ofNullable(mockProduct));
        when(mockUserRepository.findUserByUsername(mockUser.getUsername())).thenReturn(Optional.ofNullable(mockUser));

        mockOrderService.addOrder(mockProduct.getId(), mockOrder.getQuantity() , mockUser.getUsername());

        verify(mockOrderRepository).save(any(Order.class));
    }

    @Test
    void addOrder_withInvalidProductId_throwsException() {
        when(mockProductRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
                () -> mockOrderService.addOrder(anyString(), mockOrder.getQuantity(), mockUser.getUsername()));
    }

    @Test
    void addOrder_withInvalidCustomerName_throwsException() {
        when(mockProductRepository.findById(anyString())).thenReturn(Optional.ofNullable(mockProduct));
        when(mockUserRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> mockOrderService.addOrder(mockProduct.getId(), mockOrder.getQuantity(), anyString()));
    }

    @Test
    void getById_withValidId_returnsOrder() {
        when(mockOrderRepository.findById(mockOrder.getId())).thenReturn(Optional.ofNullable(mockOrder));
        final OrderDetailsModel viewModel = new OrderDetailsModel() {{
            setId("test id");
            setQuantity(12);
            setCustomerName("test customer name");
            setProductDescription("test product description");
            setImageUrl("test image url");
            setTotalPrice(BigDecimal.TEN);
        }};

        when(mockMapper.map(mockOrder, OrderDetailsModel.class)).thenReturn(viewModel);

        final OrderDetailsModel order = mockOrderService.getById(mockOrder.getId());

        assertEquals(viewModel.getId(), order.getId());
        assertEquals(viewModel.getCustomerName(), order.getCustomerName());
        assertEquals(viewModel.getImageUrl(), order.getImageUrl());
        assertEquals(viewModel.getProductDescription(), order.getProductDescription());
        assertEquals(viewModel.getQuantity(), order.getQuantity());
        assertEquals(viewModel.getTotalPrice(), order.getTotalPrice());
    }

    @Test
    void getById_withInvalidOrderId_throwsException() {
        when(mockOrderRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> mockOrderService.getById(anyString()));
    }

    @Test
    void getOrderByCustomerName_withValidCustomerName_returnsOrders() {
        final Set<Order> orders = Set.of(this.mockOrder);
        when(mockOrderRepository.findByUser_Username(mockUser.getUsername())).thenReturn(orders);
        when(mockMapper.map(orders, new TypeToken<Set<OrderServiceModel>>() {}.getType())).thenReturn(Set.of(mockServiceModel));

        final Set<OrderServiceModel> serviceModels = mockOrderService.getOrdersByCustomerName(mockUser.getUsername());

        assertEquals(1, serviceModels.size());
    }

    @Test
    void getAllOrders_returnsOrdersList() {
        final List<Order> orders = List.of(this.mockOrder);
        when(mockOrderRepository.findAll()).thenReturn(orders);
        when(mockMapper.map(orders, new TypeToken<Set<OrderServiceModel>>() {}.getType())).thenReturn(Set.of(mockServiceModel));

        final Set<OrderServiceModel> serviceModels = mockOrderService.getAllOrders();

        assertEquals(1, serviceModels.size());
    }
}
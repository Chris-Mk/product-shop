package com.mkolongo.product_shop.services;

import com.mkolongo.product_shop.domain.entities.GroceryList;
import com.mkolongo.product_shop.domain.entities.Product;
import com.mkolongo.product_shop.domain.entities.User;
import com.mkolongo.product_shop.domain.models.service.OrderServiceModel;
import com.mkolongo.product_shop.domain.models.view.OrderDetailsModel;
import com.mkolongo.product_shop.exception.OrderNotFoundException;
import com.mkolongo.product_shop.repositories.GroceryListRepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroceryListServiceImplTest {

//    @Mock
//    ProductRepository mockProductRepository;
//    @Mock
//    GroceryListRepository mockGroceryListRepository;
//    @Mock
//    UserRepository mockUserRepository;
//    @Mock
//    ModelMapper mockMapper;
//
//    @InjectMocks
//    GroceryListServiceImpl mockOrderService;
//
//    GroceryList mockGroceryList;
//    OrderServiceModel mockServiceModel;
//
//    Product mockProduct;
//    User mockUser;
//
//    @BeforeEach
//    void setUp() {
//        mockProduct = new Product() {{
//            setId("test id");
//            setPrice(BigDecimal.TEN);
//        }};
//
//        mockUser = new User() {{
//            setUsername("test username");
//        }};
//
//        mockGroceryList = new GroceryList() {{
//            setId("test id");
////            setQuantity(12);
////            setTotalPrice(BigDecimal.TEN);
//            setUser(new User());
////            setProduct(mockProduct);
//        }};
//
//        mockServiceModel = new OrderServiceModel() {{
//            setImageUrl("test url");
//            setCustomerName("test username");
//            setTotalPrice(BigDecimal.TEN);
//        }};
//    }
//
//    @Test
//    void addOrder_withValidInputData_workFine() {
//        when(mockProductRepository.findById(mockProduct.getId())).thenReturn(Optional.ofNullable(mockProduct));
//        when(mockUserRepository.findUserByUsername(mockUser.getUsername())).thenReturn(Optional.ofNullable(mockUser));
//
////        mockOrderService.addOrder(mockProduct.getId(), mockGroceryList.getQuantity() , mockUser.getUsername());
//
//        verify(mockGroceryListRepository).save(any(GroceryList.class));
//    }
//
//    @Test
//    void addOrder_withInvalidProductId_throwsException() {
//        when(mockProductRepository.findById(anyString())).thenReturn(Optional.empty());
////        assertThrows(ProductNotFoundException.class,
////                () -> mockOrderService.addOrder(anyString(), mockGroceryList.getQuantity(), mockUser.getUsername()));
//    }
//
//    @Test
//    void addOrder_withInvalidCustomerName_throwsException() {
//        when(mockProductRepository.findById(anyString())).thenReturn(Optional.ofNullable(mockProduct));
//        when(mockUserRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
//
////        assertThrows(UsernameNotFoundException.class,
////                () -> mockOrderService.addOrder(mockProduct.getId(), mockGroceryList.getQuantity(), anyString()));
//    }
//
//    @Test
//    void getById_withValidId_returnsOrder() {
//        when(mockGroceryListRepository.findById(mockGroceryList.getId())).thenReturn(Optional.ofNullable(mockGroceryList));
//        final OrderDetailsModel viewModel = new OrderDetailsModel() {{
//            setId("test id");
//            setQuantity(12);
//            setCustomerName("test customer name");
//            setProductDescription("test product description");
//            setImageUrl("test image url");
//            setTotalPrice(BigDecimal.TEN);
//        }};
//
//        when(mockMapper.map(mockGroceryList, OrderDetailsModel.class)).thenReturn(viewModel);
//
//        final OrderDetailsModel order = mockOrderService.getById(mockGroceryList.getId());
//
//        assertEquals(viewModel.getId(), order.getId());
//        assertEquals(viewModel.getCustomerName(), order.getCustomerName());
//        assertEquals(viewModel.getImageUrl(), order.getImageUrl());
//        assertEquals(viewModel.getProductDescription(), order.getProductDescription());
//        assertEquals(viewModel.getQuantity(), order.getQuantity());
//        assertEquals(viewModel.getTotalPrice(), order.getTotalPrice());
//    }
//
//    @Test
//    void getById_withInvalidOrderId_throwsException() {
//        when(mockGroceryListRepository.findById(anyString())).thenReturn(Optional.empty());
//        assertThrows(OrderNotFoundException.class, () -> mockOrderService.getById(anyString()));
//    }
//
//    @Test
//    void getOrderByCustomerName_withValidCustomerName_returnsOrders() {
//        final Set<GroceryList> groceryLists = Set.of(this.mockGroceryList);
//        when(mockGroceryListRepository.findByUser_Email()).thenReturn(groceryLists);
//        when(mockMapper.map(groceryLists, new TypeToken<Set<OrderServiceModel>>() {}.getType())).thenReturn(Set.of(mockServiceModel));
//
//        final Set<OrderServiceModel> serviceModels = mockOrderService.getOrdersByCustomerName(mockUser.getUsername());
//
//        assertEquals(1, serviceModels.size());
//    }
//
//    @Test
//    void getAllOrders_returnsOrdersList() {
//        final List<GroceryList> groceryLists = List.of(this.mockGroceryList);
//        when(mockGroceryListRepository.findAll()).thenReturn(groceryLists);
//        when(mockMapper.map(groceryLists, new TypeToken<Set<OrderServiceModel>>() {}.getType())).thenReturn(Set.of(mockServiceModel));
//
//        final Set<OrderServiceModel> serviceModels = mockOrderService.getAllOrders();
//
//        assertEquals(1, serviceModels.size());
//    }
}
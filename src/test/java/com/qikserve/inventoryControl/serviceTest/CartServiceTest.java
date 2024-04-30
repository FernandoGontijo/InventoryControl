package com.qikserve.inventoryControl.serviceTest;


import com.qikserve.inventoryControl.dto.CartDTO;
import com.qikserve.inventoryControl.dto.ProductDTO;
import com.qikserve.inventoryControl.dto.PromotionDTO;
import com.qikserve.inventoryControl.model.Cart;
import com.qikserve.inventoryControl.model.Customer;
import com.qikserve.inventoryControl.model.Product;
import com.qikserve.inventoryControl.repository.CartRepository;
import com.qikserve.inventoryControl.service.CartService;
import com.qikserve.inventoryControl.service.ProductService;
import com.qikserve.inventoryControl.service.PromotionService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private CartService cartService;


    String cartId = "dEW2K3pS5U";
    ProductDTO burgerDTO = new ProductDTO("PWWe3w1SDU", "Amazing Burger!", 999);
    ProductDTO pizzaDTO = new ProductDTO("Dwt5F7KAhi", "Amazing Pizza!", 1099);
    ProductDTO saladDTO = new ProductDTO("C8GDyLrHJb", "Amazing Salad!", 499);
    ProductDTO friesDTO = new ProductDTO("4MB7UfpTQs", "Boring Fries!", 199);

    Product burger = new Product("PWWe3w1SDU", "Amazing Burger!", 999);
    Product pizza = new Product("Dwt5F7KAhi", "Amazing Pizza!", 1099);
    Product salad = new Product("C8GDyLrHJb", "Amazing Salad!", 499);
    Product fries = new Product("4MB7UfpTQs", "Boring Fries!", 199);

    Customer customer = new Customer("Kwe5G7KA3x", "Customer Test");

    List<ProductDTO> productsDTO = Arrays.asList(burgerDTO, pizzaDTO, saladDTO, friesDTO);

    List<ProductDTO> productsDTOTest = new ArrayList<>();

    List<Product> products = Arrays.asList(burger, pizza, salad, fries);
    CartDTO cartDTO = new CartDTO(cartId, 0, 0, 0,customer, new ArrayList<>());
    Cart cart = new Cart(cartId, 0, 0, 0,customer, new ArrayList<>());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAllProductToCartOneTime() {

        when(promotionService.findAllByProduct(burgerDTO.getId())).thenReturn(Collections.singletonList(
                new PromotionDTO("ZRAwbsO2qM", "BUY_X_GET_Y_FREE", 2, 1,
                        0, 0, burger)));

        when(promotionService.findAllByProduct(pizzaDTO.getId())).thenReturn(Collections.singletonList(
                new PromotionDTO("ibt3EEYczW", "QTY_BASED_PRICE_OVERRIDE", 0,2,
                        1799, 0, pizza)));

        when(promotionService.findAllByProduct(saladDTO.getId())).thenReturn(Collections.singletonList(
                new PromotionDTO("Gm1piPn7Fg", "FLAT_PERCENT", 10, 0,
                        0, 0, salad)));

        when(promotionService.findAllByProduct(friesDTO.getId())).thenReturn(Collections.emptyList());
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        doNothing().when(productService).checkProduct(any());

        CartDTO result = cartService.addProductToCart(productsDTO, cartId);

        assertEquals(cartId, result.getId());
        assertEquals(4, result.getQuantity());
        assertEquals(2747, result.getTotalPrice());
        assertEquals(50, result.getTotalSavings());

        verify(cartRepository, times(1)).findById(cartId);
        verify(productService, times(4)).checkProduct(any());
    }

    @Test
    void testAddAllPromotions() {

        productsDTOTest.add(burgerDTO);
        productsDTOTest.add(pizzaDTO);
        productsDTOTest.add(saladDTO);
        productsDTOTest.add(friesDTO);
        productsDTOTest.add(burgerDTO);
        productsDTOTest.add(pizzaDTO);
        productsDTOTest.add(saladDTO);
        productsDTOTest.add(friesDTO);


        when(promotionService.findAllByProduct(burgerDTO.getId())).thenReturn(Collections.singletonList(
                new PromotionDTO("ZRAwbsO2qM", "BUY_X_GET_Y_FREE", 2, 1,
                        0, 0, burger)));

        when(promotionService.findAllByProduct(pizzaDTO.getId())).thenReturn(Collections.singletonList(
                new PromotionDTO("ibt3EEYczW", "QTY_BASED_PRICE_OVERRIDE", 0,2,
                        1799, 0, pizza)));

        when(promotionService.findAllByProduct(saladDTO.getId())).thenReturn(Collections.singletonList(
                new PromotionDTO("Gm1piPn7Fg", "FLAT_PERCENT", 10, 0,
                        0, 0, salad)));

        when(promotionService.findAllByProduct(friesDTO.getId())).thenReturn(Collections.emptyList());
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        doNothing().when(productService).checkProduct(any());

        CartDTO result = cartService.addProductToCart(productsDTOTest, cartId);

        assertEquals(cartId, result.getId());
        assertEquals(8, result.getQuantity());
        assertEquals(4096, result.getTotalPrice());
        assertEquals(1498, result.getTotalSavings());

        verify(cartRepository, times(1)).findById(cartId);
        verify(productService, times(8)).checkProduct(any());
    }
}

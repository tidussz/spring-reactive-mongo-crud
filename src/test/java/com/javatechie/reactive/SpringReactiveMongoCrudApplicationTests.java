package com.javatechie.reactive;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.javatechie.reactive.controller.ProductController;
import com.javatechie.reactive.dto.ProductDto;
import com.javatechie.reactive.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringReactiveMongoCrudApplicationTests {

	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private ProductService productService;

	@Test
	public void addProductTest() {
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "Mobile", 1, 10000));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);
		
		webTestClient.post().uri("/products")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}
	
	@Test
	public void getProductsTest() {
		
		ProductDto productDto1 = new ProductDto("102", "Mobile", 1, 10000);
		ProductDto productDto2 = new ProductDto("103", "TV", 1, 50000);
		Flux<ProductDto> productDtoFlux = Flux.just(productDto1, productDto2);
		
		when(productService.getProducts()).thenReturn(productDtoFlux);
		
		Flux<ProductDto> responseBody = webTestClient.get().uri("/products")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();
		
		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(productDto1)
				.expectNext(productDto2)
				.verifyComplete();
	}
	
	@Test
	public void getProductTest() {
		ProductDto productDto1 = new ProductDto("102", "Mobile", 1, 10000);
		Mono<ProductDto> productDtoMono = Mono.just(productDto1);
		
		when(productService.getProduct(any())).thenReturn(productDtoMono);
		
		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/102")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();
		
		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p -> p.getName().equals("Mobile"))
				.verifyComplete();
	}
	
	@Test
	public void updateProductTest() {
		ProductDto productDto1 = new ProductDto("102", "Mobile", 1, 10000);
		Mono<ProductDto> productDtoMono = Mono.just(productDto1);
		
		when(productService.updateProduct(productDtoMono, "102")).thenReturn(productDtoMono);
		
		webTestClient.put().uri("/products/update/102")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}
	
	@Test
	public void deleteProductTest() {
		BDDMockito.given(productService.deleteProduct(any())).willReturn(Mono.empty());
		
		webTestClient.delete().uri("/products/delete/102")
			.exchange()
			.expectStatus().isOk();
	}
}

package com.javatechie.reactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;

import com.javatechie.reactive.dto.ProductDto;
import com.javatechie.reactive.repository.ProductRepository;
import com.javatechie.reactive.utils.AppUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Flux<ProductDto> getProducts() {
		return productRepository.findAll().map(AppUtils::entityToDto);
	}

	public Mono<ProductDto> getProduct(String id) {
		return productRepository.findById(id).map(AppUtils::entityToDto);
	}

	public Flux<ProductDto> getProductInRange(double min, double max) {
		return productRepository.findByPriceBetween(Range.closed(min, max)).map(AppUtils::entityToDto);
	}

	public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
		return productDtoMono.map(AppUtils::dtoToEntity)
				.flatMap(productRepository::insert)
				.map(AppUtils::entityToDto);
	}
	
	public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
		return productRepository.findById(id)
			.flatMap(p -> productDtoMono.map(AppUtils::dtoToEntity)
				.doOnNext(e -> e.setId(id)))
			.flatMap(productRepository::save)
			.map(AppUtils::entityToDto);
	}
	
	public Mono<Void> deleteProduct(String id) {
		return productRepository.deleteById(id);
	}
}

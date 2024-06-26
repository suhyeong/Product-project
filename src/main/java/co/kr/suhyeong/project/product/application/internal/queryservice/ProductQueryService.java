package co.kr.suhyeong.project.product.application.internal.queryservice;

import co.kr.suhyeong.project.exception.ApiException;
import co.kr.suhyeong.project.product.domain.command.GetProductImageCommand;
import co.kr.suhyeong.project.product.domain.command.GetProductListCommand;
import co.kr.suhyeong.project.product.domain.model.aggregate.Product;
import co.kr.suhyeong.project.product.domain.model.view.ProductImageView;
import co.kr.suhyeong.project.product.domain.model.view.ProductView;
import co.kr.suhyeong.project.product.domain.repository.ProductRepository;
import co.kr.suhyeong.project.product.domain.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static co.kr.suhyeong.project.constants.ResponseCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    public ProductView getProduct(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ApiException(NON_EXIST_DATA));
        return new ProductView(product);
    }

    public List<Product> getProductList(GetProductListCommand command) {
        try {
            return productRepository.findProductListWithPagination(command.getPageable());
        } catch (Exception e) {
            throw new ApiException(SERVER_ERROR);
        }
    }

    public List<ProductImageView> getProductImages(GetProductImageCommand command) {
        Product product = productRepository.findByProductCode(command.getProductCode())
                .orElseThrow(() -> new ApiException(NON_EXIST_DATA));

        return product.getImages().stream()
                .map(image -> new ProductImageView(image.getImageDivCode(), image.getImgPath()))
                .collect(Collectors.toList());
    }
}

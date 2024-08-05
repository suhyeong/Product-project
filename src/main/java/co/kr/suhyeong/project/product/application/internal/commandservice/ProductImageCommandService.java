package co.kr.suhyeong.project.product.application.internal.commandservice;

import co.kr.suhyeong.project.constants.ResponseCode;
import co.kr.suhyeong.project.exception.ApiException;
import co.kr.suhyeong.project.product.domain.command.ModifyProductImageCommand;
import co.kr.suhyeong.project.product.domain.model.aggregate.Product;
import co.kr.suhyeong.project.product.domain.repository.ProductRepository;
import co.kr.suhyeong.project.product.domain.service.S3BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageCommandService {
    private final ProductRepository productRepository;
    private final S3BucketService s3BucketService;

    public List<String> uploadProductTempImages(List<MultipartFile> files) {
        return s3BucketService.putTempImages(files);
    }

    public String uploadProductTempImage(MultipartFile file) {
        return s3BucketService.putTempImage(file);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyProductImages(ModifyProductImageCommand command) {
        Product product = productRepository.findById(command.getProductCode())
                .orElseThrow(() -> new ApiException(ResponseCode.NON_EXIST_DATA));

    }
}

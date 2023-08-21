package ecommerce.api.service.impl;


import ecommerce.api.entity.ProductCategory;
import ecommerce.api.repository.ProductInfoRepository;
import ecommerce.api.entity.ProductInfo;
import ecommerce.api.enums.ProductStatusEnum;
import ecommerce.api.enums.ResultEnum;
import ecommerce.api.exception.EcommerceException;
import ecommerce.api.service.CategoryService;
import ecommerce.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    CategoryService categoryService;

    @Override
    public ProductInfo findOne(Long productId) {

        ProductInfo productInfo = productInfoRepository.findByProductId(productId);
        return productInfo;
    }

    @Override
    public Page<ProductInfo> findUpAll(Pageable pageable) {
        return productInfoRepository.findAllByProductStatusAndDeletedFalseOrderByProductIdAsc(ProductStatusEnum.UP.getCode(),pageable);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAllByAndDeletedFalseOrderByProductId(pageable);
    }

    @Override
    public Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable) {
        return productInfoRepository.findAllByProductCategory_CategoryIdAndDeletedFalseOrderByProductIdAsc(categoryType, pageable);
    }

    @Override
    @Transactional
    public void increaseStock(Long productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new EcommerceException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() + amount;
        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void decreaseStock(Long productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new EcommerceException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() - amount;
        if(update <= 0) throw new EcommerceException(ResultEnum.PRODUCT_NOT_ENOUGH );

        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo offSale(Long productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new EcommerceException(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == ProductStatusEnum.DOWN.getCode()) {
            throw new EcommerceException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo onSale(Long productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new EcommerceException(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == ProductStatusEnum.UP.getCode()) {
            throw new EcommerceException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo update(ProductInfo productInfo) {

        // if null throw exception
        ProductInfo productInfoExists= productInfoRepository.findByProductId(productInfo.getProductId());
        if(productInfoExists == null || productInfoExists.getProductStatus() > 1) {
            throw new EcommerceException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        ProductCategory productCategory= categoryService.findByCategoryType(productInfo.getProductCategory().getCategoryId());
        productInfo.setProductCategory(productCategory);
        return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        ProductCategory productCategory= categoryService.findByCategoryType(productInfo.getProductCategory().getCategoryId());
        productInfo.setProductCategory(productCategory);
        productInfo.setDeleted(false);
        return productInfoRepository.save(productInfo);
    }

    @Override
    public void delete(Long productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new EcommerceException(ResultEnum.PRODUCT_NOT_EXIST);
        productInfo.setDeleted(true);
        productInfoRepository.save(productInfo);

    }


}

package gtu.mybatis.example.mapper;

import gtu.mybatis.example.domain.Product;

import java.util.List;

public interface ProductMapper {

    List<Product> getProductListByCategory(String categoryId);

    Product getProduct(String productId);

    List<Product> searchProductList(String keywords);

}

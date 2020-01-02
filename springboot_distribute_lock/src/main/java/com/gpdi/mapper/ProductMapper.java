package com.gpdi.mapper;

import com.gpdi.entity.Product;
import java.util.List;

public interface ProductMapper {

    //查询所有的库存信息
    public List<Product> getAllProduct();

    //根据产品id查询产品库存
    public Product getProductNumberById(String productId);

    //减少库存
    public void updateProduct(Product product);
}

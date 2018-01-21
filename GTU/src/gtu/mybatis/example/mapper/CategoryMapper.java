package gtu.mybatis.example.mapper;

import gtu.mybatis.example.domain.Category;

import java.util.List;

public interface CategoryMapper {

    List<Category> getCategoryList();

    Category getCategory(String categoryId);

}

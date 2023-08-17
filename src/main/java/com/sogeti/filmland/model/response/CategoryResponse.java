package com.sogeti.filmland.model.response;

import java.util.List;

import com.sogeti.filmland.model.DTO.CategoryDTO;
import com.sogeti.filmland.model.DTO.SubscribedCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private List<CategoryDTO> availableCategories;
    private List<SubscribedCategoryDTO> subscribedCategories;

}

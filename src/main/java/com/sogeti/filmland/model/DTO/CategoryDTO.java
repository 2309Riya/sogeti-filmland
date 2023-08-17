package com.sogeti.filmland.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	public CategoryDTO(Long id, String name) {
		// TODO Auto-generated constructor stub
	}
	private Long id;
    private String name;
    private int availableContent;
    private double price;
    
}
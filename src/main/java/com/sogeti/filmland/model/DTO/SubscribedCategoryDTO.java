package com.sogeti.filmland.model.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscribedCategoryDTO {

	private String name;
    private int remainingContent;
    private double price;
    private LocalDate startDate;
 
}
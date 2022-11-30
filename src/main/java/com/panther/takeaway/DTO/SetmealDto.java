package com.panther.takeaway.DTO;

import com.panther.takeaway.entity.Setmeal;
import com.panther.takeaway.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

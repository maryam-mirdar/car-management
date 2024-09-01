package org.mirdar.api.model.dto.filter;

import com.mwga.storage.orm.model.PageableFilterable;
import lombok.Getter;
import lombok.Setter;
import org.mirdar.api.model.dto.filter.CarFilter;

@Getter
@Setter
public class CarPageableFilter extends CarFilter implements PageableFilterable {
    private Integer page;
    private Integer size;

    public CarPageableFilter() {
        this.page = 1;
        this.size = 15;
    }
}